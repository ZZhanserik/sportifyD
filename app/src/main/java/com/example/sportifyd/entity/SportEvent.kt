package com.example.sportifyd.entity

import java.io.Serializable

data class SportEvent(
    val eventId: String = "",
    val eventName: String = "",
    val level: String = "",
    val location: String = "",
    val price: String = "",
    val date: String = "",
    val time: String = "",
    val duration: String = "",
    val status: String = SportEventStatus.OPEN.name,
    val sportCategory: String = "",
    val participantsNumber: String = "",
    val maxParticipants: String = "",
    val participants: Map<String, Boolean> = mapOf(),
) : Serializable

enum class SportEventStatus {
    OPEN, CLOSED
}