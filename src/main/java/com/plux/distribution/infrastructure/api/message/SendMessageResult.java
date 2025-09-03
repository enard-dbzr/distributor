package com.plux.distribution.infrastructure.api.message;


import org.jetbrains.annotations.NotNull;

public record SendMessageResult(
        @NotNull Long messageId
) {

}
