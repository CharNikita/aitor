package ru.goncharenko.aitor.configuration;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfiguration {
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
            .defaultSystem("Ты бот-лоровед который знает всё об этом чате. Лор - это вся информация которая есть в чате")
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();
    }
}
