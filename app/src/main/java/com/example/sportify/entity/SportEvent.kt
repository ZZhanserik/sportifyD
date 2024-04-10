package com.example.sportify.entity

import java.io.Serializable

data class SportEvent(
    val eventId: String = "",
    val eventName: String = "",
    val level: String = "",
    val location: String = "",
    val price: String = "0",
    val date: String = "",
    val time: String = "",
    val duration: String = "",
    val status: String = SportEventStatus.OPEN.name,
    val sportCategory: String = "",
    val participantsNumber: Int = 0,
    val maxParticipants: Int = 0,
    val participants: Map<String, Boolean> = mapOf(),
    val description: String = ""
) : Serializable

enum class SportEventStatus {
    OPEN, CLOSED
}