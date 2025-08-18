package com.plux.distribution.application.service;

import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.message.Message;
import java.util.Optional;

public record FeedbackContext(Feedback feedback, Optional<Message> content) {

}
