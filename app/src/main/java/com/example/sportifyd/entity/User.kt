package com.example.sportifyd.entity

import java.io.Serializable

data class User(
    val fullName: String = "",
    val userName: String ="",
    val phoneNumber: String="",
    val email: String="",
    val password: String="",
    val bio: String = "",
    val photo: String = "",
    val events: Map<String, Boolean> = mapOf(),
    val organizedEventsNumber: Int = 0,
): Serializable