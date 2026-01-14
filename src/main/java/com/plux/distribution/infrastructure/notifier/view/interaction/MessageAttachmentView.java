package com.plux.distribution.infrastructure.notifier.view.interaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment.DisplayType;
import com.plux.distribution.infrastructure.notifier.view.interaction.MessageAttachmentView.MediaAttachmentView.DisplayTypeView;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MessageAttachmentView.ButtonAttachmentView.class, name = "button"),
        @JsonSubTypes.Type(value = MessageAttachmentView.MediaAttachmentView.class, name = "media"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = MessageAttachmentView.ButtonAttachmentView.class, value = "button"),
                @DiscriminatorMapping(schema = MessageAttachmentView.MediaAttachmentView.class, value = "media")
        }
)
public sealed interface MessageAttachmentView {

    static @NotNull MessageAttachmentView create(@NotNull MessageAttachment attachment) {
        return switch (attachment) {
            case ButtonAttachment a -> new ButtonAttachmentView(a.text(), a.tag());
            case MediaAttachment a ->
                    new MediaAttachmentView(a.mediaId().value(), DisplayTypeView.from(a.displayType()));
        };
    }

    record ButtonAttachmentView(@NotNull String text, @NotNull String tag) implements MessageAttachmentView {}

    record MediaAttachmentView(@NotNull UUID mediaId, @NotNull DisplayTypeView displayType) implements
            MessageAttachmentView {

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        enum DisplayTypeView {
            @JsonProperty("photo") PHOTO,
            @JsonProperty("document") DOCUMENT;

            public static DisplayTypeView from(@NotNull DisplayType domainType) {
                return switch (domainType) {
                    case PHOTO -> PHOTO;
                    case DOCUMENT -> DOCUMENT;
                };
            }
        }
    }
}
