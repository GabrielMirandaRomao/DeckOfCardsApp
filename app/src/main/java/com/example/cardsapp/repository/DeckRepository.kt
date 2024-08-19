package com.example.cardsapp.repository

import com.example.cardsapp.data.entity.Card
import com.example.cardsapp.data.entity.Deck
import com.example.cardsapp.data.entity.DrawCard
import com.example.cardsapp.data.entity.ImageCard
import com.example.cardsapp.repository.dto.DrawCardDto
import com.example.cardsapp.repository.dto.ShuffleDeckDto
import com.example.cardsapp.utils.Resource

class DeckRepository(
    private val service: DeckService
) {

    suspend fun getShuffleDeck(deckCount: Int): Resource<Deck> {
        val resource = service.getShuffleDeck(deckCount)

        return if (resource is Resource.Success) {
            Resource.Success(deckDtoToDeck(resource.data))
        } else {
            resource as Resource.Fail
        }
    }

    suspend fun getDrawCards(deckId : String, cardsCount: Int) : Resource<DrawCard> {
        val resource = service.getDrawCards(deckId, cardsCount)

        return if (resource is Resource.Success) {
            Resource.Success(drawCardsDtoToDrawCards(resource.data))
        } else {
            resource as Resource.Fail
        }
    }

    suspend fun shuffleDeck(deckId: String) : Resource<Deck> {
        val resource = service.shuffleDeck(deckId)

        return if (resource is Resource.Success) {
            Resource.Success(deckDtoToDeck(resource.data))
        } else {
            resource as Resource.Fail
        }
    }

    private fun drawCardsDtoToDrawCards(drawCardsDto: DrawCardDto): DrawCard {
        return DrawCard(
            success = drawCardsDto.success,
            deckId = drawCardsDto.id,
            cards = drawCardsDto.cards.map { cardDto ->
                Card(
                    code = cardDto.code,
                    image = cardDto.image,
                    images = ImageCard(
                        svg = cardDto.images.svg,
                        png = cardDto.images.png
                    ),
                    value = cardDto.value,
                    suit = cardDto.suit
                )
            },
            remaining = drawCardsDto.remaining
        )
    }

    private fun deckDtoToDeck(deckDto: ShuffleDeckDto): Deck {
        return Deck(
            id = deckDto.id,
            success = deckDto.success,
            shuffled = deckDto.shuffled,
            remaining = deckDto.remaining
        )
    }
}