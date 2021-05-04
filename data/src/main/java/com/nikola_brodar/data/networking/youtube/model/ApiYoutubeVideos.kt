package com.nikola_brodar.data.networking.youtube.model



data class ApiYoutubeVideos(

    val id: ApiYoutubeVideoId = ApiYoutubeVideoId(),
    val snippet: ApiYoutubeVideoSnippet = ApiYoutubeVideoSnippet()
)
