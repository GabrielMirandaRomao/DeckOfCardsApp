package com.example.cardsapp.repository.dto

import com.google.gson.annotations.SerializedName

data class ShuffleDeckDto(
    @SerializedName("deck_id") val id : String,
    @SerializedName("success") val success : Boolean,
    @SerializedName("shuffled") val shuffled : Boolean,
    @SerializedName("remaining") val remaining : Int
)