package net.protsenko.notificationbot.bot;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.protsenko.notificationbot.config.TelegramProperties;
import net.protsenko.notificationbot.service.UpdateDispatcher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Bot extends TelegramWebhookBot {

    TelegramProperties telegramProperties;
    UpdateDispatcher updateDispatcher;

    public Bot(@Qualifier("telegramProperties") TelegramProperties telegramProperties, UpdateDispatcher updateDispatcher) {
        super(telegramProperties.getToken());
        this.telegramProperties = telegramProperties;
        this.updateDispatcher = updateDispatcher;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return updateDispatcher.distribute(update, this);
    }

    @Override
    public String getBotPath() {
        return telegramProperties.getUrl();
    }

    @Override
    public String getBotUsername() {
        return telegramProperties.getName();
    }
}
