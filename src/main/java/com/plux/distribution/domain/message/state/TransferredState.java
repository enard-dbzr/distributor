package com.plux.distribution.domain.message.state;

import java.util.Date;
import org.jetbrains.annotations.NotNull;

public record TransferredState(@NotNull Date transferTime) implements MessageState {

    @Override
    public void accept(@NotNull MessageStateVisitor visitor) {
        visitor.visit(this);
    }
}
