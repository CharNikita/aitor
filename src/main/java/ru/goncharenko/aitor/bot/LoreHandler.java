package ru.goncharenko.aitor.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class LoreHandler implements TelegramUpdateHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoreHandler.class);

    private final ChatClient chatClient;
    private static final String COMMAND = "/lore";

    public LoreHandler(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public boolean isAccept(Update update) {
        if (update.getMessage() == null || update.getMessage().getText() == null) {
            logger.info("Update has no message");
            return false;
        }
        return update.getMessage().getText().startsWith(COMMAND);
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        final var command = update.getMessage().getText();

        final var modelResponse = chatClient
            .prompt()
            .user(command.substring(COMMAND.length()))
            .call()
            .content();

        return SendMessage.builder()
            .chatId(update.getMessage().getChatId())
            .text(modelResponse)
            .build();
    }
}
