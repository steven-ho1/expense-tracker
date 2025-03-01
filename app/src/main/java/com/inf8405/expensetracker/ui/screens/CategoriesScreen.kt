package com.inf8405.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inf8405.expensetracker.database.entities.CategoryEntity
import com.inf8405.expensetracker.models.MainViewModelsWrapper
import com.inf8405.expensetracker.ui.navigation.ExpenseTrackerScreen
import com.inf8405.expensetracker.utils.toColor
import com.inf8405.expensetracker.viewmodels.CategoryViewModel

/**
 * Écran principal pour afficher et gérer les catégories
 * @param mainViewModelsWrapper Conteneur de ViewModels pour accéder aux données
 * @param navController Contrôleur de navigation pour naviguer entre les écrans
 */
@Composable
fun CategoriesScreen(
    mainViewModelsWrapper: MainViewModelsWrapper,
    navController: NavController,
) {
    // État pour suivre l'onglet sélectionné (0 pour dépenses, 1 pour revenus)
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    // Récupération du ViewModel des catégories
    val categoryViewModel: CategoryViewModel = mainViewModelsWrapper.categoryViewModel

    // Observation des listes de catégories avec collectAsState()
    val expenseCategories by categoryViewModel.expenseCategories.collectAsState()
    val incomeCategories by categoryViewModel.incomeCategories.collectAsState()

    // Conteneur principal
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF)) // Couleur de fond verte foncée
        ) {
            // Barre d'onglets pour basculer entre dépenses et revenus
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFFFFFFF),

            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Dépenses") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Revenus") }
                )
            }

            // Zone de contenu qui affiche la grille de catégories
            Box(modifier = Modifier.weight(1f)) {
                CategoriesGrid(
                    // Affiche les catégories de dépenses ou de revenus selon l'onglet sélectionné
                    categories = if (selectedTab == 0) expenseCategories else incomeCategories,
                )
            }
        }

        // Bouton d'action flottant pour ajouter une nouvelle catégorie
        FloatingActionButton(
            onClick = {
                // Navigation vers l'écran d'ajout de catégorie
                navController.navigate(ExpenseTrackerScreen.AddCategory.name)
            },
            modifier = Modifier
                .scale(1f)
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

/**
 * Composant qui affiche une grille de catégories
 * @param categories Liste des catégories à afficher
 */
@Composable
fun CategoriesGrid(
    categories: List<CategoryEntity>,
) {
    // Grille verticale avec 3 colonnes
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Génère un élément pour chaque catégorie dans la liste
        items(categories) { category ->
            CategoryItem(
                category = category,
            )
        }
    }
}

/**
 * Élément individuel représentant une catégorie
 * @param category Entité de catégorie à afficher
 */
@Composable
fun CategoryItem(
    category: CategoryEntity,
) {
    // Disposition en colonne pour aligner l'icône et le texte
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        // Cercle coloré représentant la catégorie
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(category.color.toColor())
        )

        // Nom de la catégorie
        Text(
            text = category.name,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}