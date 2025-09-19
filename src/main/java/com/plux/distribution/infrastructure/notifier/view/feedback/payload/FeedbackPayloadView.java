package com.plux.distribution.infrastructure.notifier.view.feedback.payload;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ButtonFeedbackPayloadView.class, name = "button"),
        @JsonSubTypes.Type(value = MessageFeedbackPayloadView.class, name = "message"),
        @JsonSubTypes.Type(value = ReplyFeedbackPayloadView.class, name = "reply"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = ButtonFeedbackPayloadView.class, value = "button"),
                @DiscriminatorMapping(schema = MessageFeedbackPayloadView.class, value = "message"),
                @DiscriminatorMapping(schema = ReplyFeedbackPayloadView.class, value = "reply"),
        }
)
public sealed interface FeedbackPayloadView permits ButtonFeedbackPayloadView, MessageFeedbackPayloadView,
        ReplyFeedbackPayloadView {

}
