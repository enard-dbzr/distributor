package com.plux.distribution.infrastructure.api.message;

import com.plux.distribution.core.integration.application.command.SendServiceInteractionCommand;
import com.plux.distribution.core.integration.application.exception.InvalidToken;
import com.plux.distribution.core.integration.application.port.in.SendServiceInteractionUseCase;
import com.plux.distribution.infrastructure.api.message.request.SendMessageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/messages"})
@Tag(name = "Interaction")
public class MessageController {

    private final SendServiceInteractionUseCase sendServiceMessageUseCase;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MessageController(SendServiceInteractionUseCase sendServiceMessageUseCase) {
        this.sendServiceMessageUseCase = sendServiceMessageUseCase;
    }

    @PostMapping

    @Operation(
            summary = "Send message",
            description = "Send message to specified chat",
            security = {@SecurityRequirement(name = "serviceToken")},
            responses = {
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "201", description = "Interaction was send")
            }
    )
    public ResponseEntity<SendMessageResult> sendMessage(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SendMessageRequest request
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring("Bearer ".length()).trim();

        try {
            var messageId = sendServiceMessageUseCase.send(
                    new SendServiceInteractionCommand(token, request.chatId(), request.content().toModel()));

            return ResponseEntity.status(HttpStatus.CREATED).body(new SendMessageResult(messageId.value()));
        } catch (InvalidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
