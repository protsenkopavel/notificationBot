package net.protsenko.notificationbot.service.contract;

import net.protsenko.notificationbot.bot.Bot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface QueryListener {
    BotApiMethod<?> answerQuery(CallbackQuery query, String[] words, Bot bot);
}
