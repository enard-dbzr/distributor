package com.plux.distribution.infrastructure.notifier.view.message.content;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.content.MessageContentVisitor;
import com.plux.distribution.core.message.domain.content.ReplyMessageContent;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
import com.plux.distribution.infrastructure.notifier.view.message.attachment.MessageAttachmentView;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleMessageContentView.class, name = "simple"),
        @JsonSubTypes.Type(value = ReplyContentView.class, name = "reply"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = SimpleMessageContentView.class, value = "simple"),
                @DiscriminatorMapping(schema = ReplyContentView.class, value = "reply"),
        }
)
public sealed interface MessageContentView permits ReplyContentView, SimpleMessageContentView {
    static @NotNull MessageContentView create(@NotNull MessageContent content) {
        return content.accept(new MessageContentVisitor<>() {
            @Override
            public MessageContentView visit(SimpleMessageContent content) {
                return new SimpleMessageContentView(
                        content.text(),
                        content.attachments().stream().map(MessageAttachmentView::create).toList()
                );
            }

            @Override
            public @NotNull MessageContentView visit(ReplyMessageContent content) {
                return new ReplyContentView(
                        content.original().accept(this),
                        content.replyTo().value()
                );
            }
        });
    }
}
