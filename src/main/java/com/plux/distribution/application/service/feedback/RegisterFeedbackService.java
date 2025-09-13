package com.plux.distribution.application.service.feedback;

import com.plux.distribution.application.dto.feedback.command.CreateFeedbackCommand;
import com.plux.distribution.application.dto.message.CreateMessageCommand;
import com.plux.distribution.application.dto.message.MessageDto;
import com.plux.distribution.application.port.in.feedback.FeedbackProcessor;
import com.plux.distribution.application.port.in.feedback.RegisterFeedbackUseCase;
import com.plux.distribution.application.port.in.chat.CreateChatUseCase;
import com.plux.distribution.application.dto.feedback.ButtonContext;
import com.plux.distribution.application.dto.feedback.MessageContext;
import com.plux.distribution.application.port.exception.ChatIdNotFound;
import com.plux.distribution.application.port.in.message.CreateMessageUseCase;
import com.plux.distribution.application.port.out.feedback.CreateFeedbackPort;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.application.dto.feedback.dto.payload.ButtonPayload;
import com.plux.distribution.application.dto.feedback.dto.payload.MessagePayload;
import com.plux.distribution.application.dto.feedback.dto.payload.ReplyPayload;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.message.participant.UnknownServiceParticipant;
import com.plux.distribution.domain.message.participant.ChatParticipant;
import com.plux.distribution.domain.message.state.ReceivedState;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class RegisterFeedbackService implements RegisterFeedbackUseCase {

    private static final Logger log = LogManager.getLogger(RegisterFeedbackService.class);
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

    private void registerAndProcess(@NotNull CreateFeedbackCommand command) {
        var feedback = createFeedbackPort.create(command);

        feedbackProcessor.process(feedback);
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
            log.info("Created new chat with id={}", chatId);
        }

        var content = new SimpleMessageContent(context.getText(), List.of());
        var contentMessage = createMessageUseCase.create(new CreateMessageCommand(
                new ChatParticipant(chatId),
                new UnknownServiceParticipant(),
                new ReceivedState(new Date()),
                content
        ));
        context.onMessageCreated(contentMessage.id());

        var createFeedbackCommand = makeFeedbackCommand(context, contentMessage, receiveTime, chatId);

        registerAndProcess(createFeedbackCommand);
    }

    @Override
    public void handle_button(@NotNull ButtonContext context) {
        var receiveTime = new Date();

        var createFeedbackCommand = makeFeedbackCommand(context, receiveTime);

        registerAndProcess(createFeedbackCommand);
    }

    private static CreateFeedbackCommand makeFeedbackCommand(MessageContext context, MessageDto content,
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
