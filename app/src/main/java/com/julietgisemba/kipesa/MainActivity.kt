package com.julietgisemba.kipesa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.julietgisemba.kipesa.navigation.AppNavHost
import com.julietgisemba.kipesa.navigation.Destinations
import com.julietgisemba.kipesa.ui.components.BottomBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val showBottomBar = currentRoute !in listOf(
                Destinations.Login.route,
                Destinations.SignUp.route
            )

            val items = listOf(
                Destinations.Dashboard,
                Destinations.Transactions,
                Destinations.Budgets,
                Destinations.Goals,
                Destinations.Profile
            )

            Scaffold(
                bottomBar = {
                    if (showBottomBar) {
                        BottomBar(navController = navController, destinations = items)
                    }
                }
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    hiltViewModel()
                )
            }
        }
    }
}
