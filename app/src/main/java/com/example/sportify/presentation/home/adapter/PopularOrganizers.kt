package com.example.sportify.presentation.home.adapter

data class PopularOrganizers(
    val organizerName: String,
    val organizerStatus: String,
    val category: String,
    val photo: String = "",
)