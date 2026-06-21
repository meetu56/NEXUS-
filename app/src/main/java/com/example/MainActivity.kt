package com.example

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.NexusRepository
import com.example.ui.NexusViewModel
import com.example.ui.ViewModelFactory
import com.example.ui.screens.AgentConsoleScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DataTrackerScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SpaceNavy

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Supports borderless status bars and safe draw overlays
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val app = context.applicationContext as Application
                
                // Initialize database, repository, and view model using custom Factory
                val database = AppDatabase.getDatabase(app)
                val repository = NexusRepository(
                    database.memoryDao(),
                    database.taskDao(),
                    database.financeDao(),
                    database.agentLogDao()
                )
                
                val nexusViewModel: NexusViewModel = viewModel(
                    factory = ViewModelFactory(app, repository)
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NexusBottomBar(viewModel = nexusViewModel)
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SpaceNavy)
                            .padding(
                                top = innerPadding.calculateTopPadding(),
                                bottom = innerPadding.calculateBottomPadding()
                            )
                    ) {
                        when (nexusViewModel.currentTab) {
                            "dashboard" -> DashboardScreen(viewModel = nexusViewModel)
                            "console" -> AgentConsoleScreen(viewModel = nexusViewModel)
                            "trackers" -> DataTrackerScreen(viewModel = nexusViewModel)
                            "history" -> HistoryScreen(viewModel = nexusViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NexusBottomBar(viewModel: NexusViewModel) {
    NavigationBar(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = SpaceNavy,
        tonalElevation = NavigationBarDefaults.Elevation
    ) {
        NavigationBarItem(
            selected = viewModel.currentTab == "dashboard",
            onClick = { viewModel.currentTab = "dashboard" },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = com.example.ui.theme.CyberCyan,
                selectedTextColor = com.example.ui.theme.CyberCyan,
                indicatorColor = com.example.ui.theme.SpaceCard,
                unselectedIconColor = com.example.ui.theme.TextSecondary,
                unselectedTextColor = com.example.ui.theme.TextSecondary
            )
        )
        NavigationBarItem(
            selected = viewModel.currentTab == "console",
            onClick = { viewModel.currentTab = "console" },
            icon = { Icon(Icons.Default.Widgets, contentDescription = "Workspace") },
            label = { Text("Agents", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = com.example.ui.theme.CyberCyan,
                selectedTextColor = com.example.ui.theme.CyberCyan,
                indicatorColor = com.example.ui.theme.SpaceCard,
                unselectedIconColor = com.example.ui.theme.TextSecondary,
                unselectedTextColor = com.example.ui.theme.TextSecondary
            )
        )
        NavigationBarItem(
            selected = viewModel.currentTab == "trackers",
            onClick = { viewModel.currentTab = "trackers" },
            icon = { Icon(Icons.Default.Assignment, contentDescription = "Data Center") },
            label = { Text("Data Deck", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = com.example.ui.theme.CyberCyan,
                selectedTextColor = com.example.ui.theme.CyberCyan,
                indicatorColor = com.example.ui.theme.SpaceCard,
                unselectedIconColor = com.example.ui.theme.TextSecondary,
                unselectedTextColor = com.example.ui.theme.TextSecondary
            )
        )
        NavigationBarItem(
            selected = viewModel.currentTab == "history",
            onClick = { viewModel.currentTab = "history" },
            icon = { Icon(Icons.Default.History, contentDescription = "Archive") },
            label = { Text("Archive", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = com.example.ui.theme.CyberCyan,
                selectedTextColor = com.example.ui.theme.CyberCyan,
                indicatorColor = com.example.ui.theme.SpaceCard,
                unselectedIconColor = com.example.ui.theme.TextSecondary,
                unselectedTextColor = com.example.ui.theme.TextSecondary
            )
        )
    }
}
