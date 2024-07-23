package at.bachelor.workoutcounter.app.ui.components.appdrawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import at.bachelor.workoutcounter.DrawerParams
import kotlinx.coroutines.launch
import navigation.MainNavOption

@Composable
fun <T : Enum<T>> AppDrawerContent(
    drawerState: DrawerState,
    menuItems: List<AppDrawerItemInfo<T>>,
    defaultPick: T,
    onClick: (T) -> Unit
) {
    var currentPick by remember { mutableStateOf(defaultPick) }
    val coroutineScope = rememberCoroutineScope()
    ModalDrawerSheet {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Surface(color = MaterialTheme.colorScheme.background) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(menuItems) { item ->
                            AppDrawerItem(item = item) { navOption ->
                                if (currentPick == navOption) {
                                    coroutineScope.launch { drawerState.close() }
                                    return@AppDrawerItem
                                }
                                currentPick = navOption
                                coroutineScope.launch { drawerState.close() }
                                onClick(navOption)
                            }
                        }
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun AppDrawerContentPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    AppDrawerContent(
        drawerState = drawerState,
        menuItems = DrawerParams.drawerButtons,
        defaultPick = MainNavOption.HOME,
        onClick = { /* Handle click */ }
    )
}

