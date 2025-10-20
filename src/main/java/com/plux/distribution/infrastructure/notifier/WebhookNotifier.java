package com.plux.distribution.infrastructure.notifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.core.feedback.application.dto.ResolvedFeedback;
import com.plux.distribution.core.session.application.dto.SessionDto;
import com.plux.distribution.core.integration.application.port.in.GetWebhookUseCase;
import com.plux.distribution.core.integration.application.port.in.NotifyServiceFeedbackPort;
import com.plux.distribution.core.session.application.port.out.NotifySessionEventPort;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.infrastructure.notifier.view.EventType;
import com.plux.distribution.infrastructure.notifier.view.EventView;
import com.plux.distribution.infrastructure.notifier.view.feedback.ResolvedFeedbackView;
import com.plux.distribution.infrastructure.notifier.view.session.SessionEvent;
import com.plux.distribution.infrastructure.notifier.view.session.SessionEventType;
import com.plux.distribution.infrastructure.notifier.view.session.SessionView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Webhook;
import io.swagger.v3.oas.annotations.Webhooks;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Webhooks({
        @Webhook(
                name = "events",
                operation = @Operation(
                        summary = "Service(integration) events webhook",
                        requestBody = @RequestBody(
                                required = true,
                                content = @Content(schema = @Schema(implementation = EventView.class))
                        ),
                        responses = {
                                @ApiResponse(responseCode = "200")
                        }
                )
        )
})
@Component
public class WebhookNotifier implements NotifySessionEventPort, NotifyServiceFeedbackPort {

    private static final Logger log = LogManager.getLogger(WebhookNotifier.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    private final GetWebhookUseCase getWebhookUseCase;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
    public void notifyClosed(@NotNull SessionDto session) {
        var sessionView = new SessionView(session);

        sendEvent(session.serviceId(),
                new EventView(EventType.SESSION, new SessionEvent(SessionEventType.STOPED, sessionView)));
    }

    @Override
    public void notifyGotFeedback(@NotNull ServiceId serviceId, @NotNull ResolvedFeedback resolvedFeedback) {
        var feedbackView = new ResolvedFeedbackView(resolvedFeedback);

        sendEvent(serviceId, new EventView(EventType.FEEDBACK, feedbackView));
    }
}
