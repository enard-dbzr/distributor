package com.plux.distribution.core.mediastorage.application.service;

import com.plux.distribution.core.mediastorage.application.dto.MediaMetadata;
import com.plux.distribution.core.mediastorage.application.dto.StoredMedia;
import com.plux.distribution.core.mediastorage.application.dto.StoredMediaUrl;
import com.plux.distribution.core.mediastorage.application.exception.DuplicateMediaIdException;
import com.plux.distribution.core.mediastorage.application.exception.MediaNotFoundException;
import com.plux.distribution.core.mediastorage.application.port.in.CrudMediaUseCase;
import com.plux.distribution.core.mediastorage.application.port.out.MediaRepositoryPort;
import com.plux.distribution.core.mediastorage.domain.Media;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import com.plux.distribution.core.storage.application.port.in.DeleteFileUseCase;
import com.plux.distribution.core.storage.application.port.in.GenerateFileUrlUseCase;
import com.plux.distribution.core.storage.application.port.in.LoadFileUseCase;
import com.plux.distribution.core.storage.application.port.in.StoreFileUseCase;
import com.plux.distribution.core.storage.domain.StorageKey;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
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
    public @NotNull MediaId upload(@NotNull InputStream data, @NotNull String contentType, @NotNull String filename,
            long size, @Nullable String scope) {

        scope = (scope != null && !scope.isEmpty()) ? scope : "default";
        String fullStoragePath = MEDIA_BASE_PATH + "/" + scope;

        String ext = getFileExtension(filename);

        StorageKey storageKey = storeFileUseCase.store(fullStoragePath, data, contentType, ext, size);

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
    public @NotNull StoredMedia download(@NotNull MediaId id) throws MediaNotFoundException {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new MediaNotFoundException(id));

        try {
            var file = loadFileUseCase.load(media.storageKey());
            return new StoredMedia(file, composeMetadata(media));
        } catch (FileNotFoundException e) {
            log.error("Media metadata found for ID: {} but file not found in storage at path: {}", id.value(),
                    media.storageKey().path(), e);
            throw new MediaNotFoundException(id);
        }
    }

    @Override
    public @NotNull StoredMediaUrl generateUrl(@NotNull MediaId id) throws MediaNotFoundException {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new MediaNotFoundException(id));

        try {
            var fileUrl = generateFileUrlUseCase.generateUrl(media.storageKey());
            return new StoredMediaUrl(fileUrl, composeMetadata(media));
        } catch (FileNotFoundException e) {
            log.error("Media metadata found for ID: {} but file not found in storage at path: {}", id.value(),
                    media.storageKey().path(), e);
            throw new MediaNotFoundException(id);
        }
    }

    private static MediaMetadata composeMetadata(Media media) {
        String ext = Optional.ofNullable(getFileExtension(media.storageKey().path()))
                .orElse("");

        var filename = media.id().value() + ext;

        return new MediaMetadata(filename);
    }

    private static @Nullable String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex);
        } else {
            return null;
        }
    }
}
