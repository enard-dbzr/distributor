package com.plux.distribution.infrastructure.notifier.view.message.state;

import java.util.Date;
import org.jetbrains.annotations.NotNull;

public record ReceivedStateView(@NotNull Date receiveTime) implements MessageStateView {

}
