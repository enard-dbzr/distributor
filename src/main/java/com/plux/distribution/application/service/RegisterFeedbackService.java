package com.plux.distribution.application.service;

import com.plux.distribution.application.port.in.FeedbackProcessor;
import com.plux.distribution.application.port.in.RegisterFeedbackUseCase;
import com.plux.distribution.application.port.in.dto.ButtonData;
import com.plux.distribution.application.port.in.dto.MessageData;
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
    public @NotNull MessageId handle_message(@NotNull MessageData data) {
        var receiveTime = new Date();

        var message = makeMessage(data);
        var messageId = createMessagePort.create(message);

        var feedback = makeFeedback(data, messageId, receiveTime);
        createFeedbackPort.create(feedback);

        feedbackProcessor.process(feedback);

        return messageId;
    }

    @Override
    public void handle_button(@NotNull ButtonData data) {
        var receiveTime = new Date();

        var feedback = makeFeedback(data, receiveTime);
        createFeedbackPort.create(feedback);

        feedbackProcessor.process(feedback);
    }

    private static @NotNull Message makeMessage(MessageData data) {
        return new Message(
                new UserParticipant(data.userId()),
                new UnknownServiceParticipant(),
                new ReceivedState(data.timestamp()),
                new SimpleMessageContent(data.text(), List.of())
        );
    }

    private static Feedback makeFeedback(MessageData data, MessageId content, Date receivedAt) {
        if (data.replyTo() == null) {
            return new Feedback(
                    receivedAt,
                    data.userId(),
                    new MessagePayload(content)
            );
        }

        return new Feedback(
                receivedAt,
                data.userId(),
                new ReplyPayload(data.replyTo(), content)
        );
    }

    private static Feedback makeFeedback(ButtonData data, Date receivedAt) {
        return new Feedback(
                receivedAt,
                data.userId(),
                new ButtonPayload(data.replyTo(), data.tag())
        );
    }
}
