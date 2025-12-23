package com.plux.distribution.core.workflow.application.service;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.feedback.application.port.out.FeedbackProcessor;
import com.plux.distribution.core.feedback.domain.Feedback;
import com.plux.distribution.core.interaction.domain.content.ButtonClickContent;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.registration.HelloFrame;
import com.plux.distribution.core.workflow.application.port.in.CheckChatBusyUseCase;
import com.plux.distribution.core.workflow.application.port.in.WorkflowUseCase;
import com.plux.distribution.core.workflow.domain.frame.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.FrameFeedback;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public class FlowFeedbackProcessor implements FeedbackProcessor, CheckChatBusyUseCase {

    private final FeedbackProcessor next;
    private final WorkflowUseCase workflowUseCase;

    private final Function<FrameContext, Frame> registrationWorkflowFactory;
    private final Function<FrameContext, Frame> scheduleSettingsWorkflowFactory;
    private final Function<FrameContext, Frame> updateUserWorkflowFactory;
    private final Function<FrameContext, Frame> helpWorkflowFactory;

    public FlowFeedbackProcessor(
            FeedbackProcessor next,
            WorkflowUseCase workflowUseCase,
            Function<FrameContext, Frame> registrationWorkflowFactory,
            Function<FrameContext, Frame> scheduleSettingsWorkflowFactory,
            Function<FrameContext, Frame> updateUserWorkflowFactory,
            Function<FrameContext, Frame> helpWorkflowFactory
    ) {
        this.next = next;
        this.workflowUseCase = workflowUseCase;
        this.registrationWorkflowFactory = registrationWorkflowFactory;
        this.scheduleSettingsWorkflowFactory = scheduleSettingsWorkflowFactory;
        this.updateUserWorkflowFactory = updateUserWorkflowFactory;
        this.helpWorkflowFactory = helpWorkflowFactory;
    }

    private static String extractMessageText(@NotNull InteractionContent content) {
        return switch (content) {
            case SimpleMessageContent c -> c.text();
            case ReplyMessageContent c -> extractMessageText(c.original());
            case ButtonClickContent _ -> null;
        };
    }

    @Override
    public void process(@NotNull Feedback feedback) {

        var context = workflowUseCase.load(feedback.chatId());

        AtomicBoolean newTriggered = new AtomicBoolean(false);

        createFrameFeedback(feedback).text().ifPresent(text -> {
            switch (text) {
                case "/start" -> {
                    context.getRoot().changeState(context, registrationWorkflowFactory.apply(context));
                    newTriggered.set(true);
                }
                case "/schedule_settings" -> {
                    context.getRoot().changeState(context, scheduleSettingsWorkflowFactory.apply(context));
                    newTriggered.set(true);
                }
                case "/update_user" -> {
                    context.getRoot().changeState(context, updateUserWorkflowFactory.apply(context));
                    newTriggered.set(true);
                }
                case "/help" -> {
                    context.getRoot().changeState(context, new HelloFrame());
                    newTriggered.set(true);
                }
            }
        });

//        if (!newTriggered.get() && frameContext.isEmpty()) {
//            next.process(feedback);
//            return;
//        }
//
        if (!newTriggered.get()) {
            context.getRoot().handle(context, createFrameFeedback(feedback));
        }

        workflowUseCase.save(context);
    }

    private FrameFeedback createFrameFeedback(Feedback feedback) {
        var content = feedback.payload().content();
        var text = extractMessageText(content);
        var buttonTag = content instanceof ButtonClickContent c ? c.tag() : null;


        return new FrameFeedback(
                feedback,
                content,
                Optional.ofNullable(text),
                Optional.ofNullable(buttonTag)
        );
    }

    // FIXME: Исправить это недоразумение
    @Override
    public boolean isBusy(@NotNull ChatId chatId) {
//        return !workflowUseCase.load(chatId).isEmpty();
        return true;
    }
}
