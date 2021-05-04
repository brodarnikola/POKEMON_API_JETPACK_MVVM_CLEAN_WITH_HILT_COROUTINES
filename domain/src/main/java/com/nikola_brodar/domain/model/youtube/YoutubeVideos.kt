package com.nikola_brodar.domain.model.youtube



data class YoutubeVideos(

    val id: YoutubeVideoId = YoutubeVideoId(),
    val snippet: YoutubeVideoSnippet = YoutubeVideoSnippet(),
    var showProgressBar: Boolean = false
)
