package com.robertas.ugithub.core.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "items")
    val items: List<UserNetwork>
)
