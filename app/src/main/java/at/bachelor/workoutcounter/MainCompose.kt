package at.bachelor.workoutcounter

import AppDrawerTheme
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import at.bachelor.workoutcounter.app.ui.components.appdrawer.AppDrawerContent
import at.bachelor.workoutcounter.app.ui.components.appdrawer.AppDrawerItemInfo
import at.bachelor.workoutcounter.repository.MetaMotionRepository
import at.bachelor.workoutcounter.screens.dataCollectionScreen.DataCollectionViewModel
import navigation.MainNavOption
import navigation.mainGraph


@Composable
fun MainCompose(
    dataCollectionViewModel: DataCollectionViewModel,
    metaMotionRepository: MetaMotionRepository,
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
) {
    AppDrawerTheme {
        Surface {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    AppDrawerContent(
                        drawerState = drawerState,
                        menuItems = DrawerParams.drawerButtons,
                        defaultPick = MainNavOption.HOME
                    ) { onUserPickedOption ->
                        when (onUserPickedOption) {
                            MainNavOption.HOME -> navController.navigate(onUserPickedOption.name) {
                                popUpTo(
                                    NavRoutes.HomeRoute.name
                                )
                            }

                            MainNavOption.TRAINING -> navController.navigate(onUserPickedOption.name) {
                                popUpTo(
                                    NavRoutes.TrainingRoute.name
                                )
                            }

                            MainNavOption.DATA_COLLECTION -> navController.navigate(
                                onUserPickedOption.name
                            ) {
                                popUpTo(
                                    NavRoutes.DataCollectionRoute.name
                                )
                            }

                            MainNavOption.COLLECTED_DATA -> navController.navigate(
                                onUserPickedOption.name
                            ) {
                                popUpTo(
                                    NavRoutes.CollectedDataRoute.name
                                )
                            }


                            MainNavOption.PAST_WORKOUTS -> navController.navigate(onUserPickedOption.name) {
                                popUpTo(NavRoutes.PastWorkoutsRoute.name)
                            }

                            MainNavOption.STATISTICS -> navController.navigate(onUserPickedOption.name) {
                                popUpTo(
                                    NavRoutes.StatisticsRoute.name
                                )
                            }

                            MainNavOption.REGISTRATION -> navController.navigate(onUserPickedOption.name) {
                                popUpTo(
                                    NavRoutes.RegistrationRoute.name
                                )
                            }

                            MainNavOption.LOGIN -> navController.navigate(onUserPickedOption.name) {
                                popUpTo(
                                    NavRoutes.LoginRoute.name
                                )
                            }

                            MainNavOption.SETTINGS -> navController.navigate(onUserPickedOption.name) {
                                popUpTo(
                                    NavRoutes.SettingsRoute.name
                                )
                            }

                            MainNavOption.PROFILE -> navController.navigate(onUserPickedOption.name) {
                                popUpTo(
                                    NavRoutes.ProfileRoute.name
                                )
                            }

                            MainNavOption.VISUALIZATION -> navController.navigate(onUserPickedOption.name) {
                                popUpTo(
                                    NavRoutes.VisualizationRoute.name
                                )
                            }
                        }
                    }
                }) {
                NavHost(navController, startDestination = NavRoutes.HomeRoute.name) {
                    mainGraph(
                        drawerState,
                        navController,
                        dataCollectionViewModel,
                        metaMotionRepository
                    )
                }
            }
        }
    }
}

enum class NavRoutes {
    HomeRoute,
    PastWorkoutsRoute,
    TrainingRoute,
    DataCollectionRoute,
    CollectedDataRoute,
    StatisticsRoute,
    RegistrationRoute,
    LoginRoute,
    SettingsRoute,
    ProfileRoute,
    VisualizationRoute
}

object DrawerParams {
    val drawerButtons = arrayListOf(
        AppDrawerItemInfo(
            MainNavOption.PROFILE,
            R.string.drawer_profile,
            R.drawable.baseline_account_circle_24,
            R.string.drawer_profile_description
        ), AppDrawerItemInfo(
            MainNavOption.HOME,
            R.string.drawer_home,
            R.drawable.ic_home,
            R.string.drawer_home_description
        ),
        AppDrawerItemInfo(
            MainNavOption.PAST_WORKOUTS,
            R.string.drawer_trainig,
            R.drawable.ic_training,
            R.string.drawer_training_description
        ),
        AppDrawerItemInfo(
            MainNavOption.DATA_COLLECTION,
            R.string.drawer_data_collection,
            R.drawable.ic_data_collection,
            R.string.drawer_data_collection_description
        ),
        AppDrawerItemInfo(
            MainNavOption.COLLECTED_DATA,
            R.string.drawer_collected_data,
            R.drawable.ic_collected_data,
            R.string.drawer_collected_data_description
        ),
        AppDrawerItemInfo(
            MainNavOption.STATISTICS,
            R.string.drawer_statistics,
            R.drawable.baseline_bar_chart_24,
            R.string.drawer_statistics_description
        ),
        AppDrawerItemInfo(
            MainNavOption.VISUALIZATION,
            R.string.drawer_visualization,
            R.drawable.baseline_show_chart_24,
            R.string.drawer_visualization_description
        )
    )


}