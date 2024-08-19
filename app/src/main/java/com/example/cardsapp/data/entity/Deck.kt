package com.example.cardsapp.data.entity

import java.io.Serializable

data class Deck(
    val id: String = "",
    val success: Boolean = false,
    val shuffled: Boolean = false,
    val remaining: Int = -1
) : Serializable
