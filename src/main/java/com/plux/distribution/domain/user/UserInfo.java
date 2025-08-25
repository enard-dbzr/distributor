package com.plux.distribution.domain.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record UserInfo(
        @NotNull String name,
        @Nullable String email,
        @Nullable Integer age,
        @Nullable String city,
        @Nullable String hobby
) {

}
