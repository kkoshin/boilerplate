package com.github.foodiestudio.application

import com.squareup.moshi.JsonClass

class Event(
    val title: String,
    val beginDateAndTime: String
)

@JsonClass(generateAdapter = true)
class EventJson(
    val title: String,
    val begin_date: String,
    val begin_time: String
)