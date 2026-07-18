package com.justindwinata.usahanaik.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.justindwinata.usahanaik.ui.theme.AppSpacing
import com.justindwinata.usahanaik.ui.theme.BorderSubtle
import com.justindwinata.usahanaik.ui.theme.CoralPrimary
import com.justindwinata.usahanaik.ui.theme.CoralSoft
import com.justindwinata.usahanaik.ui.theme.GreenPositive
import com.justindwinata.usahanaik.ui.theme.InkMuted
import com.justindwinata.usahanaik.ui.theme.SurfaceWarm

@Composable
fun UsahaNaikCard(
    modifier: Modifier = Modifier,
    containerColor: Color = SurfaceWarm,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.md),
            content = content
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (actionLabel != null) {
            Text(
                text = actionLabel,
                style = MaterialTheme.typography.labelMedium,
                color = CoralPrimary
            )
        }
    }
}

@Composable
fun PillBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = CoralSoft,
    contentColor: Color = CoralPrimary
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            text = text,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier.height(52.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = CoralPrimary,
            contentColor = SurfaceWarm
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    helper: String,
    modifier: Modifier = Modifier,
    containerColor: Color = SurfaceWarm,
    accentColor: Color = CoralPrimary
) {
    UsahaNaikCard(modifier = modifier, containerColor = containerColor) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(accentColor.copy(alpha = 0.14f), CircleShape)
                .border(1.dp, accentColor.copy(alpha = 0.22f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(accentColor, CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = InkMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = helper,
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ProgressScoreCard(
    title: String,
    score: Int,
    helper: String,
    modifier: Modifier = Modifier,
    containerColor: Color = SurfaceWarm
) {
    UsahaNaikCard(modifier = modifier, containerColor = containerColor) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                PillBadge(text = "Preview")
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = helper,
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMuted
                )
            }
            Spacer(modifier = Modifier.width(AppSpacing.md))
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { score.coerceIn(0, 100) / 100f },
                    modifier = Modifier.size(86.dp),
                    color = GreenPositive,
                    trackColor = BorderSubtle,
                    strokeWidth = 8.dp,
                    strokeCap = StrokeCap.Round
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = score.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/100",
                        style = MaterialTheme.typography.labelMedium,
                        color = InkMuted
                    )
                }
            }
        }
    }
}

@Composable
fun TrendLineChart(
    revenuePoints: List<Float>,
    expensePoints: List<Float>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxWidth().height(150.dp)) {
        val allPoints = revenuePoints + expensePoints
        val maxPoint = allPoints.maxOrNull()?.takeIf { it > 0f } ?: 1f
        val minPoint = allPoints.minOrNull() ?: 0f
        val range = (maxPoint - minPoint).takeIf { it > 0f } ?: 1f

        fun mapPoint(index: Int, value: Float, total: Int): Offset {
            val x = if (total <= 1) 0f else size.width * index / (total - 1)
            val normalized = (value - minPoint) / range
            val y = size.height - (size.height * normalized)
            return Offset(x, y)
        }

        repeat(4) { index ->
            val y = size.height * index / 3f
            drawLine(
                color = BorderSubtle,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        fun drawSeries(points: List<Float>, color: Color) {
            for (index in 0 until points.lastIndex) {
                drawLine(
                    color = color,
                    start = mapPoint(index, points[index], points.size),
                    end = mapPoint(index + 1, points[index + 1], points.size),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            points.forEachIndexed { index, value ->
                drawCircle(
                    color = color,
                    radius = 4.dp.toPx(),
                    center = mapPoint(index, value, points.size),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }

        drawSeries(expensePoints, CoralPrimary.copy(alpha = 0.7f))
        drawSeries(revenuePoints, GreenPositive)
    }
}
