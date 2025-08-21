package com.plux.distribution.application.service;

import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.message.content.MessageContent;
import java.util.Optional;

public record FeedbackContext(Feedback feedback, Optional<MessageContent> content) {

}
