package com.example.tp_flashcard.data

import com.example.tp_flashcard.model.FlashCard
import com.example.tp_flashcard.model.FlashCardCategory

//singleton
object FlashcardRepository {
    val categories = listOf(
        FlashCardCategory(1, "Maths"),
        FlashCardCategory(2, "Histoire"),
        FlashCardCategory(3, "Blagues")
    )

    val flashcards = listOf(
        FlashCard(1, 1, "Combien font 6+9 ?", "15"),
        FlashCard(2, 1, "Dérivée de 6x² ?", "12x"),
        FlashCard(3, 2, "Qui est le meilleur élève de la classe ?", "Charly FLU"),
        FlashCard(4, 2, "Qui est le roi de france en 1789 ?", "Louis XVI"),
        FlashCard(5, 3, "Qu'est ce qui est pire qu'un bébé dans une poubelle ?", "Un bébé dans deux poubelles !"),
        FlashCard(6, 3, "Un poisson rentre dans un autre poisson, le poisson dit quoi ?", "Désolé, j'avais de l'eau dans les yeux !")

        )
}