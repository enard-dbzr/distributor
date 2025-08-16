package com.plux.distribution.application.service;

import com.plux.distribution.application.port.in.FeedbackProcessor;
import com.plux.distribution.application.port.in.RegisterFeedbackUseCase;
import com.plux.distribution.application.port.in.context.ButtonContext;
import com.plux.distribution.application.port.in.context.MessageContext;
import com.plux.distribution.application.port.out.feedback.CreateFeedbackPort;
import com.plux.distribution.application.port.out.message.CreateMessagePort;
import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.feedback.payload.ButtonPayload;
import com.plux.distribution.domain.feedback.payload.MessagePayload;
import com.plux.distribution.domain.feedback.payload.ReplyPayload;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.message.participant.UnknownServiceParticipant;
import com.plux.distribution.domain.message.participant.UserParticipant;
import com.plux.distribution.domain.message.state.ReceivedState;
import java.util.Date;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class RegisterFeedbackService implements RegisterFeedbackUseCase {

    private final CreateMessagePort createMessagePort;
    private final CreateFeedbackPort createFeedbackPort;
    private final FeedbackProcessor feedbackProcessor;

    public RegisterFeedbackService(CreateMessagePort createMessagePort,
            CreateFeedbackPort createFeedbackPort, FeedbackProcessor feedbackProcessor) {
        this.createMessagePort = createMessagePort;
        this.createFeedbackPort = createFeedbackPort;
        this.feedbackProcessor = feedbackProcessor;
    }

    @Override
    public void handle_message(@NotNull MessageContext context) {
        var receiveTime = new Date();

        var message = makeMessage(context);
        var messageId = createMessagePort.create(message);
        context.onMessageCreated(messageId);

        var feedback = makeFeedback(context, messageId, receiveTime);
        createFeedbackPort.create(feedback);

        feedbackProcessor.process(feedback);
    }

    @Override
    public void handle_button(@NotNull ButtonContext context) {
        var receiveTime = new Date();

        var feedback = makeFeedback(context, receiveTime);
        createFeedbackPort.create(feedback);

        feedbackProcessor.process(feedback);
    }

    private static @NotNull Message makeMessage(MessageContext context) {
        return new Message(
                new UserParticipant(context.getUserId()),
                new UnknownServiceParticipant(),
                new ReceivedState(context.getTimestamp()),
                new SimpleMessageContent(context.getText(), List.of())
        );
    }

    private static Feedback makeFeedback(MessageContext context, MessageId content, Date receivedAt) {
        if (context.getReplyTo() == null) {
            return new Feedback(
                    receivedAt,
                    context.getUserId(),
                    new MessagePayload(content)
            );
        }

        return new Feedback(
                receivedAt,
                context.getUserId(),
                new ReplyPayload(context.getReplyTo(), content)
        );
    }

    private static Feedback makeFeedback(ButtonContext context, Date receivedAt) {
        return new Feedback(
                receivedAt,
                context.getUserId(),
                new ButtonPayload(context.getReplyTo(), context.getTag())
        );
    }
}
