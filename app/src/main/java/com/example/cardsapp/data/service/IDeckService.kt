package com.example.cardsapp.data.service

import com.example.cardsapp.repository.dto.DrawCardDto
import com.example.cardsapp.repository.dto.ShuffleDeckDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IDeckService {

    @GET("new/shuffle/")
    suspend fun getShuffleDeck(
        @Query("deck_count") count: Int
    ) : Response<ShuffleDeckDto>

    @GET("{deckId}/draw/")
    suspend fun getDrawCards(
        @Path("deckId") deckId: String,
        @Query("count") count: Int
    ) : Response<DrawCardDto>

    @POST("{deckId}/shuffle/")
    suspend fun shuffleDeck(
        @Path("deckId") deckId: String
    ) : Response<ShuffleDeckDto>

}