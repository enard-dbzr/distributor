package com.plux.distribution.infrastructure.notifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.application.dto.feedback.dto.ResolvedFeedback;
import com.plux.distribution.application.dto.session.SessionDto;
import com.plux.distribution.application.port.in.integration.GetWebhookUseCase;
import com.plux.distribution.application.port.in.integration.NotifyServiceFeedbackPort;
import com.plux.distribution.application.port.out.session.NotifySessionEventPort;
import com.plux.distribution.domain.service.ServiceId;
import com.plux.distribution.infrastructure.notifier.view.EventType;
import com.plux.distribution.infrastructure.notifier.view.EventView;
import com.plux.distribution.infrastructure.notifier.view.feedback.ResolvedFeedbackView;
import com.plux.distribution.infrastructure.notifier.view.session.SessionEvent;
import com.plux.distribution.infrastructure.notifier.view.session.SessionEventType;
import com.plux.distribution.infrastructure.notifier.view.session.SessionView;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class WebhookNotifier implements NotifySessionEventPort, NotifyServiceFeedbackPort {

    private static final Logger log = LogManager.getLogger(WebhookNotifier.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    private final GetWebhookUseCase getWebhookUseCase;

    public WebhookNotifier(GetWebhookUseCase getWebhookUseCase) {
        this.getWebhookUseCase = getWebhookUseCase;
    }

    private void sendEvent(@NotNull ServiceId serviceId, EventView event) {
        var webhook = getWebhookUseCase.getWebhook(serviceId).url();

        try {
            var json = mapper.writeValueAsString(event);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhook))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                log.info("Event [{}] sent to {} (status: {})", event.eventType(), serviceId, response.statusCode());
            } else {
                log.warn("Event [{}] failed for {} (status: {}, body: {})",
                        event.eventType(), serviceId, response.statusCode(), response.body());
            }

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event [{}]", event.eventType(), e);
        } catch (IOException | InterruptedException e) {
            log.error("Error while sending event [{}] to {}({})", event.eventType(), serviceId, webhook, e);
        }
    }


    @Override
    public void notifyCreated(@NotNull SessionDto session) {
        var sessionView = new SessionView(session);

        sendEvent(session.serviceId(),
                new EventView(EventType.SESSION, new SessionEvent(SessionEventType.CREATED, sessionView)));
    }

    @Override
    public void notifyStarted(@NotNull SessionDto session) {
        var sessionView = new SessionView(session);

        sendEvent(session.serviceId(),
                new EventView(EventType.SESSION, new SessionEvent(SessionEventType.STARTED, sessionView)));
    }

    @Override
    public void notifyGotFeedback(@NotNull ServiceId serviceId, @NotNull ResolvedFeedback resolvedFeedback) {
        var feedbackView = new ResolvedFeedbackView(resolvedFeedback);

        sendEvent(serviceId, new EventView(EventType.FEEDBACK, feedbackView));
    }
}
