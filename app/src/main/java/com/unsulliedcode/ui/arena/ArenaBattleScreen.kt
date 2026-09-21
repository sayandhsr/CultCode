package com.unsulliedcode.ui.arena

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation3.runtime.NavKey
import com.unsulliedcode.ArenaResult
import com.unsulliedcode.ui.navigation.AppScaffold
import com.unsulliedcode.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ArenaBattleScreen(
    onNavigate: (NavKey) -> Unit,
    onBack: () -> Unit
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    var code by remember { mutableStateOf("fun main() {\n  // Write your code here\n}") }
    var timeLeft by remember { mutableIntStateOf(300) }
    var opponentProgress by remember { mutableFloatStateOf(0f) }

    val animatedProgress by animateFloatAsState(
        targetValue = opponentProgress,
        animationSpec = Motion.fluid
    )

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
            if (timeLeft % 5 == 0 && opponentProgress < 1f) {
                opponentProgress += 0.05f
            }
        }
    }

    AppScaffold(
        title = "Arena Battle - ${timeLeft / 60}:${(timeLeft % 60).toString().padStart(2, '0')}",
        onBack = onBack
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Space.md)
        ) {
            // Your Code section
            Text(
                text = "YOUR CODE",
                style = typography.label,
                color = colors.textSecondary,
                modifier = Modifier.padding(bottom = Space.xs)
            )
            BasicTextField(
                value = code,
                onValueChange = { code = it },
                textStyle = TextStyle(
                    color = colors.textPrimary,
                    fontFamily = FontFamily.Monospace
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(colors.surface, shape = androidx.compose.foundation.shape.RoundedCornerShape(Radius.md))
                    .padding(Space.sm)
            )

            Spacer(modifier = Modifier.height(Space.md))

            // Opponent Progress
            Text(
                text = "OPPONENT PROGRESS",
                style = typography.label,
                color = colors.textSecondary,
                modifier = Modifier.padding(bottom = Space.xs)
            )
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth(),
                color = colors.accentPrimary,
                trackColor = colors.surface
            )

            Spacer(modifier = Modifier.height(Space.lg))

            Button(
                onClick = { onNavigate(ArenaResult) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accentPrimary,
                    contentColor = colors.textOnAccent
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SUBMIT SOLUTION", style = typography.label)
            }
        }
    }
}
