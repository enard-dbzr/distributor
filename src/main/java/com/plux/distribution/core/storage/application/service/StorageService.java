package com.plux.distribution.core.storage.application.service;

import com.plux.distribution.core.storage.domain.FileMetadata;
import com.plux.distribution.core.storage.application.dto.StoredFileUrl;
import com.plux.distribution.core.storage.application.exception.ObjectAlreadyExistsException;
import com.plux.distribution.core.storage.application.port.in.DeleteFileUseCase;
import com.plux.distribution.core.storage.application.port.in.GenerateFileUrlUseCase;
import com.plux.distribution.core.storage.application.port.in.LoadFileUseCase;
import com.plux.distribution.core.storage.application.port.in.StoreFileUseCase;
import com.plux.distribution.core.storage.application.port.out.StoragePort;
import com.plux.distribution.core.storage.domain.StorageKey;
import com.plux.distribution.core.storage.application.dto.StoredFile;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.jetbrains.annotations.NotNull;

public class StorageService implements StoreFileUseCase, LoadFileUseCase, DeleteFileUseCase, GenerateFileUrlUseCase {

    private final StoragePort storage;
    private final Duration defaultUrlTtl;

    public StorageService(@NotNull StoragePort storage, @NotNull Duration defaultUrlTtl) {
        this.storage = storage;
        this.defaultUrlTtl = defaultUrlTtl;
    }

    @Override
    public @NotNull StorageKey store(@NotNull String basePath, @NotNull InputStream data, @NotNull String contentType,
            long size) {
        while (true) {
            try {
                String uuid = UUID.randomUUID().toString();

                String ext;

                try {
                    ext = MimeTypes.getDefaultMimeTypes().forName(contentType).getExtension();
                } catch (MimeTypeException e) {
                    ext = "";
                }

                String fullPath = normalize(basePath) + "/" + uuid + ext;

                StorageKey key = new StorageKey(fullPath);


                storage.save(fullPath, data, contentType, size);

                return key;

            } catch (ObjectAlreadyExistsException _) {
            }
        }

    }

    @Override
    public @NotNull StoredFile load(@NotNull StorageKey key) throws FileNotFoundException {
        InputStream data = storage.load(key.path());
        FileMetadata metadata = storage.loadMetadata(key.path());

        return new StoredFile(data, metadata);
    }

    @Override
    public void delete(@NotNull StorageKey key) {
        storage.delete(key.path());
    }

    @Override
    public @NotNull StoredFileUrl generateUrl(@NotNull StorageKey key) throws FileNotFoundException {
        URL url = storage.generateUrl(key.path(), defaultUrlTtl);
        FileMetadata metadata = storage.loadMetadata(key.path());

        return new StoredFileUrl(url, metadata);
    }

    private String normalize(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }
}
