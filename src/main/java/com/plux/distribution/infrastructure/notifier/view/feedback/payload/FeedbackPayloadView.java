package com.plux.distribution.infrastructure.notifier.view.feedback.payload;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
public sealed interface FeedbackPayloadView permits ButtonFeedbackPayloadView, MessageFeedbackPayloadView,
        ReplyFeedbackPayloadView {

}
