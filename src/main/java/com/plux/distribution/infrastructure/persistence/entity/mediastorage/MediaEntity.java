package com.plux.distribution.infrastructure.persistence.entity.mediastorage;

import com.plux.distribution.core.mediastorage.domain.Media;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import com.plux.distribution.core.storage.domain.StorageKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "media_items")
public class MediaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String storageKeyPath;

    public MediaEntity(@NotNull Media model) {
        this.id = model.id().value();
        this.storageKeyPath = model.storageKey().path();
    }

    public MediaEntity() {

    }

    public @NotNull Media toModel() {
        return new Media(
                new MediaId(id),
                new StorageKey(storageKeyPath)
        );
    }
}
