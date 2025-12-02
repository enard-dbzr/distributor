package com.plux.distribution.core.interaction.domain.content;

import com.plux.distribution.core.interaction.domain.InteractionId;

public record ButtonClickContent(InteractionId source, String tag) implements InteractionContent {}
