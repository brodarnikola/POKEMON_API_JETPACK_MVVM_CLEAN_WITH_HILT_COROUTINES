package com.nikola_brodar.data.networking.youtube.model



data class ApiYoutubeVideosMain(

    val nextPageToken: String,
    val regionCode: String = "",
    val items: List<ApiYoutubeVideos> = listOf()
)
