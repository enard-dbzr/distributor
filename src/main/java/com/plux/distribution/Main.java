package com.plux.distribution;

import com.plux.distribution.infrastructure.persistence.ConstUserTgLinker;
import com.plux.distribution.infrastructure.telegram.TelegramHandler;
import com.plux.distribution.infrastructure.telegram.sender.TelegramMessageSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        var botToken = System.getenv("TG_TOKEN");
        var tgUserId = Long.parseLong(System.getenv("TG_USER_ID"));

        var tgUserLinker = new ConstUserTgLinker(tgUserId);

        var tgClient = new OkHttpTelegramClient(botToken);
        var sender = new TelegramMessageSender(tgClient, tgUserLinker, null);

        try (var botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new TelegramHandler(sender));

            System.out.println("Bot successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            log.error("e: ", e);
        }

    }
}