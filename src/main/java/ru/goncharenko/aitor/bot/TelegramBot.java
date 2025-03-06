package ru.goncharenko.aitor.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.goncharenko.aitor.properties.BotProperties;

import java.util.List;
import java.util.Objects;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    private final List<TelegramUpdateHandler> handlerList;
    private final String botName;

    public TelegramBot(
        BotProperties botProperties,
        List<TelegramUpdateHandler> handlerList
    ) {
        super(botProperties.apiToken());
        this.botName = botProperties.name();
        this.handlerList = handlerList;
    }

    @Override
    public void onUpdateReceived(Update update) {
        handlerList.stream()
            .filter(handler -> handler.isAccept(update))
            .map(handler -> handler.handle(update))
            .filter(Objects::nonNull)
            .forEach(method -> {
                try {
                    execute(method);
                } catch (TelegramApiException e) {
                    logger.error(e.getMessage());
                }
            });
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }
}
