package com.house.keeper.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.house.keeper.ui.screens.home.HomeScreen
import com.house.keeper.ui.screens.record.RecordScreen
import com.house.keeper.ui.screens.statistics.StatisticsScreen
import com.house.keeper.ui.screens.settings.SettingsScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.Record.route) {
            RecordScreen(navController = navController)
        }
        
        composable(Screen.Statistics.route) {
            StatisticsScreen(navController = navController)
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: String) {
    object Home : Screen("home", "首页", "🏠")
    object Record : Screen("record", "记账", "➕")
    object Statistics : Screen("statistics", "统计", "📊")
    object Settings : Screen("settings", "设置", "⚙️")
    
    companion object {
        val bottomNavItems = listOf(Home, Record, Statistics, Settings)
    }
}
