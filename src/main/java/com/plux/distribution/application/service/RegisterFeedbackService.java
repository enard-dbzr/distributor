package com.plux.distribution.application.service;

import com.plux.distribution.application.port.in.FeedbackProcessor;
import com.plux.distribution.application.port.in.RegisterFeedbackUseCase;
import com.plux.distribution.application.port.in.context.ButtonContext;
import com.plux.distribution.application.port.in.context.MessageContext;
import com.plux.distribution.application.port.exception.ChatIdNotFound;
import com.plux.distribution.application.port.out.feedback.CreateFeedbackPort;
import com.plux.distribution.application.port.out.message.CreateMessagePort;
import com.plux.distribution.application.port.out.chat.CreateChatPort;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.feedback.payload.ButtonPayload;
import com.plux.distribution.domain.feedback.payload.MessagePayload;
import com.plux.distribution.domain.feedback.payload.ReplyPayload;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.message.participant.UnknownServiceParticipant;
import com.plux.distribution.domain.message.participant.ChatParticipant;
import com.plux.distribution.domain.message.state.ReceivedState;
import com.plux.distribution.domain.chat.Chat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class RegisterFeedbackService implements RegisterFeedbackUseCase {

    private final CreateMessagePort createMessagePort;
    private final CreateFeedbackPort createFeedbackPort;
    private final CreateChatPort createChatPort;
    private final FeedbackProcessor feedbackProcessor;

    public RegisterFeedbackService(CreateMessagePort createMessagePort,
            CreateFeedbackPort createFeedbackPort, CreateChatPort createChatPort,
            FeedbackProcessor feedbackProcessor) {
        this.createMessagePort = createMessagePort;
        this.createFeedbackPort = createFeedbackPort;
        this.createChatPort = createChatPort;
        this.feedbackProcessor = feedbackProcessor;
    }

    @Override
    public void handle_message(@NotNull MessageContext context) {
        var receiveTime = new Date();

        ChatId chatId;
        try {
            chatId = context.getChatId();
        } catch (ChatIdNotFound e) {
            chatId = createChatPort.create(new Chat());
            context.onChatCreated(chatId);
            System.out.println("Created new chat");
        }

        var message = makeMessage(context, chatId);
        var messageId = createMessagePort.create(message);
        context.onMessageCreated(messageId);

        var feedback = makeFeedback(context, messageId, receiveTime, chatId);
        createFeedbackPort.create(feedback);

        var feedbackContext = new FeedbackContext(feedback, Optional.of(message));

        feedbackProcessor.process(feedbackContext);

    }

    @Override
    public void handle_button(@NotNull ButtonContext context) {
        var receiveTime = new Date();

        var feedback = makeFeedback(context, receiveTime);
        createFeedbackPort.create(feedback);

        var feedbackContext = new FeedbackContext(feedback, Optional.empty());

        feedbackProcessor.process(feedbackContext);
    }

    private static @NotNull Message makeMessage(MessageContext context, ChatId chatId) {
        return new Message(
                new ChatParticipant(chatId),
                new UnknownServiceParticipant(),
                new ReceivedState(context.getTimestamp()),
                new SimpleMessageContent(context.getText(), List.of())
        );
    }

    private static Feedback makeFeedback(MessageContext context, MessageId content,
            Date receivedAt, ChatId chatId) {
        if (context.getReplyTo() == null) {
            return new Feedback(
                    receivedAt,
                    chatId,
                    new MessagePayload(content)
            );
        }

        return new Feedback(
                receivedAt,
                chatId,
                new ReplyPayload(context.getReplyTo(), content)
        );

    }

    private static Feedback makeFeedback(ButtonContext context, Date receivedAt) {
        try {
            return new Feedback(
                    receivedAt,
                    context.getChatId(),
                    new ButtonPayload(context.getReplyTo(), context.getTag())
            );
        } catch (ChatIdNotFound chatIdNotFound) {
            throw new RuntimeException(chatIdNotFound);
        }
    }
}
