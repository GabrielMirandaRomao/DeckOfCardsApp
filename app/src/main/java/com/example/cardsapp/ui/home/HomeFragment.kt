package com.example.cardsapp.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cardsapp.R
import com.example.cardsapp.configuration.SharedPreferencesManager
import com.example.cardsapp.configuration.SharedPreferencesManager.Companion.DECK_ID
import com.example.cardsapp.databinding.FragmentHomeBinding
import com.example.cardsapp.extensions.hide
import com.example.cardsapp.extensions.show
import com.example.cardsapp.extensions.viewBinding
import com.example.cardsapp.ui.adapter.CardsListAdapter
import com.example.cardsapp.utils.Resource
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: HomeViewModel by viewModel()
    private val adapter: CardsListAdapter by lazy { CardsListAdapter() }
    private lateinit var sharedPreferencesManager : SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        observe()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val rvDeckCards = binding.rvDeckCards
        rvDeckCards.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        rvDeckCards.adapter = adapter
        rvDeckCards.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 500
        }
    }

    private fun setListener() {
        with(binding) {
            btGetDeck.setOnClickListener {
                viewModel.getShuffleDeck()
            }

            btGetShuffled.setOnClickListener {
                viewModel.shuffleDeck(sharedPreferencesManager.getDeckId(DECK_ID).orEmpty())
            }

            btPlayBlackjack.setOnClickListener {
                findNavController().navigate(R.id.composeBlackJackFragment)
            }
        }
    }

    private fun observe() {
        viewModel.deck.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.pbLoading.hide()
                    useSharedPreference(response.data.id)
                    binding.btGetShuffled.show()
                    binding.tvDeckId.show()
                    binding.tvDeckId.text = getString(R.string.deck_id, response.data.id)
                }

                is Resource.Fail -> {
                    binding.pbLoading.hide()
                    makeAlert(response.message)
                }

                is Resource.Processing -> {
                    binding.pbLoading.show()
                }
            }
        }

        viewModel.drawCards.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.pbLoading.hide()
                    val deckList = response.data.cards.map { it.images.png }
                    adapter.updateDeckList(deckList)
                }

                is Resource.Fail -> {
                    binding.pbLoading.hide()
                    makeAlert(response.message)
                }

                is Resource.Processing -> {
                    binding.pbLoading.show()
                }
            }
        }

        viewModel.shuffledDeck.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    with(binding) {
                        useSharedPreference(response.data.id)
                        pbLoading.hide()
                        rvDeckCards.show()
                        btGetShuffled.show()
                        tvDeckId.show()
                        tvDeckId.text = getString(R.string.deck_id, response.data.id)
                    }
                }

                is Resource.Fail -> {
                    binding.pbLoading.hide()
                    binding.rvDeckCards.hide()
                    makeAlert(response.message)
                }

                is Resource.Processing -> {
                    binding.rvDeckCards.hide()
                    binding.pbLoading.show()
                }
            }
        }

        viewModel.alert.observe(viewLifecycleOwner) { alert ->
            with(binding) {
                pbLoading.hide()
                rvDeckCards.hide()
            }
            makeAlert(alert)
        }
    }

    private fun makeAlert(alert: String?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setTitle("Something went wrong")
            .setMessage(alert)
            .setPositiveButton("Try again") { _, _ ->
                viewModel.getShuffleDeck()
            }
            .setNegativeButton("Close") { _, _ -> }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun useSharedPreference(deckId: String) {
        if (sharedPreferencesManager.getDeckId(DECK_ID)?.isNotBlank() == false) {
            sharedPreferencesManager.saveDeckId(DECK_ID, deckId)
        } else {
            sharedPreferencesManager.remove(DECK_ID)
            sharedPreferencesManager.saveDeckId(DECK_ID, deckId)
        }
    }
}