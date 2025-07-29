package com.house.keeper.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.house.keeper.ui.screens.calendar.CalendarScreen
import com.house.keeper.ui.screens.home.HomeScreen
import com.house.keeper.ui.screens.record.EditTransactionScreen
import com.house.keeper.ui.screens.record.RecordScreen
import com.house.keeper.ui.screens.statistics.StatisticsScreen
import com.house.keeper.ui.screens.settings.SettingsScreen
import com.house.keeper.ui.screens.transactions.TransactionListScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.Record.route) {
            RecordScreen(navController = navController)
        }

        composable(
            route = "edit_transaction/{transactionId}",
            arguments = listOf(navArgument("transactionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: 0L
            EditTransactionScreen(
                navController = navController,
                transactionId = transactionId
            )
        }
        
        composable(Screen.Statistics.route) {
            StatisticsScreen(navController = navController)
        }

        composable(Screen.Transactions.route) {
            TransactionListScreen(navController = navController)
        }

        composable(Screen.Calendar.route) {
            CalendarScreen(navController = navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: String) {
    object Home : Screen("home", "首页", "🏠")
    object Record : Screen("record", "记账", "➕")
    object Transactions : Screen("transactions", "账单", "📋")
    object Calendar : Screen("calendar", "日历", "📅")
    object Statistics : Screen("statistics", "统计", "📊")
    object Settings : Screen("settings", "设置", "⚙️")

    companion object {
        val bottomNavItems = listOf(Home, Record, Transactions, Calendar, Statistics)
    }
}
