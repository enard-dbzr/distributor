package com.plux.distribution.application.service;

import com.plux.distribution.application.port.in.FeedbackProcessor;
import com.plux.distribution.application.port.out.workflow.ContextLoaderPort;
import com.plux.distribution.application.workflow.core.FrameFeedback;
import com.plux.distribution.domain.feedback.payload.ButtonPayload;
import com.plux.distribution.domain.feedback.payload.FeedbackPayloadVisitor;
import com.plux.distribution.domain.feedback.payload.MessagePayload;
import com.plux.distribution.domain.feedback.payload.ReplyPayload;
import com.plux.distribution.domain.message.content.MessageContentVisitor;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

public class FlowFeedbackProcessor implements FeedbackProcessor {

    private final ContextLoaderPort contextLoader;

    public FlowFeedbackProcessor(ContextLoaderPort contextLoader) {
        this.contextLoader = contextLoader;
    }

    @Override
    public void process(@NotNull FeedbackContext context) {
        var messageId = context.feedback().payload().getReplyTo();

        System.out.printf("Got feedback: %s%n", context);

        if (messageId == null) {
            var frameContext = contextLoader.init(context.feedback().chatId());
            frameContext.push(frameContext.getFactory().get("flow.registration"), true);
            frameContext.exec();
        } else {
            var frameContext = contextLoader.load(context.feedback().chatId(), messageId);
            frameContext.handle(createFrameFeedback(context));
        }
    }

    public FrameFeedback createFrameFeedback(FeedbackContext context) {
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
            message.getContent().accept(new MessageContentVisitor() {

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
