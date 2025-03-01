package ru.goncharenko.aitor.bot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot")
public record BotConfig(String apiToken, String name) {
}
