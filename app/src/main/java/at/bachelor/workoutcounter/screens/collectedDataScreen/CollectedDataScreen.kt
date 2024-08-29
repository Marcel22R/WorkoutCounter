package at.bachelor.workoutcounter.screens.collectedDataScreen

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import at.bachelor.workoutcounter.app.ui.components.appbar.AppBar
import java.io.File

@Composable
fun CollectedDataScreen(drawerState: DrawerState) {
    val context = LocalContext.current
    val savedFiles = remember {
        getSavedFiles(context)
    }

    Scaffold(
        topBar = { AppBar(drawerState = drawerState) }
    ) { innerPadding ->
        FileList(fileList = savedFiles, modifier = Modifier.padding(innerPadding))
        if (savedFiles.isEmpty()){
            Text(text = "No files found")
        }
    }
}

private fun getSavedFiles(context: Context): List<File> {
    val dir = context.getExternalFilesDir(null)
    if (dir != null && dir.exists()) {
        val files = dir.listFiles { file -> file.isFile }
        return files?.toList() ?: emptyList()
    }

    return emptyList()
}

@Composable
fun FileList(fileList: List<File>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(fileList) { file ->
            FileListItem(file)
        }
    }
}

@Composable
fun FileListItem(file: File) {
    Text(
        text = file.name,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        style = MaterialTheme.typography.bodyMedium
    )
}