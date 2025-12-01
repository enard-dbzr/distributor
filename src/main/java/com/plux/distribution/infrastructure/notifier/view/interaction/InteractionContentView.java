package com.plux.distribution.infrastructure.notifier.view.interaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = InteractionContentView.SimpleMessageContentView.class, name = "simple"),
        @JsonSubTypes.Type(value = InteractionContentView.ReplyContentView.class, name = "reply"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = InteractionContentView.SimpleMessageContentView.class, value = "simple"),
                @DiscriminatorMapping(schema = InteractionContentView.ReplyContentView.class, value = "reply"),
        }
)
public sealed interface InteractionContentView permits InteractionContentView.ReplyContentView,
        InteractionContentView.SimpleMessageContentView {

    static @NotNull InteractionContentView create(@NotNull InteractionContent content) {
        return switch (content) {
            case SimpleMessageContent c -> new SimpleMessageContentView(
                    c.text(),
                    c.attachments().stream().map(MessageAttachmentView::create).toList()
            );
            case ReplyMessageContent c -> new ReplyContentView(create(c.original()), c.replyTo().value());
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
}
