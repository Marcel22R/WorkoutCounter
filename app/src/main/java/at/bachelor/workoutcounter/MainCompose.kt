package at.bachelor.workoutcounter

import AppDrawerTheme
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import at.bachelor.workoutcounter.app.ui.components.appdrawer.AppDrawerContent
import at.bachelor.workoutcounter.app.ui.components.appdrawer.AppDrawerItemInfo

import navigation.MainNavOption
import navigation.mainGraph


@Composable
fun MainCompose(
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
                                    NavRoutes.PastWorkoutsRoute.name
                                )
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
                        }
                    }
                }) {
                //could add logic for registration here
                NavHost(navController, startDestination = NavRoutes.HomeRoute.name) {
                    mainGraph(drawerState)
                }
            }
        }
    }
}

enum class NavRoutes {
    HomeRoute,
    PastWorkoutsRoute,
    StatisticsRoute,
    RegistrationRoute,
    LoginRoute,
    SettingsRoute,
    ProfileRoute
}

object DrawerParams {
    val drawerButtons = arrayListOf(
        AppDrawerItemInfo(
            MainNavOption.PROFILE,
            R.string.drawer_profile,
            R.drawable.baseline_account_circle_24,
            R.string.drawer_profile_description
        ),AppDrawerItemInfo(
            MainNavOption.HOME,
            R.string.drawer_home,
            R.drawable.ic_home,
            R.string.drawer_home_description
        ),
        AppDrawerItemInfo(
            MainNavOption.TRAINING,
            R.string.drawer_trainig,
            R.drawable.ic_training,
            R.string.drawer_training_description
        ),
        AppDrawerItemInfo(
            MainNavOption.STATISTICS,
            R.string.drawer_statistics,
            R.drawable.baseline_bar_chart_24,
            R.string.drawer_statistics_description
        )
    )


}

@Preview
@Composable
fun MainActivityPreview() {
    MainCompose()
}