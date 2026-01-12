package com.plux.distribution.infrastructure.api.mediastorage;

import com.plux.distribution.core.mediastorage.application.exception.MediaNotFoundException;
import com.plux.distribution.core.mediastorage.application.port.in.CrudMediaUseCase;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import com.plux.distribution.core.storage.application.dto.StoredFile;
import com.plux.distribution.core.storage.application.dto.StoredFileUrl;
import com.plux.distribution.infrastructure.api.mediastorage.response.MediaUrlResponse;
import com.plux.distribution.infrastructure.api.mediastorage.response.UploadMediaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.UUID;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/media")
@Tag(name = "Media Storage")
public class MediaStorageController {

    private final CrudMediaUseCase crudMediaUseCase;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MediaStorageController(CrudMediaUseCase crudMediaUseCase) {
        this.crudMediaUseCase = crudMediaUseCase;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload media",
            description = "Upload a new media file and return its ID",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Media was successfully uploaded"),
                    @ApiResponse(responseCode = "400", description = "Invalid file or request"),
            }
    )
    public ResponseEntity<UploadMediaResponse> upload(
            @Parameter(description = "File to upload", required = true)
            @RequestParam("file")
            MultipartFile file,

            @Parameter(description = "Optional scope for organizing files (e.g., chat ID)")
            @RequestParam(value = "scope", required = false)
            String scope
    ) throws IOException {

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || contentType.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content-Type is required");
        }

        MediaId mediaId = crudMediaUseCase.upload(
                file.getInputStream(),
                file.getContentType(),
                file.getSize(),
                scope
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(UploadMediaResponse.of(mediaId));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Download media",
            description = "Download media file by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Media file returned"),
                    @ApiResponse(responseCode = "404", description = "Media not found")
            }
    )
    public ResponseEntity<Resource> downloadMedia(
            @Parameter(description = "Media ID", required = true)
            @PathVariable("id") UUID id
    ) throws MediaNotFoundException {
        StoredFile storedFile = crudMediaUseCase.download(new MediaId(id));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(storedFile.metadata().contentType()))
                .contentLength(storedFile.metadata().size())
                .body(new InputStreamResource(storedFile.data()));
    }

    @GetMapping("/{id}/url")
    @Operation(
            summary = "Generate media URL",
            description = "Generate a pre-signed URL for direct access to the media file",
            responses = {
                    @ApiResponse(responseCode = "200", description = "URL generated"),
                    @ApiResponse(responseCode = "404", description = "Media not found")
            }
    )
    public ResponseEntity<MediaUrlResponse> generateMediaUrl(
            @Parameter(description = "Media ID", required = true)
            @PathVariable("id")
            UUID id
    ) throws MediaNotFoundException {
        StoredFileUrl storedFileUrl = crudMediaUseCase.generateUrl(new MediaId(id));

        return ResponseEntity.ok(new MediaUrlResponse(
                storedFileUrl.url().toString(),
                storedFileUrl.metadata().contentType()
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete media",
            description = "Delete media file by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Media was deleted"),
                    @ApiResponse(responseCode = "404", description = "Media not found")
            }
    )
    public ResponseEntity<Void> deleteMedia(
            @Parameter(description = "Media ID", required = true)
            @PathVariable("id") UUID id
    ) throws MediaNotFoundException {
        crudMediaUseCase.delete(new MediaId(id));
        return ResponseEntity.noContent().build();
    }


}
