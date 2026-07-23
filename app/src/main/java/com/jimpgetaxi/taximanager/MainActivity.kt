package com.jimpgetaxi.taximanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jimpgetaxi.taximanager.ui.AddRideScreen
import com.jimpgetaxi.taximanager.ui.HistoryScreen
import com.jimpgetaxi.taximanager.ui.HomeScreen
import com.jimpgetaxi.taximanager.ui.SplashScreen
import com.jimpgetaxi.taximanager.ui.MainViewModel
import com.jimpgetaxi.taximanager.ui.PlaceholderScreen
import com.jimpgetaxi.taximanager.ui.ShiftDetailScreen
import com.jimpgetaxi.taximanager.ui.components.AmbientBackground
import com.jimpgetaxi.taximanager.ui.components.BottomNavBar
import com.jimpgetaxi.taximanager.ui.theme.TaxiManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TaxiManagerTheme {
                val navController = rememberNavController()
                val viewModel: MainViewModel = hiltViewModel()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                val mainTabs = listOf("home", "analytics", "shifts", "notifications", "profile")
                val showBottomBar = currentRoute in mainTabs

                val selectedTab = mainTabs.indexOf(currentRoute).coerceAtLeast(0)

                var showSplash by remember { mutableStateOf(true) }

                Box(modifier = Modifier.fillMaxSize()) {
                    // Ambient gradient background
                    AmbientBackground()

                    if (showSplash) {
                        SplashScreen { showSplash = false }
                    } else {
                        Scaffold(
                        containerColor = Color.Transparent,
                        bottomBar = {
                            if (showBottomBar) {
                                BottomNavBar(
                                    selectedTab = selectedTab,
                                    onTabSelected = { index ->
                                        val route = mainTabs[index]
                                        if (route != currentRoute) {
                                            navController.navigate(route) {
                                                popUpTo("home") { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable("home") {
                                HomeScreen(
                                    viewModel = viewModel,
                                    onNavigateToAddRide = { navController.navigate("add_ride") }
                                )
                            }
                            composable("add_ride") {
                                AddRideScreen(
                                    viewModel = viewModel,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable("analytics") {
                                PlaceholderScreen(title = "Στατιστικά")
                            }
                            composable("shifts") {
                                HistoryScreen(
                                    viewModel = viewModel,
                                    onNavigateToShiftDetail = { shiftId -> navController.navigate("shift_detail/$shiftId") }
                                )
                            }
                            composable(
                                route = "shift_detail/{shiftId}",
                                arguments = listOf(navArgument("shiftId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val shiftId = backStackEntry.arguments?.getInt("shiftId") ?: return@composable
                                ShiftDetailScreen(
                                    shiftId = shiftId,
                                    viewModel = viewModel,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable("notifications") {
                                PlaceholderScreen(title = "Ειδοποιήσεις")
                            }
                            composable("profile") {
                                PlaceholderScreen(title = "Προφίλ")
                            }
                        }
                    }
                }
            }
        }
    }
}
}
