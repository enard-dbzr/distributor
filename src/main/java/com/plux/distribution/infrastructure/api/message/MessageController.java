package com.plux.distribution.infrastructure.api.message;

import com.plux.distribution.application.dto.integration.SendServiceMessageCommand;
import com.plux.distribution.application.port.exception.InvalidToken;
import com.plux.distribution.application.port.in.integration.SendServiceMessageUseCase;
import io.javalin.Javalin;

import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class MessageController {

    private static final Logger log = LogManager.getLogger(MessageController.class);

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private final SendServiceMessageUseCase sendServiceMessageUseCase;

    public MessageController(SendServiceMessageUseCase sendServiceMessageUseCase) {
        this.sendServiceMessageUseCase = sendServiceMessageUseCase;
    }

    public void register(Javalin app) {
        app.post("messages/", this::post);
    }


    private void post(@NotNull Context ctx) {
        String authHeader = ctx.header("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedResponse();
        }
        String token = authHeader.substring("Bearer ".length()).trim();

        var request = ctx.bodyAsClass(SendMessageRequest.class);

        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            ctx.status(400).json(violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .toList());
            return;
        }

        log.info("Got sending message request: {}", request);

        try {
            var messageId = sendServiceMessageUseCase.send(new SendServiceMessageCommand(
                    token,
                    request.chatId(),
                    request.content().toModel()
            ));

            ctx.status(201).json(new SendMessageResult(messageId.value()));
        } catch (InvalidToken e) {
            throw new UnauthorizedResponse("Invalid token");
        }
    }
}
