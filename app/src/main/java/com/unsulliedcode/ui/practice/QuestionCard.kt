package com.unsulliedcode.ui.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.unsulliedcode.ui.theme.*

import com.unsulliedcode.data.Question

@Composable
fun QuestionCard(
    question: Question,
    selectedAnswer: Int?,
    onSelectAnswer: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .themeDepth(isLight = colors.isLight, accentColor = colors.accentPrimary)
            .background(colors.surfaceElevated, RoundedCornerShape(Radius.lg))
            .padding(Space.lg)
    ) {
        // Difficulty badge
        Text(
            text = question.difficulty.uppercase(),
            style = typography.overline,
            color = when (question.difficulty.lowercase()) {
                "easy" -> colors.difficultyEasy
                "medium" -> colors.difficultyMedium
                "hard" -> colors.difficultyHard
                else -> colors.difficultyExpert
            },
            modifier = Modifier
                .background(
                    when (question.difficulty.lowercase()) {
                        "easy" -> colors.successBg
                        "medium" -> colors.warningBg
                        "hard" -> colors.errorBg
                        else -> colors.infoBg
                    },
                    RoundedCornerShape(Radius.sm)
                )
                .padding(horizontal = Space.sm, vertical = Space.xxs)
        )

        Spacer(modifier = Modifier.height(Space.md))

        Text(
            text = question.text,
            style = typography.h3,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(Space.lg))

        question.options.forEachIndexed { index, option ->
            val isSelected = selectedAnswer == index
            val isCorrect = selectedAnswer != null && index == question.correctIndex
            val isWrong = selectedAnswer == index && index != question.correctIndex

            val bgColor = when {
                isCorrect && selectedAnswer != null -> colors.successBg
                isWrong -> colors.errorBg
                isSelected -> colors.accentMuted
                else -> colors.surface
            }
            val borderColor = when {
                isCorrect && selectedAnswer != null -> colors.successBorder
                isWrong -> colors.errorBorder
                isSelected -> colors.borderAccent
                else -> colors.borderSubtle
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Space.xs)
                    .background(bgColor, RoundedCornerShape(Radius.md))
                    .interactive(onClick = { if (selectedAnswer == null) onSelectAnswer(index) })
                    .padding(Space.md)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${('A' + index)}.",
                        style = typography.label,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(end = Space.sm)
                    )
                    Text(
                        text = option,
                        style = typography.body,
                        color = colors.textPrimary
                    )
                }
            }
        }

        // Explanation (shown after answer)
        if (selectedAnswer != null) {
            Spacer(modifier = Modifier.height(Space.md))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.infoBg, RoundedCornerShape(Radius.md))
                    .padding(Space.md)
            ) {
                Column {
                    Text(
                        text = "EXPLANATION",
                        style = typography.overline,
                        color = colors.info
                    )
                    Spacer(modifier = Modifier.height(Space.xs))
                    Text(
                        text = question.explanation,
                        style = typography.body,
                        color = colors.textSecondary
                    )
                }
            }
        }
    }
}
