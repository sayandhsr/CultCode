package com.unsulliedcode

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.unsulliedcode.ui.theme.UnsulliedCodeTheme
import com.unsulliedcode.data.UserProgressRepository

val LocalThemeUpdater = staticCompositionLocalOf<() -> Unit> { {} }

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val repo = remember { UserProgressRepository(this) }
      var isDark by remember { mutableStateOf(repo.isDarkTheme()) }
      var isMono by remember { mutableStateOf(repo.isMonospace()) }
      
      CompositionLocalProvider(
          LocalThemeUpdater provides { 
              isDark = repo.isDarkTheme()
              isMono = repo.isMonospace()
          }
      ) {
          UnsulliedCodeTheme(darkTheme = isDark, useMonospace = isMono) { 
              Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { 
                  MainNavigation() 
              } 
          }
      }
    }
  }
}

