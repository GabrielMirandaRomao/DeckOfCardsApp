package com.example.cardsapp.ui.black_jack

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardsapp.MyApplication
import com.example.cardsapp.data.entity.BlackjackDeck
import com.example.cardsapp.data.entity.Deck
import com.example.cardsapp.repository.DeckRepository
import com.example.cardsapp.utils.ProcessStatus
import com.example.cardsapp.utils.Resource
import kotlinx.coroutines.launch

class BlackJackViewModel(
    private val deckRepository: DeckRepository
) : ViewModel() {

    private val _dealerCards = mutableStateOf<List<BlackjackDeck>>(mutableListOf())
    val dealerCards: State<List<BlackjackDeck>> = _dealerCards

    private val _playerCards = mutableStateOf<List<BlackjackDeck>>(mutableListOf())
    val playerCards: State<List<BlackjackDeck>> = _playerCards

    private var _deck = MutableLiveData<Resource<Deck>>()
    val deck: LiveData<Resource<Deck>> = _deck

    private var _alert = MutableLiveData<String?>()
    var alert: LiveData<String?> = _alert

    fun getShuffleDeck() {
        if (MyApplication.hasNetwork()) {
            viewModelScope.launch {
                val resource = deckRepository.getShuffleDeck(BLACK_JACK_DECK)

                if (resource is Resource.Success) {
                    _deck.value = resource
                    getDrawCards(resource.data.id)
                } else if (resource is Resource.Fail) {
                    _deck.value = resource
                    handleFailure(resource)
                }
            }
        } else {
            _alert.value = "Error no network"
        }
    }

    private fun getDrawCards(deckId: String) {
        if (MyApplication.hasNetwork()) {
            viewModelScope.launch {
                val resource = deckRepository.getDrawCards(deckId, START_GAME)

                if (resource is Resource.Success) {
                    val dealerCards = mutableListOf<BlackjackDeck>()
                    val playerCards = mutableListOf<BlackjackDeck>()

                    resource.data.cards.forEach { card ->
                        val value = when (card.value) {
                            KING, QUEEN, JACK, VALUE_TEN_STRING -> VALUE_TEN
                            ACE -> VALUE_ACE
                            else -> card.value.toInt()
                        }

                        if (dealerCards.size == 2) {
                            playerCards.add(BlackjackDeck(png = card.images.png, value = value))
                        } else {
                            dealerCards.add(BlackjackDeck(png = card.images.png, value = value))
                        }
                    }

                    _playerCards.value = playerCards
                    _dealerCards.value = dealerCards

                } else if (resource is Resource.Fail) {
                    handleFailure(resource)
                }
            }
        } else {
            _alert.value = "Error no network"
        }
    }

    fun getOneMoreCard(deckId: String) {
        if (MyApplication.hasNetwork()) {
            viewModelScope.launch {
                val resource = deckRepository.getDrawCards(deckId, ONE_MORE)

                if (resource is Resource.Success) {
                    val updatedCards = _playerCards.value +
                            resource.data.cards.map { card ->
                                val value = when (card.value) {
                                    KING, QUEEN, JACK, VALUE_TEN_STRING -> VALUE_TEN
                                    ACE -> VALUE_ACE
                                    else -> card.value.toInt()
                                }
                                BlackjackDeck(
                                    png = card.images.png,
                                    value = value
                                )
                            }

                    _playerCards.value = updatedCards

                } else if (resource is Resource.Fail) {
                    handleFailure(resource)
                }
            }
        } else {
            _alert.value = "Error no network"
        }
    }

    private fun handleFailure(resource: Resource.Fail) {
        when (resource.status) {
            ProcessStatus.NoInternet -> _alert.value = "Error no internet ${resource.message}"

            ProcessStatus.TimeOut -> _alert.value = "Time out ${resource.message}"

            else -> {
                _alert.value = "Fail ${resource.message}"
            }
        }
    }

    fun restartGame() {
        _dealerCards.value = emptyList()
        _playerCards.value = emptyList()
    }

    companion object {
        const val BLACK_JACK_DECK = 6
        const val START_GAME = 4
        const val ONE_MORE = 1
        const val KING = "KING"
        const val JACK = "JACK"
        const val QUEEN = "QUEEN"
        const val ACE = "ACE"
        const val VALUE_TEN = 10
        const val VALUE_ACE = 11
        const val VALUE_TEN_STRING = "10"
        const val PLAYER_WIN = "Player win"
        const val DEALER_WIN = "Dealer win"
    }
}