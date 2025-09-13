package com.plux.distribution.infrastructure.notifier.view.message.content;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.content.MessageContentVisitor;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
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
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = SimpleMessageContentView.class, value = "simple")
        }
)
public sealed interface MessageContentView permits SimpleMessageContentView {
    static @NotNull MessageContentView create(@NotNull MessageContent content) {
        return content.accept(new MessageContentVisitor<>() {
            @Override
            public MessageContentView visit(SimpleMessageContent content) {
                return new SimpleMessageContentView(
                        content.text(),
                        content.attachments().stream().map(MessageAttachmentView::create).toList()
                );
            }
        });
    }
}
