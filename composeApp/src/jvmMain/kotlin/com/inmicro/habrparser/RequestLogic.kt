package com.inmicro.habrparser

import androidx.compose.ui.text.buildAnnotatedString
import be.digitalia.compose.htmlconverter.htmlToAnnotatedString
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

    suspend fun rssParseRequest(input: String): List<LocalRssItem> {
        val rssChannel: RssChannel = rssParser.getRssChannel("https://habr.com/ru/rss/search/?q=$input&order_by=date&target_type=posts&hl=ru&fl=ru&fl=ru&limit=100")
        rssChannel.items.forEach {
            feed.add(
                LocalRssItem(
                    it.title!!,
                    it.link!!,
                    it.pubDate!!,
                    htmlToString(it.description!!).replace("Читать далее", ""),
                )
            )
        }



        return feed
    }

//    private val client = HttpClient(OkHttp) {
//        engine {
//            config {
//                followRedirects(true)
//            }
//        }
//    }
//
//    suspend fun testGetRequest(): String {
//        val response = client.get("https://api.habr.ru/v1")
//        return response.bodyAsText()
//    }

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}