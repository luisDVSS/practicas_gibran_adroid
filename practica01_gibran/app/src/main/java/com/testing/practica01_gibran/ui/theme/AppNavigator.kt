package com.testing.practica01_gibran.ui.theme

import android.R.attr.type
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.testing.practica01_gibran.FirstScreen
class AppNavigator {

}

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
            ){backStackEntry ->
            val  textoRecibido = backStackEntry.arguments?.getString("texto")?:""
            SecondScreen(mensaje = textoRecibido)
        }

        }

    }

