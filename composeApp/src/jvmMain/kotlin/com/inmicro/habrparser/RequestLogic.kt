package com.inmicro.habrparser

import com.prof18.rssparser.RssParserBuilder
import com.prof18.rssparser.model.RssChannel
import okhttp3.OkHttpClient

class RequestLogic {
    private val platform = getPlatform()

    val builder = RssParserBuilder(
        callFactory = OkHttpClient(),
        charset = Charsets.UTF_8
    )
    val rssParser = builder.build()

    suspend fun rssParseRequest(input: String): String {
        val rssChannel: RssChannel = rssParser.getRssChannel("https://habr.com/ru/rss/search/?q=$input&order_by=date&target_type=posts&hl=ru&fl=ru&fl=ru")
        return rssChannel.items.joinToString(separator = "\n")
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