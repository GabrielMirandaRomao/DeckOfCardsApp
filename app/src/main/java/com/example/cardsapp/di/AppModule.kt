package com.example.cardsapp.di

import com.example.cardsapp.data.remote.RetrofitClient
import com.example.cardsapp.repository.DeckRepository
import com.example.cardsapp.repository.DeckService
import com.example.cardsapp.ui.black_jack.BlackJackViewModel
import com.example.cardsapp.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DependencyModule {
    val appModule = module {
        single { RetrofitClient.createService }
        single { DeckService(get()) }
        single { DeckRepository(get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { BlackJackViewModel(get()) }
    }
}