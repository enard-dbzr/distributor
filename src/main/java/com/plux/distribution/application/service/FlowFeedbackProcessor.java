package com.plux.distribution.application.service;

import com.plux.distribution.application.port.in.FeedbackProcessor;
import com.plux.distribution.application.port.out.workflow.SaveContextPort;
import com.plux.distribution.application.port.out.workflow.LoadContextPort;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameContextManager;
import com.plux.distribution.application.workflow.core.FrameFactory;
import com.plux.distribution.application.workflow.core.FrameFeedback;
import com.plux.distribution.domain.feedback.payload.ButtonPayload;
import com.plux.distribution.domain.feedback.payload.FeedbackPayloadVisitor;
import com.plux.distribution.domain.feedback.payload.MessagePayload;
import com.plux.distribution.domain.feedback.payload.ReplyPayload;
import com.plux.distribution.domain.message.content.MessageContentVisitor;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

public class FlowFeedbackProcessor implements FeedbackProcessor {

    private final LoadContextPort contextLoader;
    private final SaveContextPort contextSaver;
    private final FrameContextManager contextManager;
    private final FrameFactory frameFactory;

    public FlowFeedbackProcessor(LoadContextPort contextLoader, SaveContextPort contextSaver,
            FrameContextManager contextManager, FrameFactory frameFactory) {
        this.contextLoader = contextLoader;
        this.contextSaver = contextSaver;
        this.contextManager = contextManager;
        this.frameFactory = frameFactory;
    }

    @Override
    public void process(@NotNull FeedbackContext context) {
        var chatId = context.feedback().chatId();
        var frameContext = contextLoader.load(chatId, contextManager, frameFactory)
                .orElse(new FrameContext(contextManager, frameFactory, chatId));

        AtomicBoolean newTriggered = new AtomicBoolean(false);

        createFrameFeedback(context).text().ifPresent(text -> {
            if (text.equals("/start")) {
                startRegistration(frameContext);
                newTriggered.set(true);
            }
        });

        if (!newTriggered.get()) {
            frameContext.handle(createFrameFeedback(context));
        }

        contextSaver.save(frameContext);
    }

    private void startRegistration(FrameContext frameContext) {
        frameContext.clear();
        frameContext.push(frameFactory.get("flow.registration"), true);
        frameContext.exec();
    }

    private FrameFeedback createFrameFeedback(FeedbackContext context) {
        var text = new AtomicReference<String>();
        var buttonTag = new AtomicReference<String>();

        context.feedback().payload().accept(new FeedbackPayloadVisitor() {
            @Override
            public void visit(@NotNull ButtonPayload entity) {
                buttonTag.set(entity.tag());
            }

            @Override
            public void visit(@NotNull MessagePayload entity) {

            }

            @Override
            public void visit(@NotNull ReplyPayload entity) {

            }
        });

        context.content().ifPresent(message -> {
            message.accept(new MessageContentVisitor() {

                @Override
                public void visit(SimpleMessageContent content) {
                    text.set(content.text());
                }
            });
        });

        return new FrameFeedback(context.feedback(), context.content(),
                Optional.ofNullable(text.get()), Optional.ofNullable(buttonTag.get()));
    }
}
