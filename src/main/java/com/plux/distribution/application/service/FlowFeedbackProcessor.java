package com.plux.distribution.application.service;

import com.plux.distribution.application.dto.feedback.dto.Feedback;
import com.plux.distribution.application.port.in.FeedbackProcessor;
import com.plux.distribution.application.port.out.workflow.SaveContextPort;
import com.plux.distribution.application.port.out.workflow.LoadContextPort;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameContextManager;
import com.plux.distribution.application.workflow.core.FrameFactory;
import com.plux.distribution.application.workflow.core.FrameFeedback;
import com.plux.distribution.application.dto.feedback.dto.payload.ButtonPayload;
import com.plux.distribution.application.dto.feedback.dto.payload.FeedbackPayloadVisitor;
import com.plux.distribution.application.dto.feedback.dto.payload.MessagePayload;
import com.plux.distribution.application.dto.feedback.dto.payload.ReplyPayload;
import com.plux.distribution.domain.message.content.MessageContent;
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
    public void process(@NotNull Feedback feedback) {
        var chatId = feedback.chatId();
        var frameContext = contextLoader.load(chatId, contextManager, frameFactory)
                .orElse(new FrameContext(contextManager, frameFactory, chatId));

        AtomicBoolean newTriggered = new AtomicBoolean(false);

        createFrameFeedback(feedback).text().ifPresent(text -> {
            if (text.equals("/start")) {
                startRegistration(frameContext);
                newTriggered.set(true);
            }
        });

        if (!newTriggered.get()) {
            frameContext.handle(createFrameFeedback(feedback));
        }

        contextSaver.save(frameContext);
    }

    private void startRegistration(FrameContext frameContext) {
        frameContext.clear();
        frameContext.push(frameFactory.get("flow.registration"), true);
        frameContext.exec();
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
    }
}
