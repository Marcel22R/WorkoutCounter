package navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import at.bachelor.workoutcounter.NavRoutes
import at.bachelor.workoutcounter.homeScreen.HomeScreen

enum class MainNavOption {
    REGISTRATION,
    LOGIN,
    HOME,
    TRAINING,
    STATISTICS,
    SETTINGS,
    PROFILE
}


fun NavGraphBuilder.mainGraph(drawerState: DrawerState) {
    navigation(startDestination = MainNavOption.HOME.name, route = NavRoutes.HomeRoute.name) {
        composable(MainNavOption.HOME.name) {
            HomeScreen(drawerState = drawerState)
        }
        composable(MainNavOption.TRAINING.name) {
            Text(text = "This is Training")
        }
        composable(MainNavOption.STATISTICS.name) {
            Text(text = "This is Statistics")
        }


    }
}