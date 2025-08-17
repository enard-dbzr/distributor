package com.plux.distribution.domain.workflow;

import com.plux.distribution.domain.feedback.Feedback;
import org.jetbrains.annotations.NotNull;

public interface Frame {

    @NotNull String getKey();

    void exec(@NotNull FrameContext context);

    void handle(@NotNull FrameContext context, @NotNull Feedback feedback);

}
