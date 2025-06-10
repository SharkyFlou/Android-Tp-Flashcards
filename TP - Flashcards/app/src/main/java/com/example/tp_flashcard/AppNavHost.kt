package com.example.tp_flashcard

import ads_mobile_sdk.h5
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tp_flashcard.model.FlashCardCategory
import com.example.tp_flashcard.ui.theme.Purple80
import kotlinx.coroutines.launch


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
                onCategoryClick = { category ->
                    flashcardViewModel.loadCardsForCategory(category.id)
                    navController.navigate("flashcard"){
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable("flashcard") {
            FlashcardScreen(
                flashcardViewModel = flashcardViewModel,
                onSessionFinished = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Choisissez une catégorie",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(categories) { category ->
                    CategoryDisplay(
                        category = category,
                        onFlashcardClick = onCategoryClick
                    )
                }
            }
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
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }

}

@OptIn(ExperimentalAnimationApi::class)
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

    //récupère la carte actuelle, sinon affiche rien
    val currentCard = state.cards.getOrNull(state.currentIndex) ?: return
    val scope = rememberCoroutineScope()
    val rotationY = remember { Animatable(0f) }

    LaunchedEffect(state.currentIndex) {
        rotationY.snapTo(0f)
    }

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

        AnimatedContent(
            targetState = state.currentIndex to currentCard,
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }
                ) + fadeIn() togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth }
                        ) + fadeOut()
            },
            label = "CardContent"
        ) { (currentIndex, card) ->
            // change le texte quand plus que 90°
            val isFront = rotationY.value <= 90f
            val content = if (isFront) card.question else card.answer
            //permet de ne pas retourner le texte
            val textRotation = if (isFront) 0f else 180f

            FlashcardText(
                text = content,
                rotationY = rotationY.value,
                textRotationY = textRotation, // Pass this to FlashcardText
                modifier = Modifier
                    .padding(32.dp)
                    .clickable {
                        scope.launch {
                            val target = if (rotationY.value < 90f) 180f else 0f
                            rotationY.animateTo(target, animationSpec = tween(durationMillis = 400))
                            flashcardViewModel.flipCard()
                        }
                    },
            )
        }


        Button(
            onClick = { flashcardViewModel.nextCard() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Suivant")
        }
    }
}

@Composable
fun FlashcardText(
    text: String,
    rotationY: Float,
    textRotationY: Float = 0f, // Add this parameter
    modifier: Modifier = Modifier,
) {
    val density = androidx.compose.ui.platform.LocalDensity.current.density

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(16.dp)
            .graphicsLayer {
                this.rotationY = rotationY
                cameraDistance = 8 * density
            }
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(24.dp)
                .graphicsLayer {
                    this.rotationY = textRotationY // Flip text back if needed
                }
        )
    }
}