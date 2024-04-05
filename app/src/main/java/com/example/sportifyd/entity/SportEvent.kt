package com.example.sportifyd.entity

import java.util.Date

data class SportEvent(
    val eventName: String = "",
    val location: String = "",
    val date: String = "",
    val time: String = "",
    val sportCategory: String = "",
    val maxParticipants: String = "",
    val duration: String = ""
)