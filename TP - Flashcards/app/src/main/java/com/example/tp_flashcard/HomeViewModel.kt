package com.example.tp_flashcard

import androidx.lifecycle.ViewModel
import com.example.tp_flashcard.data.FlashcardRepository
import com.example.tp_flashcard.model.FlashCardCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<FlashCardCategory>>(emptyList())
    val categories: StateFlow<List<FlashCardCategory>> = _categories

    init {
        _categories.value = FlashcardRepository.categories
    }
}
