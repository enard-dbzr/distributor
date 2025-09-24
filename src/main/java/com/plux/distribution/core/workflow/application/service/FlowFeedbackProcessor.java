package com.plux.distribution.core.workflow.application.service;

import com.plux.distribution.core.feedback.application.dto.Feedback;
import com.plux.distribution.core.feedback.application.port.in.FeedbackProcessor;
import com.plux.distribution.core.workflow.application.port.in.WorkflowUseCase;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.feedback.domain.payload.ButtonPayload;
import com.plux.distribution.core.feedback.domain.payload.FeedbackPayloadVisitor;
import com.plux.distribution.core.feedback.domain.payload.MessagePayload;
import com.plux.distribution.core.feedback.domain.payload.ReplyPayload;
import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.content.MessageContentVisitor;
import com.plux.distribution.core.message.domain.content.ReplyMessageContent;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

public class FlowFeedbackProcessor implements FeedbackProcessor {

    private final FeedbackProcessor next;
    private final WorkflowUseCase workflowUseCase;

    private final Frame registrationWorkflow;
    private final Frame scheduleSettingsWorkflow;
    private final Frame updateUserWorkflow;
    private final Frame helpWorkflow;

    public FlowFeedbackProcessor(
            FeedbackProcessor next,
            WorkflowUseCase workflowUseCase,
            Frame registrationWorkflow,
            Frame scheduleSettingsWorkflow,
            Frame updateUserWorkflow,
            Frame helpWorkflow
    ) {
        this.next = next;
        this.workflowUseCase = workflowUseCase;
        this.registrationWorkflow = registrationWorkflow;
        this.scheduleSettingsWorkflow = scheduleSettingsWorkflow;
        this.updateUserWorkflow = updateUserWorkflow;
        this.helpWorkflow = helpWorkflow;
    }

    @Override
    public void process(@NotNull Feedback feedback) {
        var frameContext = workflowUseCase.load(feedback.chatId());

        AtomicBoolean newTriggered = new AtomicBoolean(false);

        createFrameFeedback(feedback).text().ifPresent(text -> {
            switch (text) {
                case "/start" -> {
                    startRegistration(frameContext);
                    newTriggered.set(true);
                }
                case "/schedule_settings" -> {
                    startScheduleSettings(frameContext);
                    newTriggered.set(true);
                }
                case "/update_user" -> {
                    stratUpdateUser(frameContext);
                    newTriggered.set(true);
                }
                case "/help" -> {
                    startHelp(frameContext);
                    newTriggered.set(true);
                }
            }
        });

        if (!newTriggered.get() && frameContext.isEmpty()) {
            next.process(feedback);
            return;
        }

        if (!newTriggered.get()) {
            frameContext.handle(createFrameFeedback(feedback));
        }

        workflowUseCase.save(frameContext);
    }

    private void startRegistration(FrameContext frameContext) {
        frameContext.clear();
        frameContext.push(registrationWorkflow, true);
        frameContext.exec();
    }

    private void startScheduleSettings(FrameContext frameContext) {
        if (frameContext.isEmpty()) {
            frameContext.push(scheduleSettingsWorkflow, true);
            frameContext.exec();
        }
    }

    private void stratUpdateUser(FrameContext frameContext) {
        if (frameContext.isEmpty()) {
            frameContext.push(updateUserWorkflow, true);
            frameContext.exec();
        }
    }

    private void startHelp(FrameContext frameContext) {
        if (frameContext.isEmpty()) {
            frameContext.push(helpWorkflow, true);
            frameContext.exec();
        }
    }

    private FrameFeedback createFrameFeedback(Feedback feedback) {
        var text = new AtomicReference<String>();
        var buttonTag = new AtomicReference<String>();
        var content = new AtomicReference<MessageContent>();

        feedback.payload().accept(new FeedbackPayloadVisitor<Void>() {
            @Override
            public Void visit(@NotNull ButtonPayload entity) {
                buttonTag.set(entity.tag());
                return null;
            }

            @Override
            public Void visit(@NotNull MessagePayload entity) {
                content.set(entity.content().content());
                text.set(entity.content().content().accept(new ExtractMessageText()));
                return null;
            }

            @Override
            public Void visit(@NotNull ReplyPayload entity) {
                content.set(entity.content().content());
                text.set(entity.content().content().accept(new ExtractMessageText()));
                return null;
            }
        });

        return new FrameFeedback(
                feedback,
                Optional.ofNullable(content.get()),
                Optional.ofNullable(text.get()),
                Optional.ofNullable(buttonTag.get())
        );
    }

    private static class ExtractMessageText implements MessageContentVisitor<String> {

        @Override
        public String visit(SimpleMessageContent content) {
            return content.text().isEmpty() ? null : content.text();
        }

        @Override
        public String visit(ReplyMessageContent content) {
            return content.original().accept(this);
        }
    }
}
