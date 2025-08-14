package com.plux.distribution.infrastructure.telegram;

import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.message.participant.UserParticipant;
import com.plux.distribution.domain.user.UserId;
import com.plux.distribution.infrastructure.telegram.sender.TelegramMessageSender;
import java.util.List;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramHandler implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramMessageSender sender;

    public TelegramHandler(TelegramMessageSender sender) {
        this.sender = sender;
    }

    @Override
    public void consume(Update update) {
        System.out.println(update.toString());

        var msg = new Message(new UserParticipant(new UserId(1)),
                new SimpleMessageContent(
                        "test",
                        List.of(
                                new ButtonAttachment("hello", "hello"),
                                new ButtonAttachment("world", "world")
                        )
                ));

        sender.send(new MessageId(1), msg.getRecipient(), msg.getContent());
    }
}
