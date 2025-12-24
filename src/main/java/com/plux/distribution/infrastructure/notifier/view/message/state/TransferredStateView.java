package com.plux.distribution.infrastructure.notifier.view.message.state;

import java.util.Date;
import org.jetbrains.annotations.NotNull;

public record TransferredStateView(@NotNull Date transferTime) implements MessageStateView {

}
