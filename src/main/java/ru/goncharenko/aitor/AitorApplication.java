package ru.goncharenko.aitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(AitorApplication.class, args);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(List<LongPollingBot> bots) throws TelegramApiException {
        final var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        for (LongPollingBot bot : bots) {
            telegramBotsApi.registerBot(bot);
        }
        return telegramBotsApi;
    }
}
