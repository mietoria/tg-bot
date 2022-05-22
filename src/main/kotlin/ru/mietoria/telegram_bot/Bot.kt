package ru.mietoria.telegram_bot

import dev.inmo.tgbotapi.bot.Ktor.telegramBot
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import dev.inmo.tgbotapi.extensions.utils.formatting.*
import dev.inmo.tgbotapi.extensions.utils.whenWithUser
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.abstracts.Message
import dev.inmo.tgbotapi.types.message.content.TextContent
import dev.inmo.tgbotapi.utils.PreviewFeature
import dev.inmo.tgbotapi.utils.RiskFeature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@OptIn(PreviewFeature::class, RiskFeature::class)
suspend fun main() {
    val bot = telegramBot(System.getenv("BOT_TOKEN"))
    val scope = CoroutineScope(Dispatchers.Default)
    println("Server Start")
    bot.buildBehaviourWithLongPolling(scope) {

        onCommand("start") {
            commandStart(bot, it)
        }

        onCommand("help") {
            commandHelp(bot, it)
        }

        onText {
            justGoogleIt(bot, it)
        }
    }.join()
}

@OptIn(PreviewFeature::class)
suspend fun commandStart(bot: TelegramBot, message: Message) {
    message.whenWithUser {
        bot.sendTextMessage(
            message.chat.id,
            "Hello, ${it.user.username?.usernameWithoutAt}, I am ${bot.getMe().firstName}"
        )
    }

}

suspend fun commandHelp(bot: TelegramBot, message: Message) {
    bot.sendTextMessage(message.chat.id, "I help yandexing queries. Just send me your question.")
}

@OptIn(RiskFeature::class, PreviewFeature::class)
suspend fun justGoogleIt(
    bot: TelegramBot,
    message: CommonMessage<TextContent>
) {
    val messageText: String = message.content.text
    if (messageText.startsWith("/")) {
        return
    }

    //bot.sendMessage(message.chat.id, buildEntities { link("link", "vk.com") })
    bot.sendTextMessage(message.chat.id, buildEntities { link("link", searching(message)) })
}