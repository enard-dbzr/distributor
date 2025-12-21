package com.plux.distribution.core.workflow.application.service;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.feedback.application.port.out.FeedbackProcessor;
import com.plux.distribution.core.feedback.domain.Feedback;
import com.plux.distribution.core.interaction.domain.content.ButtonClickContent;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.registration.hello.HelloFrame;
import com.plux.distribution.core.workflow.application.frame.utils.SequenceFrame;
import com.plux.distribution.core.workflow.application.port.in.CheckChatBusyUseCase;
import com.plux.distribution.core.workflow.application.port.in.WorkflowUseCase;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public class FlowFeedbackProcessor implements FeedbackProcessor, CheckChatBusyUseCase {

    private final FeedbackProcessor next;
    private final WorkflowUseCase workflowUseCase;

    private final Function<FrameContext, Frame> registrationWorkflowMaker;
    private final Frame scheduleSettingsWorkflow;
    private final Frame updateUserWorkflow;
    private final Frame helpWorkflow;

    public FlowFeedbackProcessor(
            FeedbackProcessor next,
            WorkflowUseCase workflowUseCase,
            Function<FrameContext, Frame> registrationWorkflowMaker,
            Frame scheduleSettingsWorkflow,
            Frame updateUserWorkflow,
            Frame helpWorkflow
    ) {
        this.next = next;
        this.workflowUseCase = workflowUseCase;
        this.registrationWorkflowMaker = registrationWorkflowMaker;
        this.scheduleSettingsWorkflow = scheduleSettingsWorkflow;
        this.updateUserWorkflow = updateUserWorkflow;
        this.helpWorkflow = helpWorkflow;
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
                    var frame = registrationWorkflowMaker.apply(context);
                    context.getRoot().changeState(frame);
                    newTriggered.set(true);
                }
//                case "/schedule_settings" -> {
//                    startScheduleSettings(frameContext);
//                    newTriggered.set(true);
//                }
//                case "/update_user" -> {
//                    stratUpdateUser(frameContext);
//                    newTriggered.set(true);
//                }
                case "/help" -> {
                    context.getRoot().changeState(new HelloFrame(context, context.getRoot()));
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
            context.getRoot().handle(createFrameFeedback(feedback));
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
