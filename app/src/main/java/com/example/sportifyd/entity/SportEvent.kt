package com.example.sportifyd.entity

import java.util.Date

data class SportEvent(
    val eventId: Int,
    val eventName: String,
    val location: String,
    val date: Date,
    val time: Long,
    val sportCategory: String,
    val maxParticipants: Int
){
    fun createEvent(){}

    fun searchEvents(){}
}