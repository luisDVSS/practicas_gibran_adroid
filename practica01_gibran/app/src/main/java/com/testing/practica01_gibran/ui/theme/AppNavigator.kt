package com.testing.practica01_gibran.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.testing.practica01_gibran.FirstScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "first_screen") {
        composable("first_screen") {
            FirstScreen(onNavigateToSecondScreen = { texto -> navController.navigate("second_screen/$texto") })

        }
        composable (
            route = "second_screen/{texto}",
            arguments = listOf(navArgument("texto"){type = NavType.StringType})
            ){ backStackEntry ->
            val textoRecibido = backStackEntry.arguments?.getString("texto") ?: ""
            SecondScreen(
                mensaje = textoRecibido,
                onBack = { navController.popBackStack() } // Esto cierra la pantalla actual y vuelve
            )
        }

        }

    }

