package com.plux.distribution.core.mediastorage.application.service;

import com.plux.distribution.core.mediastorage.application.exception.DuplicateMediaIdException;
import com.plux.distribution.core.mediastorage.application.exception.MediaNotFoundException;
import com.plux.distribution.core.mediastorage.application.port.in.CrudMediaUseCase;
import com.plux.distribution.core.mediastorage.application.port.out.MediaRepositoryPort;
import com.plux.distribution.core.mediastorage.domain.Media;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import com.plux.distribution.core.storage.application.dto.StoredFile;
import com.plux.distribution.core.storage.application.dto.StoredFileUrl;
import com.plux.distribution.core.storage.application.port.in.DeleteFileUseCase;
import com.plux.distribution.core.storage.application.port.in.GenerateFileUrlUseCase;
import com.plux.distribution.core.storage.application.port.in.LoadFileUseCase;
import com.plux.distribution.core.storage.application.port.in.StoreFileUseCase;
import com.plux.distribution.core.storage.domain.StorageKey;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MediaStorageService implements CrudMediaUseCase {

    private static final String MEDIA_BASE_PATH = "media";
    private static final Logger log = LogManager.getLogger(MediaStorageService.class);

    private final @NotNull StoreFileUseCase storeFileUseCase;
    private final @NotNull LoadFileUseCase loadFileUseCase;
    private final @NotNull DeleteFileUseCase deleteFileUseCase;
    private final @NotNull GenerateFileUrlUseCase generateFileUrlUseCase;
    private final @NotNull MediaRepositoryPort mediaRepository;

    public MediaStorageService(@NotNull StoreFileUseCase storeFileUseCase, @NotNull LoadFileUseCase loadFileUseCase,
            @NotNull DeleteFileUseCase deleteFileUseCase, @NotNull GenerateFileUrlUseCase generateFileUrlUseCase,
            @NotNull MediaRepositoryPort mediaRepository) {
        this.storeFileUseCase = storeFileUseCase;
        this.loadFileUseCase = loadFileUseCase;
        this.deleteFileUseCase = deleteFileUseCase;
        this.generateFileUrlUseCase = generateFileUrlUseCase;
        this.mediaRepository = mediaRepository;
    }

    @Override
    public @NotNull MediaId upload(@NotNull InputStream data, @NotNull String contentType, long size,
            @Nullable String scope) {

        scope = (scope != null && !scope.isEmpty()) ? scope : "default";
        String fullStoragePath = MEDIA_BASE_PATH + "/" + scope;

        StorageKey storageKey = storeFileUseCase.store(fullStoragePath, data, contentType, size);

        while (true) {
            Media media = new Media(MediaId.generate(), storageKey);

            try {
                mediaRepository.save(media);
                log.info("Media uploaded successfully with ID: {} and StorageKey: {}",
                        media.id().value(), media.storageKey().path());

                return media.id();

            } catch (DuplicateMediaIdException e) {
                log.warn("Generated MediaId {} already exists in repository. Retrying with a new ID.",
                        media.id().value());
            }
        }
    }

    @Override
    public void delete(@NotNull MediaId id) throws MediaNotFoundException {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new MediaNotFoundException(id));

        deleteFileUseCase.delete(media.storageKey());
        mediaRepository.delete(id);
        log.info("Media with ID: {} and StorageKey: {} deleted successfully.", id.value(),
                media.storageKey().path());
    }

    @Override
    public @NotNull StoredFile download(@NotNull MediaId id) throws MediaNotFoundException {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new MediaNotFoundException(id));

        try {
            return loadFileUseCase.load(media.storageKey());
        } catch (FileNotFoundException e) {
            log.error("Media metadata found for ID: {} but file not found in storage at path: {}", id.value(),
                    media.storageKey().path(), e);
            throw new MediaNotFoundException(id);
        }
    }

    @Override
    public @NotNull StoredFileUrl generateUrl(@NotNull MediaId id) throws MediaNotFoundException {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new MediaNotFoundException(id));

        try {
            return generateFileUrlUseCase.generateUrl(media.storageKey());
        } catch (FileNotFoundException e) {
            log.error("Media metadata found for ID: {} but file not found in storage at path: {}", id.value(),
                    media.storageKey().path(), e);
            throw new MediaNotFoundException(id);
        }
    }
}
