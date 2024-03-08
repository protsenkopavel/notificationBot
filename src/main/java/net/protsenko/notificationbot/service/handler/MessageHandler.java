package net.protsenko.notificationbot.service.handler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.protsenko.notificationbot.bot.Bot;
import net.protsenko.notificationbot.service.contract.AbstractHandler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageHandler extends AbstractHandler {


    @Override
    public BotApiMethod<?> answer(BotApiObject object, Bot bot) {
        var message = (Message) object;
        throw new UnsupportedOperationException();
    }
}