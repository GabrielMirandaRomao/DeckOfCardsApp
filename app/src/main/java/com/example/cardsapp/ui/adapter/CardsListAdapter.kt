package com.example.cardsapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardsapp.databinding.ItemCardBinding

class CardsListAdapter() : RecyclerView.Adapter<CardsListAdapter.CardViewHolder>() {

    private var deckList = emptyList<String>()

    class CardViewHolder(val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val item = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(item)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(ivCard.context)
                .load(deckList[position])
                .into(ivCard)
        }
    }

    override fun getItemCount(): Int {
        return deckList.size
    }

    fun updateDeckList(deckList: List<String>) {
        this.deckList = deckList
        notifyDataSetChanged()
    }

}