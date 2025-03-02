package ru.goncharenko.aitor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClientBuilder;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.VectorStore;
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

    @Bean
    public ChatClient chatClient(
        ChatModel chatModel,
        VectorStore vectorStore
    ) {
        return ChatClient.builder(chatModel)
            .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
            .build();
    }
}
