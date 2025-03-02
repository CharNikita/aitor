package ru.goncharenko.aitor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.goncharenko.aitor.tools.UserInformationTool;

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

    @Bean
    public ChatClient chatClient(
        ChatModel chatModel,
        UserInformationTool tool
    ) {
        return ChatClient.builder(chatModel)
            .defaultSystem("Ты бот-лоровед который знает всё об этом чате. Лор - это вся инфромация которая есть в чате")
            .defaultTools(tool)
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();
    }
}
