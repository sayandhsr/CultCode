package com.cultcode.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.cultcode.data.DefaultDataRepository
import com.cultcode.theme.CultCodeTheme
import com.cultcode.CourseList
import com.cultcode.Practice
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
  onItemClick: (NavKey) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(DefaultDataRepository()) },
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  when (state) {
    MainScreenUiState.Loading -> {
      // Blank
    }
    is MainScreenUiState.Success -> {
      MainScreen(data = (state as MainScreenUiState.Success).data, onItemClick = onItemClick, modifier = modifier)
    }
    is MainScreenUiState.Error -> {
      Text("Error loading data: ${(state as MainScreenUiState.Error).throwable.message}")
    }
  }
}

@Composable
internal fun MainScreen(data: List<String>, onItemClick: (NavKey) -> Unit, modifier: Modifier = Modifier) {
  Scaffold(
    topBar = {
        @OptIn(ExperimentalMaterial3Api::class)
        TopAppBar(
            title = { Text("CultCode", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }
  ) { padding ->
    Column(
        modifier = modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to CultCode!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🔥 12 DAY STREAK", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Continue Learning", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Python: Functions & Modules", fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { onItemClick(CourseList) }) {
            Text("Go to Course List")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onItemClick(Practice("PY-EASY-VARIABLES-001")) }) {
            Text("Practice Python Variable Assignment")
        }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
  CultCodeTheme { MainScreen(listOf("Android"), {}) }
}

@Preview(showBackground = true, widthDp = 340)
@Composable
fun MainScreenPortraitPreview() {
  CultCodeTheme { MainScreen(listOf("Android"), {}) }
}
