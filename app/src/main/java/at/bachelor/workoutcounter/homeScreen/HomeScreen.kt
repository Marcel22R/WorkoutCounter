package at.bachelor.workoutcounter.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import at.bachelor.workoutcounter.R
import at.bachelor.workoutcounter.app.ui.components.appbar.AppBar

@Composable
fun HomeScreen(drawerState: DrawerState) {
    Scaffold(topBar = { AppBar(drawerState = drawerState) }) {
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