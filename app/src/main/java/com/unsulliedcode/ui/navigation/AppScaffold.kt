package com.unsulliedcode.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import com.unsulliedcode.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    showBackButton: Boolean = true,
    onBack: () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = { Text("UnsulliedCode ", fontWeight = FontWeight.Bold, color = LocalAppColors.current.accentPrimary) },
    content: @Composable (PaddingValues) -> Unit
) {
    val colors = LocalAppColors.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.bg,
        topBar = {
            LargeTopAppBar(
                title = { Text(title, color = colors.textPrimary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onBack) {
                            Text("<", color = colors.textPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                },
                actions = actions,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.bgElevated,
                    scrolledContainerColor = colors.surface,
                    titleContentColor = colors.textPrimary
                ),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = bottomBar,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        content(padding)
    }
}
