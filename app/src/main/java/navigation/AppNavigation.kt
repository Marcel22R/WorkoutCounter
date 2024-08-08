package navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import at.bachelor.workoutcounter.NavRoutes
import at.bachelor.workoutcounter.homeScreen.HomeScreen
import at.bachelor.workoutcounter.repository.MetaMotionRepository
import at.bachelor.workoutcounter.trainingScreen.TrainingScreen
import at.bachelor.workoutcounter.trainingScreen.TrainingViewModel

enum class MainNavOption {
    REGISTRATION,
    LOGIN,
    HOME,
    TRAINING,
    DATA_COLLECTION,
    PAST_WORKOUTS,
    STATISTICS,
    SETTINGS,
    PROFILE
}


fun NavGraphBuilder.mainGraph(
    drawerState: DrawerState,
    navController: NavHostController,
    viewModel: TrainingViewModel,
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
                trainingViewModel = viewModel,
                startAccelerometer = { metaMotionRepository.startAccelerometer() },
                stopAccelerometer = { metaMotionRepository.stopAccelerometer() })
        }
        composable(MainNavOption.DATA_COLLECTION.name){
            Text(text = "This is Data Collection")
        }
        composable(MainNavOption.STATISTICS.name) {
            Text(text = "This is Statistics")
        }
        composable(MainNavOption.PAST_WORKOUTS.name) {
            Text(text = "This is Past Workouts")
        }

    }
}