package com.example.tp_flashcard

import ads_mobile_sdk.h5
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tp_flashcard.model.FlashCardCategory
import com.example.tp_flashcard.ui.theme.Purple80


@Composable
fun FlashcardNavHost(
    modifier: Modifier = Modifier,
    homeViewModel : HomeViewModel,
    flashcardViewModel : FlashcardViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            HomeScreen(
                homeViewModel = homeViewModel,
                onCategoryClick = {  }
            )
        }
        composable("flashcard") {
            FlashcardScreen(
                flashcardViewModel = flashcardViewModel,
                onSessionFinished = {  }
            )
        }
    }
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onCategoryClick: (FlashCardCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories by homeViewModel.categories.collectAsState()

    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(categories) { category ->
            CategoryDisplay(category = category,
                onFlashcardClick = onCategoryClick)
        }
    }
}

@Composable
fun CategoryDisplay(
    category: FlashCardCategory,
    onFlashcardClick: (FlashCardCategory) -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {onFlashcardClick(category)},
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ){
        Text(
            text = category.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun FlashcardScreen(
    flashcardViewModel: FlashcardViewModel,
    onSessionFinished: () -> Unit
) {
    val state by flashcardViewModel.uiState.collectAsState()

    if (state.isFinished) {
        onSessionFinished()
        return
    }

    val currentCard = state.cards.getOrNull(state.currentIndex) ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LinearProgressIndicator(
            progress = { (state.currentIndex + 1).toFloat() / state.cards.size },
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = currentCard.question,
            //style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(32.dp)
        )

        Button(
            onClick = { flashcardViewModel.nextCard() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Suivant")
        }
    }
}
