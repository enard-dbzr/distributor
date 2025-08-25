package com.plux.distribution.application.service;

import com.plux.distribution.application.dto.feedback.CreateFeedbackCommand;
import com.plux.distribution.application.dto.message.CreateMessageCommand;
import com.plux.distribution.application.port.in.FeedbackProcessor;
import com.plux.distribution.application.port.in.RegisterFeedbackUseCase;
import com.plux.distribution.application.port.in.chat.CreateChatUseCase;
import com.plux.distribution.application.dto.feedback.ButtonContext;
import com.plux.distribution.application.dto.feedback.MessageContext;
import com.plux.distribution.application.port.exception.ChatIdNotFound;
import com.plux.distribution.application.port.in.message.CreateMessageUseCase;
import com.plux.distribution.application.port.out.feedback.CreateFeedbackPort;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.feedback.payload.ButtonPayload;
import com.plux.distribution.domain.feedback.payload.MessagePayload;
import com.plux.distribution.domain.feedback.payload.ReplyPayload;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.message.participant.UnknownServiceParticipant;
import com.plux.distribution.domain.message.participant.ChatParticipant;
import com.plux.distribution.domain.message.state.ReceivedState;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegisterFeedbackService implements RegisterFeedbackUseCase {

    private final CreateMessageUseCase createMessageUseCase;
    private final CreateFeedbackPort createFeedbackPort;
    private final CreateChatUseCase createChatUseCase;
    private final FeedbackProcessor feedbackProcessor;

    public RegisterFeedbackService(CreateMessageUseCase createMessageUseCase,
            CreateFeedbackPort createFeedbackPort, CreateChatUseCase createChatUseCase,
            FeedbackProcessor feedbackProcessor) {
        this.createMessageUseCase = createMessageUseCase;
        this.createFeedbackPort = createFeedbackPort;
        this.createChatUseCase = createChatUseCase;
        this.feedbackProcessor = feedbackProcessor;
    }

    private void registerAndProcess(@NotNull CreateFeedbackCommand command, @Nullable MessageContent content) {
        var feedback = createFeedbackPort.create(command);

        var feedbackContext = new FeedbackContext(feedback, Optional.ofNullable(content));
        feedbackProcessor.process(feedbackContext);
    }

    @Override
    public void handle_message(@NotNull MessageContext context) {
        var receiveTime = new Date();

        ChatId chatId;
        try {
            chatId = context.getChatId();
        } catch (ChatIdNotFound e) {
            chatId = createChatUseCase.create().id();
            context.onChatCreated(chatId);
            System.out.println("Created new chat");
        }

        var content = new SimpleMessageContent(context.getText(), List.of());
        var messageId = createMessageUseCase.create(new CreateMessageCommand(
                new ChatParticipant(chatId),
                new UnknownServiceParticipant(),
                new ReceivedState(new Date()),
                content
        ));
        context.onMessageCreated(messageId);

        var createFeedbackCommand = makeFeedbackCommand(context, messageId, receiveTime, chatId);

        registerAndProcess(createFeedbackCommand, content);
    }

    @Override
    public void handle_button(@NotNull ButtonContext context) {
        var receiveTime = new Date();

        var createFeedbackCommand = makeFeedbackCommand(context, receiveTime);

        registerAndProcess(createFeedbackCommand, null);
    }

    private static CreateFeedbackCommand makeFeedbackCommand(MessageContext context, MessageId content,
            Date receivedAt, ChatId chatId) {
        if (context.getReplyTo() == null) {
            return new CreateFeedbackCommand(
                    receivedAt,
                    chatId,
                    new MessagePayload(content)
            );
        }

        return new CreateFeedbackCommand(
                receivedAt,
                chatId,
                new ReplyPayload(context.getReplyTo(), content)
        );

    }

    private static CreateFeedbackCommand makeFeedbackCommand(ButtonContext context, Date receivedAt) {
        try {
            return new CreateFeedbackCommand(
                    receivedAt,
                    context.getChatId(),
                    new ButtonPayload(context.getReplyTo(), context.getTag())
            );
        } catch (ChatIdNotFound chatIdNotFound) {
            throw new RuntimeException(chatIdNotFound);
        }
    }
}
