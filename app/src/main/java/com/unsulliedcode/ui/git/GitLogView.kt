package com.unsulliedcode.ui.git

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.unsulliedcode.ui.theme.*

data class GitCommit(
    val hash: String,
    val message: String,
    val author: String,
    val branch: String,
    val isMerge: Boolean = false
)

@Composable
fun GitLogView(
    commits: List<GitCommit>,
    currentBranch: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.bg),
        contentPadding = PaddingValues(Space.md),
        verticalArrangement = Arrangement.spacedBy(Space.xxs)
    ) {
        item {
            Text(
                text = "⎇ $currentBranch",
                style = typography.label,
                color = colors.accentSecondary,
                modifier = Modifier.padding(bottom = Space.sm)
            )
        }

        items(commits) { commit ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surface, RoundedCornerShape(Radius.sm))
                    .padding(Space.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Graph node
                Box(
                    modifier = Modifier
                        .size(Space.sm)
                        .clip(CircleShape)
                        .background(
                            if (commit.isMerge) colors.accentTertiary
                            else colors.accentPrimary
                        )
                )
                Spacer(modifier = Modifier.width(Space.sm))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = commit.message,
                        style = typography.body,
                        color = colors.textPrimary,
                        maxLines = 1
                    )
                    Row {
                        Text(
                            text = commit.hash.take(7),
                            style = typography.code,
                            color = colors.syntaxNumber
                        )
                        Spacer(modifier = Modifier.width(Space.sm))
                        Text(
                            text = commit.author,
                            style = typography.caption,
                            color = colors.textTertiary
                        )
                    }
                }
                if (commit.branch != currentBranch) {
                    Text(
                        text = commit.branch,
                        style = typography.caption,
                        color = colors.accentSecondary,
                        modifier = Modifier
                            .background(
                                colors.accentMuted,
                                RoundedCornerShape(Radius.sm)
                            )
                            .padding(horizontal = Space.xs, vertical = Space.xxs)
                    )
                }
            }
        }
    }
}
