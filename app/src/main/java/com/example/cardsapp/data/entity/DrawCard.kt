package com.example.cardsapp.data.entity

data class DrawCard(
    val success: Boolean,
    val deckId: String,
    val cards: List<Card>,
    val remaining: Int
)

data class Card(
    val code: String,
    val image: String,
    val images: ImageCard,
    val value: String,
    val suit: String
)

data class ImageCard(
    val svg: String,
    val png: String
)