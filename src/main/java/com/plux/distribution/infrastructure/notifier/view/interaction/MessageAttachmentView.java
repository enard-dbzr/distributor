package com.plux.distribution.infrastructure.notifier.view.interaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MessageAttachmentView.ButtonAttachmentView.class, name = "button"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = MessageAttachmentView.ButtonAttachmentView.class, value = "button")
        }
)
public sealed interface MessageAttachmentView permits MessageAttachmentView.ButtonAttachmentView {

    static @NotNull MessageAttachmentView create(@NotNull MessageAttachment attachment) {
        return switch (attachment) {
            case ButtonAttachment a -> new ButtonAttachmentView(a.text(), a.tag());
        };
    }

    record ButtonAttachmentView(@NotNull String text, @NotNull String tag) implements MessageAttachmentView {

    }

}
