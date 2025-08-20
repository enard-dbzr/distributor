package com.plux.distribution;

import com.plux.distribution.application.port.out.user.CreateUserPort;
import com.plux.distribution.application.service.ExecuteActionService;
import com.plux.distribution.application.service.FlowFeedbackProcessor;
import com.plux.distribution.application.service.MessageDeliveryService;
import com.plux.distribution.application.service.RegisterFeedbackService;
import com.plux.distribution.application.workflow.DefaultContextManager;
import com.plux.distribution.application.workflow.frame.FrameRegistry;
import com.plux.distribution.application.workflow.core.FrameFactory;
import com.plux.distribution.application.workflow.frame.utils.SequenceFrame;
import com.plux.distribution.infrastructure.persistence.DbChatRepository;
import com.plux.distribution.infrastructure.persistence.DbFeedbackRepository;
import com.plux.distribution.infrastructure.persistence.DbFrameRepository;
import com.plux.distribution.infrastructure.persistence.DbMessageRepository;
import com.plux.distribution.infrastructure.persistence.DbTgChatLinker;
import com.plux.distribution.infrastructure.persistence.DbTgMessageLinker;
import com.plux.distribution.infrastructure.persistence.DbUserRepository;
import com.plux.distribution.infrastructure.persistence.config.HibernateConfig;
import com.plux.distribution.infrastructure.telegram.TelegramActionExecutor;
import com.plux.distribution.infrastructure.telegram.TelegramHandler;
import com.plux.distribution.infrastructure.telegram.sender.TelegramMessageSender;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        var botToken = System.getenv("TG_TOKEN");

        var hibernateConfig = new HibernateConfig(
                System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD")
        );

        var messageRepo = new DbMessageRepository(hibernateConfig.getSessionFactory());
        var feedbackRepo = new DbFeedbackRepository(hibernateConfig.getSessionFactory());
        var chatRepo = new DbChatRepository(hibernateConfig.getSessionFactory());
        var frameRepo = new DbFrameRepository(hibernateConfig.getSessionFactory());
        var userRepo = new DbUserRepository(hibernateConfig.getSessionFactory());

        var tgChatLinker = new DbTgChatLinker(hibernateConfig.getSessionFactory());
        var tgMessageLinker = new DbTgMessageLinker(hibernateConfig.getSessionFactory());

        var tgClient = new OkHttpTelegramClient(botToken);

        var sender = new TelegramMessageSender(tgClient, tgChatLinker, tgMessageLinker);
        var executor = new TelegramActionExecutor(tgClient, tgChatLinker, tgMessageLinker);

        var messageDeliveryService = new MessageDeliveryService(sender, messageRepo, messageRepo);
        var executeActionService = new ExecuteActionService(executor);

        var frameFactory = makeFrameFactory(userRepo);
        var frameContextManager = new DefaultContextManager(messageDeliveryService, executeActionService);
        var mainFeedbackProcessor = new FlowFeedbackProcessor(frameRepo, frameRepo, frameContextManager, frameFactory);

        var registerFeedbackService = new RegisterFeedbackService(messageRepo, feedbackRepo,
                chatRepo, mainFeedbackProcessor);

        var tgHandler = new TelegramHandler(registerFeedbackService, tgMessageLinker,
                tgMessageLinker, tgChatLinker, tgChatLinker);

        try (var botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, tgHandler);

            System.out.println("Bot successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            log.error("e: ", e);
        }

    }

    private static FrameFactory makeFrameFactory(CreateUserPort createUserPort) {
        var pin = System.getenv("BOT_PIN");

        var factory = new FrameRegistry();

        factory.register(new com.plux.distribution.application.workflow.frame.registration.hello.HelloFrame());
        factory.register(new com.plux.distribution.application.workflow.frame.registration.hello.PostHelloFrame());
        factory.register(new com.plux.distribution.application.workflow.frame.registration.pin.CheckPasswordFrame(pin));
        factory.register(new com.plux.distribution.application.workflow.frame.registration.pin.CorrectPasswordFrame());
        factory.register(new com.plux.distribution.application.workflow.frame.registration.pin.InorrectPasswordFrame());
        factory.register(
                new com.plux.distribution.application.workflow.frame.registration.user.StartUserBuildingFrame());
        factory.register(new com.plux.distribution.application.workflow.frame.registration.user.AskNameFrame());
        factory.register(new com.plux.distribution.application.workflow.frame.registration.user.AskEmailFrame());
        factory.register(new com.plux.distribution.application.workflow.frame.registration.user.AskAgeFrame());
        factory.register(new com.plux.distribution.application.workflow.frame.registration.user.AskCityFrame());
        factory.register(new com.plux.distribution.application.workflow.frame.registration.user.AskHobbyFrame());
        factory.register(
                new com.plux.distribution.application.workflow.frame.registration.user.FinalizeFrame(createUserPort));

        factory.register(new SequenceFrame("flow.registration", List.of(
                factory.get("registration.hello_frame"),
                factory.get("registration.check_pin"),
                factory.get("registration.user.start_building")
        )));

        return factory;
    }
}