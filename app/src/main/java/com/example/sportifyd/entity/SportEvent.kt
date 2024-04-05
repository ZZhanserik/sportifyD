package com.example.sportifyd.entity

import java.util.Date

data class SportEvent(
    val eventName: String = "",
    val location: String = "",
    val level: String = "",
    val date: String = "",
    val time: String = "",
    val sportCategory: String = "",
    val participantsNumber: String = "",
    val maxParticipants: String = "",
    val duration: String = "",
    val status: String = SportEventStatus.OPEN.name,
)

enum class SportEventStatus {
    OPEN, CLOSED
}