package ru.goncharenko.aitor.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Objects;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    private final List<TelegramUpdateHandler> handlerList;
    private final String botName;

    public TelegramBot(
        BotConfig config,
        List<TelegramUpdateHandler> handlerList
    ) {
        super(config.apiToken());
        this.botName = config.name();
        this.handlerList = handlerList;
    }

    @Override
    public void onUpdateReceived(Update update) {
        MDC.put("updateId", String.valueOf(update.getUpdateId()));
        try {
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
        } finally {
            MDC.clear();
        }
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }
}
