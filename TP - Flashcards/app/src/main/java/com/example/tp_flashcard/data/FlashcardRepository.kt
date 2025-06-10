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
        FlashCard(3, 1, "A partir de quel taille d'échantillan la loi normal peut elle être appliqué ?", "~30"),
        FlashCard(4, 1, "Combien de faces a un cube ?", "6"),
        

        FlashCard(5, 2, "Qui est le meilleur élève de la classe ?", "Charly FLU"),
        FlashCard(6, 2, "Qui est le roi de france en 1789 ?", "Louis XVI"),
        FlashCard(7, 2, "Qui a écrit la Déclaration des droits de l'homme et du citoyen ?", "L'Assemblée nationale constituante"),
        FlashCard(8, 2, "Qui a découvert l'Amérique ?", "Christophe Colomb"),

        FlashCard(9, 3, "Qu'est ce qui est pire qu'un bébé dans une poubelle ?", "Un bébé dans deux poubelles !"),
        FlashCard(10, 3, "Un poisson rentre dans un autre poisson, le poisson dit quoi ?", "Désolé, j'avais de l'eau dans les yeux !"),
        FlashCard(11, 3, "Pourquoi les plongeurs plongent-ils toujours en arrière et jamais en avant ?", "Parce que sinon ils tombent dans le bateau !"),

        )
}