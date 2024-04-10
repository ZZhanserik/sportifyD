package com.example.sportify.presentation.home.adapter

import android.os.Parcelable
import java.io.Serializable
data class PopularEvent(
    val eventName: String,
    val eventStatus: String,
    val pricePerPerson: String,
    val taken: String,
): Serializable