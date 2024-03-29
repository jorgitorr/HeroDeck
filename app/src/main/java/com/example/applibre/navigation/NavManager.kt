package com.example.applibre.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.applibre.ui.model.HeroDeckViewModel
import com.example.applibre.ui.model.LoginViewModel
import com.example.applibre.ui.view.TipoPartida
import com.example.applibre.ui.view.components.Search
import com.example.applibre.ui.view.principal.Favoritos
import com.example.applibre.ui.view.principal.HeroDetailView
import com.example.applibre.ui.view.principal.Screen
import com.example.applibre.ui.view.login.Login
import com.example.applibre.ui.view.login.Register
import com.example.applibre.ui.view.principal.HeroDetailViewFav

/**
 * Nav Manager contiene todas las posibles pantallas que vamos a usar en la aplicacion
 * con ello podemos movernos de una a otra pantalla
 * @param heroDeckViewModel es el viewmodel del superheroe
 * @param loginViewModel login viewmodel
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavManager(heroDeckViewModel: HeroDeckViewModel, loginViewModel: LoginViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "TipoPartida") {
        composable("TipoPartida"){
            TipoPartida(navController = navController)
        }
        composable("CartasJugador"){
            Screen(heroDeckViewModel = heroDeckViewModel, navController)
        }
        composable("Login"){
            Login(loginViewModel = loginViewModel, navController)
        }
        composable("HeroDetail/{id}", arguments = listOf(
            navArgument("id") { type = NavType.StringType }//el fallo era en el tipo de NavType :_(
        )  ){
            val id = it.arguments?.getString("id") ?: 0
            HeroDetailView(heroDeckViewModel = heroDeckViewModel, navController = navController,
                id.toString())
        }
        composable("HeroDetailFav/{id}", arguments = listOf(
            navArgument("id") { type = NavType.StringType }//el fallo era en el tipo de NavType :_(
        )  ){
            val id = it.arguments?.getString("id") ?: 0
            HeroDetailViewFav(heroDeckViewModel = heroDeckViewModel, navController = navController,
                id.toString())
        }
        composable("Registro"){
            Register(loginViewModel = loginViewModel, navController)
        }
        composable("Favoritos"){
            Favoritos(heroDeckViewModel, navController)
        }
        composable("Search"){
            Search(heroDeckViewModel, navController)
        }

    }
}

