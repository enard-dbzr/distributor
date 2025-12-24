package com.plux.distribution.infrastructure.persistence.entity.interaction.content;

import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.content.ButtonClickContent;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BUTTON_CLICK")
public class ButtonClickContentEntity extends InteractionContentEntity {

    private String tag;

    private Long sourceInteractionId;

    public ButtonClickContentEntity(String tag, Long sourceInteractionId) {
        this.tag = tag;
        this.sourceInteractionId = sourceInteractionId;
    }

    public ButtonClickContentEntity() {

    }

    @Override
    public InteractionContent toModel() {
        return new ButtonClickContent(new InteractionId(sourceInteractionId), tag);
    }
}
