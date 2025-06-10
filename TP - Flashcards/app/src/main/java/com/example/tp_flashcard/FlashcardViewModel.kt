package com.example.tp_flashcard

import androidx.lifecycle.ViewModel
import com.example.tp_flashcard.data.FlashcardRepository
import com.example.tp_flashcard.model.FlashcardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FlashcardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState

    fun loadCardsForCategory(categoryId: Int) {
        //récupère les cartes correspondant à la catégorie donnée
        val filtered = FlashcardRepository.flashcards.filter { it.categoryId == categoryId }
        _uiState.value = FlashcardUiState(currentIndex = 0, cards = filtered)
    }

    fun nextCard() {
        val state = _uiState.value
        //si pas dernière carte
        if (state.currentIndex < state.cards.lastIndex) {
            //on avance la carte et on la remet dans le bon sens
            _uiState.value = state.copy(currentIndex = state.currentIndex + 1, isCardFlipped = false)
        } else {
            //on indique si c'est fnit
            _uiState.value = state.copy(isFinished = true)
        }
    }

    fun flipCard(){
        //on retourne la carte
        _uiState.value = _uiState.value.copy(isCardFlipped = !_uiState.value.isCardFlipped)
    }
}
