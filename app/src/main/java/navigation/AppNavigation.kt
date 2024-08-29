package navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import at.bachelor.workoutcounter.NavRoutes
import at.bachelor.workoutcounter.repository.MetaMotionRepository
import at.bachelor.workoutcounter.screens.collectedDataScreen.CollectedDataScreen
import at.bachelor.workoutcounter.screens.dataCollectionScreen.DataCollectionScreen
import at.bachelor.workoutcounter.screens.dataCollectionScreen.DataCollectionViewModel
import at.bachelor.workoutcounter.screens.screens.homeScreen.HomeScreen
import at.bachelor.workoutcounter.screens.trainingScreen.TrainingScreen

enum class MainNavOption {
    REGISTRATION,
    LOGIN,
    HOME,
    TRAINING,
    DATA_COLLECTION,
    COLLECTED_DATA,
    PAST_WORKOUTS,
    STATISTICS,
    SETTINGS,
    PROFILE
}


fun NavGraphBuilder.mainGraph(
    drawerState: DrawerState,
    navController: NavHostController,
    dataCollectionViewModel: DataCollectionViewModel,
    metaMotionRepository: MetaMotionRepository
) {
    navigation(startDestination = MainNavOption.HOME.name, route = NavRoutes.HomeRoute.name) {

        composable(MainNavOption.PROFILE.name) {
            Text(text = "This is Profile")
        }

        composable(MainNavOption.HOME.name) {
            HomeScreen(drawerState = drawerState, navController)
        }
        composable(MainNavOption.TRAINING.name) {
            TrainingScreen(
                dataCollection = dataCollectionViewModel,
                metaMotionRepository = metaMotionRepository
            )
        }
        composable(MainNavOption.DATA_COLLECTION.name) {
            DataCollectionScreen(
                viewModel = dataCollectionViewModel,
                metaMotionRepository,
                drawerState = drawerState
            )
        }
        composable(MainNavOption.COLLECTED_DATA.name) {
            CollectedDataScreen(drawerState=drawerState)

        }
        composable(MainNavOption.STATISTICS.name) {
            Text(text = "This is Statistics")
        }
        composable(MainNavOption.PAST_WORKOUTS.name) {
            Text(text = "This is Past Workouts")
        }

    }
}