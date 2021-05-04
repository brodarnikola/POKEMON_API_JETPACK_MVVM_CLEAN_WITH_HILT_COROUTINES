package com.nikola_brodar.domain.model.youtube



data class YoutubeVideosMain(

    val nextPageToken: String = "",
    val regionCode: String = "",
    val items: List<YoutubeVideos> = listOf()
)
