package com.inmicro.habrparser

import be.digitalia.compose.htmlconverter.htmlToString
import com.prof18.rssparser.RssParserBuilder
import com.prof18.rssparser.model.RssChannel
import okhttp3.OkHttpClient

class RequestLogic {
    private val platform = getPlatform()
    val feed = mutableListOf<LocalRssItem>()

    val builder = RssParserBuilder(
        callFactory = OkHttpClient(),
        charset = Charsets.UTF_8
    )
    val rssParser = builder.build()

    suspend fun rssParseRequest(input: String, orderBy: String): List<LocalRssItem> {
        val rssChannel: RssChannel = rssParser.getRssChannel("https://habr.com/ru/rss/search/?q=$input&order_by=$orderBy&target_type=posts&hl=ru&fl=ru&fl=ru&limit=100")
        rssChannel.items.forEach {
            feed.add(
                LocalRssItem(
                    it.title!!,
                    it.link!!,
                    it.pubDate!!,
                    htmlToString(it.description!!).replace("Читать далее", "").replace("Читать дальше →", ""),
                )
            )
        }



        return feed
    }
}