package com.nikola_brodar.domain.model.youtube



data class YoutubeVideoSnippet(

    var title: String = "",
    val description: String = "",
    val thumbnails: YoutubeVideoThumbnails = YoutubeVideoThumbnails()

)
