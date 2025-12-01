package com.plux.distribution.core.feedback.application.service;

import com.plux.distribution.core.chat.application.port.in.CreateChatUseCase;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.feedback.application.command.CreateFeedbackCommand;
import com.plux.distribution.core.feedback.application.exception.ChatIdNotFound;
import com.plux.distribution.core.feedback.application.port.in.FeedbackProcessor;
import com.plux.distribution.core.feedback.application.port.in.register.ButtonContext;
import com.plux.distribution.core.feedback.application.port.in.register.MessageContext;
import com.plux.distribution.core.feedback.application.port.in.register.RegisterFeedbackUseCase;
import com.plux.distribution.core.feedback.application.port.out.CreateFeedbackPort;
import com.plux.distribution.core.feedback.domain.payload.ButtonPayload;
import com.plux.distribution.core.feedback.domain.payload.MessagePayload;
import com.plux.distribution.core.feedback.domain.payload.ReplyPayload;
import com.plux.distribution.core.interaction.application.command.CreateInteractionCommand;
import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import com.plux.distribution.core.interaction.application.port.in.CreateInteractionUseCase;
import com.plux.distribution.core.interaction.domain.InteractionState.Transferred;
import com.plux.distribution.core.interaction.domain.Participant.BotParticipant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class RegisterFeedbackService implements RegisterFeedbackUseCase {

    private static final Logger log = LogManager.getLogger(RegisterFeedbackService.class);
    private final CreateInteractionUseCase createInteractionUseCase;
    private final CreateFeedbackPort createFeedbackPort;
    private final CreateChatUseCase createChatUseCase;
    private final FeedbackProcessor feedbackProcessor;

    public RegisterFeedbackService(CreateInteractionUseCase createInteractionUseCase,
            CreateFeedbackPort createFeedbackPort, CreateChatUseCase createChatUseCase,
            FeedbackProcessor feedbackProcessor) {
        this.createInteractionUseCase = createInteractionUseCase;
        this.createFeedbackPort = createFeedbackPort;
        this.createChatUseCase = createChatUseCase;
        this.feedbackProcessor = feedbackProcessor;
    }

    private static CreateFeedbackCommand makeFeedbackCommand(MessageContext context, InteractionDto content,
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
        var contentMessage = createInteractionUseCase.create(new CreateInteractionCommand(
                new ChatParticipant(chatId),
                new BotParticipant(),
                new Transferred(new Date()),
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
}
