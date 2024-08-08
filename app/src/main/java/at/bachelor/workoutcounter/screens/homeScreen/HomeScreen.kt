package at.bachelor.workoutcounter.screens.screens.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.bachelor.workoutcounter.NavRoutes
import at.bachelor.workoutcounter.R
import at.bachelor.workoutcounter.app.ui.components.appbar.AppBar
import navigation.MainNavOption

@Composable
fun HomeScreen(drawerState: DrawerState, navController: NavHostController) {
    Scaffold(topBar = { AppBar(drawerState = drawerState) }, bottomBar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    navController.navigate(MainNavOption.TRAINING.name) {
                        popUpTo(NavRoutes.TrainingRoute.name)
                    }
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text(text = "Start workout")
            }
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.homeTitle))
        }
    }
}