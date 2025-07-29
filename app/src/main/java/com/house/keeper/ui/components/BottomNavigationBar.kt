package com.house.keeper.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.house.keeper.ui.navigation.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        Screen.bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Text(
                        text = screen.icon,
                        fontSize = 20.sp,
                        modifier = Modifier.size(24.dp),
                        textAlign = TextAlign.Center
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        fontSize = 12.sp
                    )
                },
                selected = when (screen.route) {
                    Screen.Record.route -> {
                        // 记账页面需要匹配带参数的路由
                        currentRoute == screen.route || currentRoute?.startsWith("${screen.route}?") == true
                    }
                    else -> currentRoute == screen.route
                },
                onClick = {
                    val isCurrentlySelected = when (screen.route) {
                        Screen.Record.route -> {
                            currentRoute == screen.route || currentRoute?.startsWith("${screen.route}?") == true
                        }
                        else -> currentRoute == screen.route
                    }

                    if (!isCurrentlySelected) {
                        navController.navigate(screen.route) {
                            // 清除回退栈到首页，避免栈积累
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            // 避免重复创建相同的destination
                            launchSingleTop = true
                            // 恢复状态
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
