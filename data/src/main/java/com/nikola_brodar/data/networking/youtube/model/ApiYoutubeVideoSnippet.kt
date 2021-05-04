package com.nikola_brodar.data.networking.youtube.model



data class ApiYoutubeVideoSnippet(

    val title: String = "",
    val description: String = "",
    val thumbnails: ApiYoutubeVideoThumbnails = ApiYoutubeVideoThumbnails()

)
