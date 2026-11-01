package com.example.miniproy3

import com.example.miniproy3.data.AppDatabase
import com.example.miniproy3.data.PublicacionRepository
import com.example.miniproy3.ui.theme.PublicacionScreen
import com.example.miniproy3.ui.theme.PublicacionViewModel
import com.example.miniproy3.ui.theme.PublicacionViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface


class MainActivity : ComponentActivity() {

    private val viewModel: PublicacionViewModel by viewModels {

        val database = AppDatabase.getDatabase(
            applicationContext
        )

        val repository = PublicacionRepository(
            database.publicacionDao()
        )

        PublicacionViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MaterialTheme {

                Surface {

                    PublicacionScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
