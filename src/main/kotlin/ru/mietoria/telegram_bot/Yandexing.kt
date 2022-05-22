package ru.mietoria.telegram_bot

import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import org.jsoup.Jsoup

fun searching(message: CommonMessage<TextContent>): String {
    val url = modifyMessageToRequest(message)
    val doc = Jsoup.connect(url)
        .userAgent("Mozilla/5.0")
        .timeout(10000)
        .get()
    val links =
        doc.select(".VanillaReact.OrganicTitle.OrganicTitle_wrap.Typo.Typo_text_l.Typo_line_m.organic__title-wrapper")
            .select("a[href]").attr("href")
    val description =
        doc.select(".OrganicTextContentSpan").attr("span")
    println(description.first().toString())
    return links.toString()
}

fun modifyMessageToRequest(message: CommonMessage<TextContent>): String =
    "https://yandex.ru/search/?text=" + (message.content.text.replace(" ", "+")) + "&lr=2"