package com.julietgisemba.kipesa.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.julietgisemba.kipesa.ui.screens.BudgetsScreen
import com.julietgisemba.kipesa.ui.screens.DashboardScreen
import com.julietgisemba.kipesa.ui.screens.GoalsScreen
import com.julietgisemba.kipesa.ui.screens.LoginScreen
import com.julietgisemba.kipesa.ui.screens.ProfileScreen
import com.julietgisemba.kipesa.ui.screens.SignUpScreen
import com.julietgisemba.kipesa.ui.screens.TransactionsScreen
import com.julietgisemba.kipesa.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier, viewModel: FinanceViewModel) {
    NavHost(navController = navController, startDestination = Destinations.Login.route, modifier = modifier) {
        composable(Destinations.Login.route) { LoginScreen(navController) }
        composable(Destinations.SignUp.route) { SignUpScreen(navController) }
        composable(Destinations.Dashboard.route) { DashboardScreen() }
        composable(Destinations.Transactions.route) { TransactionsScreen() }
        composable(Destinations.Budgets.route) { BudgetsScreen() }
        composable(Destinations.Goals.route) { GoalsScreen() }
        composable(Destinations.Profile.route) { ProfileScreen(navController) }
    }

}