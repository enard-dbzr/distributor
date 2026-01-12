package com.plux.distribution.core.mediastorage.application.port.out;

import com.plux.distribution.core.mediastorage.application.exception.DuplicateMediaIdException;
import com.plux.distribution.core.mediastorage.domain.Media;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface MediaRepositoryPort {

    void save(@NotNull Media media) throws DuplicateMediaIdException;

    @NotNull Optional<Media> findById(@NotNull MediaId id);

    void delete(@NotNull MediaId id);
}
