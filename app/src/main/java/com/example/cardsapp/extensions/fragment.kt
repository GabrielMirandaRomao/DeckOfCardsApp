package com.example.cardsapp.extensions

import androidx.viewbinding.ViewBinding
import com.example.cardsapp.utils.FragmentViewBindingDelegate

inline fun <reified T : ViewBinding> viewBinding() =
    FragmentViewBindingDelegate(T::class.java)