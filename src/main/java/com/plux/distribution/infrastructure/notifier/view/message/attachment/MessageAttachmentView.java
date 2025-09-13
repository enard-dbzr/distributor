package com.plux.distribution.infrastructure.notifier.view.message.attachment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.domain.message.attachment.AttachmentVisitor;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import com.plux.distribution.domain.message.attachment.MessageAttachment;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ButtonAttachmentView.class, name = "button"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = ButtonAttachmentView.class, value = "button")
        }
)
public sealed interface MessageAttachmentView permits ButtonAttachmentView {
    static @NotNull MessageAttachmentView create(@NotNull MessageAttachment attachment) {
        return attachment.accept(new AttachmentVisitor<>() {
            @Override
            public MessageAttachmentView visit(ButtonAttachment attachment) {
                return new ButtonAttachmentView(attachment.text(), attachment.tag());
            }
        });
    }
}
