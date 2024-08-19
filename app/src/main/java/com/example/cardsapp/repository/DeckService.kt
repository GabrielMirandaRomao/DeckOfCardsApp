package com.example.cardsapp.repository

import com.example.cardsapp.data.service.IDeckService
import com.example.cardsapp.repository.dto.DrawCardDto
import com.example.cardsapp.repository.dto.ShuffleDeckDto
import com.example.cardsapp.utils.ProcessStatus
import com.example.cardsapp.utils.Resource
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DeckService(
    private val service: IDeckService
) {

    suspend fun getShuffleDeck(deckCount: Int) : Resource<ShuffleDeckDto> {
        return try {
            val response = service.getShuffleDeck(deckCount)

            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                var message = response.code().toString()
                if (response.body() != null) {
                    message += " - ${response.body()}"
                }
                if (response.message().isNotBlank()) {
                    message += " - ${response.message()}"
                }
                if (response.errorBody() != null) {
                    message += " - ${response.errorBody()}"
                }
                Resource.Fail(ProcessStatus.Fail, message)
            }
        } catch (e: UnknownHostException) {
            Resource.Fail(ProcessStatus.NoInternet, e.message ?: e.toString())
        } catch (e: SocketTimeoutException) {
            Resource.Fail(ProcessStatus.TimeOut, e.message ?: e.toString())
        } catch (e: Exception) {
            Resource.Fail(ProcessStatus.Fail, e.message ?: e.toString())
        }
    }

    suspend fun getDrawCards(deckId : String, cardsCount: Int) : Resource<DrawCardDto> {
        return try {
            val response = service.getDrawCards(deckId, cardsCount)

            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                var message = response.code().toString()
                if (response.body() != null) {
                    message += " - ${response.body()}"
                }
                if (response.message().isNotBlank()) {
                    message += " - ${response.message()}"
                }
                if (response.errorBody() != null) {
                    message += " - ${response.errorBody()}"
                }
                Resource.Fail(ProcessStatus.Fail, message)
            }
        } catch (e: UnknownHostException) {
            Resource.Fail(ProcessStatus.NoInternet, e.message ?: e.toString())
        } catch (e: SocketTimeoutException) {
            Resource.Fail(ProcessStatus.TimeOut, e.message ?: e.toString())
        } catch (e: Exception) {
            Resource.Fail(ProcessStatus.Fail, e.message ?: e.toString())
        }
    }

    suspend fun shuffleDeck(deckId : String) : Resource<ShuffleDeckDto> {
        return try {
            val response = service.shuffleDeck(deckId)

            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                var message = response.code().toString()
                if (response.body() != null) {
                    message += " - ${response.body()}"
                }
                if (response.message().isNotBlank()) {
                    message += " - ${response.message()}"
                }
                if (response.errorBody() != null) {
                    message += " - ${response.errorBody()}"
                }
                Resource.Fail(ProcessStatus.Fail, message)
            }
        } catch (e: UnknownHostException) {
            Resource.Fail(ProcessStatus.NoInternet, e.message ?: e.toString())
        } catch (e: SocketTimeoutException) {
            Resource.Fail(ProcessStatus.TimeOut, e.message ?: e.toString())
        } catch (e: Exception) {
            Resource.Fail(ProcessStatus.Fail, e.message ?: e.toString())
        }
    }

}