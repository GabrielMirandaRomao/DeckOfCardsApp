package com.example.cardsapp.repository.dto

import com.google.gson.annotations.SerializedName

data class DrawCardDto(
    @SerializedName("success") val success : Boolean,
    @SerializedName("deck_id") val id : String,
    @SerializedName("cards") val cards : List<CardDto>,
    @SerializedName("remaining") val remaining : Int
)

data class CardDto (
    @SerializedName("code") val code : String,
    @SerializedName("image") val image : String,
    @SerializedName("images") val images : ImageCardDto,
    @SerializedName("value") val value : String,
    @SerializedName("suit") val suit: String
)

data class ImageCardDto (
    @SerializedName("svg") val svg : String,
    @SerializedName("png") val png : String,
)