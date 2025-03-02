package ru.goncharenko.aitor.bot;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramUpdateHandler {
    boolean isAccept(Update update);

    BotApiMethod<?> handle(Update update);
}
