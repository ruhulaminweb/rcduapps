package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.DonateScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val isFirebaseInitialized = remember { com.google.firebase.FirebaseApp.getApps(this@MainActivity).isNotEmpty() }

        if (!isFirebaseInitialized) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.errorContainer) {
                Box(contentAlignment = androidx.compose.ui.Alignment.Center, modifier = Modifier.padding(32.dp)) {
                    Text(
                        "Firebase is not initialized.\n\nPlease upload your google-services.json file to the 'app' folder and restart the app.",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            var isAuthenticated by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser != null) }

            if (!isAuthenticated) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    LoginScreen(onSignInSuccess = { isAuthenticated = true })
                }
            } else {
                val navController = rememberNavController()
                
                Scaffold(
              modifier = Modifier.fillMaxSize(),
              bottomBar = {
                NavigationBar {
                  val navBackStackEntry by navController.currentBackStackEntryAsState()
                  val currentDestination = navBackStackEntry?.destination

                  NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = currentDestination?.hierarchy?.any { it.route == "home" } == true,
                    onClick = {
                      navController.navigate("home") {
                        popUpTo(navController.graph.findStartDestination().id) {
                          saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                      }
                    }
                  )
                  NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = currentDestination?.hierarchy?.any { it.route == "profile" } == true,
                    onClick = {
                      navController.navigate("profile") {
                        popUpTo(navController.graph.findStartDestination().id) {
                          saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                      }
                    }
                  )
                  NavigationBarItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "Donate") },
                    label = { Text("Donate") },
                    selected = currentDestination?.hierarchy?.any { it.route == "donate" } == true,
                    onClick = {
                      navController.navigate("donate") {
                        popUpTo(navController.graph.findStartDestination().id) {
                          saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                      }
                    }
                  )
                }
              }
            ) { innerPadding ->
              NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
              ) {
                composable("home") { HomeScreen(viewModel) }
                composable("profile") { ProfileScreen(viewModel) }
                composable("donate") { DonateScreen() }
              }
            }
          }
        }
      }
    }
  }
}
