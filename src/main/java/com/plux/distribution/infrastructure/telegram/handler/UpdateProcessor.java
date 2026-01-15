package com.plux.distribution.infrastructure.telegram.handler;

import com.plux.distribution.core.chat.application.port.in.CreateChatUseCase;
import com.plux.distribution.core.interaction.application.command.DeliverInteractionCommand;
import com.plux.distribution.core.interaction.application.port.in.InteractionDeliveryUseCase;
import com.plux.distribution.core.interaction.domain.Participant.BotParticipant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import com.plux.distribution.core.interaction.domain.content.ButtonClickContent;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment.DisplayType;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.mediastorage.application.port.in.UploadMediaUseCase;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import com.plux.distribution.infrastructure.telegram.port.TgChatLinker;
import com.plux.distribution.infrastructure.telegram.port.TgMessageGlobalId;
import com.plux.distribution.infrastructure.telegram.port.TgMessageLinker;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class UpdateProcessor {

    private final @NotNull TelegramClient telegramClient;
    private final @NotNull InteractionDeliveryUseCase interactionDeliveryUseCase;
    private final @NotNull CreateChatUseCase createChatUseCase;
    private final @NotNull UploadMediaUseCase uploadMediaUseCase;
    private final @NotNull TgMessageLinker tgMessageLinker;
    private final @NotNull TgChatLinker tgChatLinker;

    public UpdateProcessor(@NotNull TelegramClient telegramClient,
            @NotNull InteractionDeliveryUseCase interactionDeliveryUseCase,
            @NotNull CreateChatUseCase createChatUseCase, @NotNull UploadMediaUseCase uploadMediaUseCase,
            @NotNull TgMessageLinker tgMessageLinker, @NotNull TgChatLinker tgChatLinker) {
        this.telegramClient = telegramClient;
        this.interactionDeliveryUseCase = interactionDeliveryUseCase;
        this.createChatUseCase = createChatUseCase;
        this.uploadMediaUseCase = uploadMediaUseCase;
        this.tgMessageLinker = tgMessageLinker;
        this.tgChatLinker = tgChatLinker;
    }

    public void processMessageGroup(List<Message> parts) {
        parts = parts.stream()
                .sorted(Comparator.comparingInt(Message::getMessageId))
                .toList();

        Message primary = parts.getFirst();

        var chatId = tgChatLinker.getChatId(primary.getChatId());
        if (chatId == null) {
            chatId = createChatUseCase.create().id();

            tgChatLinker.link(chatId, primary.getChatId());
        }

        String text = parts.stream()
                .map(Message::getText)
                .filter(t -> t != null && !t.isBlank())
                .findFirst()
                .orElse("");

        List<MessageAttachment> attachments = new ArrayList<>();

        attachments.addAll(parts.stream()
                .map(this::extractPhoto)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList());

        attachments.addAll(parts.stream()
                .map(this::extractDocument)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList());

        InteractionContent content = new SimpleMessageContent(text, attachments);

        Optional<Message> replyToMsg = parts.stream()
                .map(Message::getReplyToMessage)
                .filter(Objects::nonNull)
                .findFirst();

        if (replyToMsg.isPresent()) {
            var replyTo = tgMessageLinker.getInteractionId(
                    new TgMessageGlobalId(
                            replyToMsg.get().getMessageId(),
                            replyToMsg.get().getChatId()
                    )
            );

            content = new ReplyMessageContent(content, replyTo);
        }

        var command = new DeliverInteractionCommand(
                new ChatParticipant(chatId),
                new BotParticipant(),
                content
        );

        var interactionId = interactionDeliveryUseCase.deliver(command);

        for (Message p : parts) {
            tgMessageLinker.link(
                    interactionId,
                    new TgMessageGlobalId(p.getMessageId(), p.getChatId())
            );
        }
    }

    public void processCallback(CallbackQuery callbackQuery) {
        var tag = callbackQuery.getData();

        var tgChatId = callbackQuery.getMessage().getChatId();
        var tgMessageId = callbackQuery.getMessage().getMessageId();

        var chatId = Optional.ofNullable(tgChatLinker.getChatId(tgChatId)).orElseThrow();
        var interactionId = tgMessageLinker.getInteractionId(new TgMessageGlobalId(tgMessageId, tgChatId));

        interactionDeliveryUseCase.deliver(new DeliverInteractionCommand(
                new ChatParticipant(chatId),
                new BotParticipant(),
                new ButtonClickContent(interactionId, tag)
        ));
    }

    private Optional<MessageAttachment> extractPhoto(Message message) {
        if (message.getPhoto() == null || message.getPhoto().isEmpty()) {
            return Optional.empty();
        }

        var largestPhoto = message.getPhoto().stream()
                .max(Comparator.comparingInt(PhotoSize::getFileSize))
                .orElseThrow();

        MediaId mediaId = uploadAttachment(
                largestPhoto.getFileId(),
                "image/jpeg",
                largestPhoto.getFileSize()
        );

        return Optional.of(new MediaAttachment(mediaId, DisplayType.PHOTO));
    }

    private Optional<MessageAttachment> extractDocument(Message message) {
        if (!message.hasDocument()) {
            return Optional.empty();
        }

        Document doc = message.getDocument();

        MediaId mediaId = uploadAttachment(
                doc.getFileId(),
                Optional.ofNullable(doc.getMimeType()).orElse("application/octet-stream"),
                doc.getFileSize()
        );

        return Optional.of(new MediaAttachment(mediaId, DisplayType.DOCUMENT));
    }

    private MediaId uploadAttachment(String fileId, String mimeType, long fileSize) {
        try {
            File file = telegramClient.execute(new GetFile(fileId));

            try (InputStream inputStream = telegramClient.downloadFileAsStream(file)) {
                System.out.println(file.getFilePath());
                return uploadMediaUseCase.upload(
                        inputStream,
                        mimeType,
                        file.getFilePath(),
                        fileSize,
                        "in_chat"
                );
            }

        } catch (TelegramApiException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
