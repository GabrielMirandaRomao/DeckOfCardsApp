package com.example.cardsapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardsapp.MyApplication
import com.example.cardsapp.data.entity.Deck
import com.example.cardsapp.data.entity.DrawCard
import com.example.cardsapp.repository.DeckRepository
import com.example.cardsapp.utils.ProcessStatus
import com.example.cardsapp.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel(
    private val deckRepository: DeckRepository
) : ViewModel() {

    private var _deck = MutableLiveData<Resource<Deck>>()
    val deck: LiveData<Resource<Deck>> = _deck

    private var _drawCards = MutableLiveData<Resource<DrawCard>>()
    val drawCards: LiveData<Resource<DrawCard>> = _drawCards

    private var _shuffledDeck = MutableLiveData<Resource<Deck>>()
    val shuffledDeck: LiveData<Resource<Deck>> = _shuffledDeck

    private var _alert = MutableLiveData<String?>()
    var alert: LiveData<String?> = _alert

    fun getShuffleDeck() {
        if (MyApplication.hasNetwork()) {
            viewModelScope.launch {
                val resource = deckRepository.getShuffleDeck(ONE_DECK)

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
            _drawCards.value = Resource.Processing
            viewModelScope.launch {
                val resource = deckRepository.getDrawCards(deckId, FULL_DECK)

                if (resource is Resource.Success) {
                    _drawCards.value = resource
                } else if (resource is Resource.Fail) {
                    _drawCards.value = resource
                    handleFailure(resource)
                }
            }
        } else {
            _alert.value = "Error no network"
        }
    }

    fun shuffleDeck(deckId: String) {
        if (MyApplication.hasNetwork()) {
            viewModelScope.launch {
                val resource = deckRepository.shuffleDeck(deckId)

                if (resource is Resource.Success) {
                    _shuffledDeck.value = resource
                    getDrawCards(deckId)
                } else if (resource is Resource.Fail) {
                    _shuffledDeck.value = resource
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

    companion object {
        const val ONE_DECK = 1
        const val FULL_DECK = 52
    }
}