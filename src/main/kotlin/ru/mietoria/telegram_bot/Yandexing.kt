package ru.mietoria.telegram_bot

import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import org.jsoup.Jsoup

fun searching(message: CommonMessage<TextContent>): Pair<String, String> {
    val url = modifyMessageToRequest(message)
    val doc = Jsoup.connect(url)
        .userAgent("Mozilla/5.0")
        .timeout(10000)
        .get()
    val link =
        doc.select(".VanillaReact.OrganicTitle.OrganicTitle_wrap.Typo.Typo_text_l.Typo_line_m.organic__title-wrapper")
            .select("a[href]").attr("href")
    println(link.toString())
    val description =
        doc.select(".TextContainer.OrganicText.organic__text.text-container.Typo.Typo_text_m.Typo_line_m")
            .select("span").eachText().toString().substring(1..100) + "..."
    println(description)
    return Pair(link.toString(), description)
}

fun modifyMessageToRequest(message: CommonMessage<TextContent>): String =
    "https://yandex.ru/search/?text=" + (message.content.text.replace(" ", "+")) + "&lr=2"