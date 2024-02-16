package com.example.applibre.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.applibre.ui.model.HeroDeckViewModel
import com.example.applibre.ui.view.ContentDetailView
import com.example.applibre.ui.view.Pantalla

@Composable
fun NavManager(viewModel: HeroDeckViewModel){
    // DCS - Crea y recuerda una instancia del NavController, que se utiliza para navegar entre composables.
    val navController = rememberNavController()

    // DCS - Define el NavHost, que es el contenedor de navegación que gestiona los composables de destino.
    NavHost(navController = navController, startDestination = "Pantalla") {
        // DCS - Define un destino composable para la pantalla de inicio.
        composable("Pantalla") {
            // DCS - Llama al composable HomeView, pasando el viewModel y navController como parámetros.
            Pantalla(navController)
        }
        // DCS - Define un destino composable para la pantalla de detalles del juego, incluyendo un argumento dinámico.
        composable("HeroDetail/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType } // DCS - Define el argumento "id" que se espera sea un entero.
        )  ){
            // DCS - Recupera el argumento "id" del destino actual y lo pasa al composable DetailView.
            val id = it.arguments?.getInt("id") ?: 0 // DCS - Obtiene el "id" del juego o 0 si no se encuentra.
            //DetailView(viewModel, navController, id)
            ContentDetailView(superHero = viewModel.character)
        }
    }

}