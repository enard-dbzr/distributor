package com.plux.distribution.infrastructure.notifier.view.interaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.plux.distribution.core.interaction.domain.content.ButtonClickContent;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.infrastructure.notifier.view.interaction.InteractionContentView.ButtonClickContentView;
import com.plux.distribution.infrastructure.notifier.view.interaction.InteractionContentView.ReplyContentView;
import com.plux.distribution.infrastructure.notifier.view.interaction.InteractionContentView.SimpleMessageContentView;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@JsonTypeInfo(
        use = Id.NAME,
        include = As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @Type(value = SimpleMessageContentView.class, name = "simple"),
        @Type(value = ReplyContentView.class, name = "reply"),
        @Type(value = ButtonClickContentView.class, name = "button_click"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = SimpleMessageContentView.class, value = "simple"),
                @DiscriminatorMapping(schema = ReplyContentView.class, value = "reply"),
                @DiscriminatorMapping(schema = ButtonClickContentView.class, value = "button_click"),
        }
)
public sealed interface InteractionContentView {

    static @NotNull InteractionContentView create(@NotNull InteractionContent content) {
        return switch (content) {
            case SimpleMessageContent c -> new SimpleMessageContentView(
                    c.text(),
                    c.attachments().stream().map(MessageAttachmentView::create).toList()
            );
            case ReplyMessageContent c -> new ReplyContentView(create(c.original()), c.replyTo().value());
            case ButtonClickContent c -> new ButtonClickContentView(c.source().value(), c.tag());
        };
    }

    record ReplyContentView(
            @NotNull InteractionContentView original,
            @NotNull Long replyToMessageId
    ) implements InteractionContentView {}

    record SimpleMessageContentView(
            @NotNull String text,
            @NotNull List<MessageAttachmentView> attachments
    ) implements InteractionContentView {}

    record ButtonClickContentView(
            @NotNull Long sourceInteractionId,
            @NotNull String tag
    ) implements InteractionContentView {}
}
