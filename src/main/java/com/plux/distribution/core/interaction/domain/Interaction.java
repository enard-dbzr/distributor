package com.plux.distribution.core.interaction.domain;

import com.plux.distribution.core.interaction.domain.InteractionState.Transferred;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class Interaction {

    private final @NotNull InteractionId id;

    private final @NotNull Participant sender;

    private final @NotNull Participant recipient;

    private final @NotNull InteractionContent content;

    private @NotNull InteractionState state;

    public Interaction(@NotNull InteractionId id, @NotNull Participant sender, @NotNull Participant recipient,
            @NotNull InteractionState state,
            @NotNull InteractionContent content) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.state = state;
        this.content = content;
    }

    public void markTransferred(@NotNull Date time) {
        this.state = new Transferred(time);
    }

    @NotNull
    public Participant getSender() {
        return sender;
    }

    @NotNull
    public Participant getRecipient() {
        return recipient;
    }

    @NotNull
    public InteractionState getState() {
        return state;
    }

    @NotNull
    public InteractionContent getContent() {
        return content;
    }

    public @NotNull InteractionId getId() {
        return id;
    }
}
