package com.inmicro.habrparser

import com.prof18.rssparser.model.ItunesItemData
import com.prof18.rssparser.model.RawEnclosure
import com.prof18.rssparser.model.YoutubeItemData

data class RssItem (
    val guid: String,
    val title: String,
    val author: String,
    val link: String,
    val pubDate: String,
    val description: String,
    val itunesItemData: ItunesItemData,
    val commentsUrl: String,
    val youtubeItemData: YoutubeItemData,
    val rawEnclosure: RawEnclosure
)