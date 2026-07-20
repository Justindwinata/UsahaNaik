package com.justindwinata.usahanaik.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.justindwinata.usahanaik.ui.theme.AppSpacing
import com.justindwinata.usahanaik.ui.theme.BlueSoft
import com.justindwinata.usahanaik.ui.theme.BorderSubtle
import com.justindwinata.usahanaik.ui.theme.BorderStrong
import com.justindwinata.usahanaik.ui.theme.CoralPrimary
import com.justindwinata.usahanaik.ui.theme.CoralSoft
import com.justindwinata.usahanaik.ui.theme.GreenPositive
import com.justindwinata.usahanaik.ui.theme.GreenSoft
import com.justindwinata.usahanaik.ui.theme.Ink
import com.justindwinata.usahanaik.ui.theme.InkMuted
import com.justindwinata.usahanaik.ui.theme.InkStrong
import com.justindwinata.usahanaik.ui.theme.InkSubtle
import com.justindwinata.usahanaik.ui.theme.SurfaceElevated
import com.justindwinata.usahanaik.ui.theme.SurfacePressed
import com.justindwinata.usahanaik.ui.theme.SurfaceWarm

@Composable
fun UsahaNaikCard(
    modifier: Modifier = Modifier,
    containerColor: Color = SurfaceWarm,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.md),
            content = content
        )
    }
}

@Composable
fun ProfessionalScreen(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    badge: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
    ) {
        ScreenHeroHeader(title = title, subtitle = subtitle, badge = badge)
        content()
    }
}

@Composable
fun ScreenHeroHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    badge: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        badge?.let {
            PillBadge(text = it, containerColor = BlueSoft, contentColor = CoralPrimary)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = InkStrong
        )
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
    }
}

@Composable
fun ProfessionalSectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, color = InkStrong)
            subtitle?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
            }
        }
        if (actionLabel != null && onActionClick != null) {
            Text(
                modifier = Modifier.clickable(onClick = onActionClick),
                text = actionLabel,
                style = MaterialTheme.typography.labelLarge,
                color = CoralPrimary
            )
        }
    }
}

@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    tone: StatusTone = StatusTone.Neutral
) {
    val colors = when (tone) {
        StatusTone.Positive -> GreenSoft to GreenPositive
        StatusTone.Warning -> com.justindwinata.usahanaik.ui.theme.YellowSoft to com.justindwinata.usahanaik.ui.theme.YellowDeep
        StatusTone.Danger -> com.justindwinata.usahanaik.ui.theme.RoseSoft to com.justindwinata.usahanaik.ui.theme.RoseDeep
        StatusTone.Info -> BlueSoft to com.justindwinata.usahanaik.ui.theme.BlueDeep
        StatusTone.Neutral -> SurfacePressed to InkMuted
    }
    PillBadge(
        text = text,
        modifier = modifier,
        containerColor = colors.first,
        contentColor = colors.second
    )
}

enum class StatusTone {
    Positive,
    Warning,
    Danger,
    Info,
    Neutral
}

@Composable
fun ProfessionalKpiCard(
    title: String,
    value: String,
    helper: String,
    modifier: Modifier = Modifier,
    accentColor: Color = CoralPrimary,
    badge: String? = null
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(AppSpacing.md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(accentColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(AppSpacing.xs))
                Text(
                    modifier = Modifier.weight(1f),
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = InkMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                badge?.let { StatusBadge(text = it, tone = StatusTone.Info) }
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = InkStrong,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = helper,
                style = MaterialTheme.typography.bodyMedium,
                color = InkSubtle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ProfessionalActionTile(
    title: String,
    message: String,
    actionLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    accentColor: Color = CoralPrimary
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier.padding(AppSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(accentColor.copy(alpha = 0.12f), CircleShape)
                    .border(1.dp, accentColor.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, tint = accentColor)
                } else {
                    Box(modifier = Modifier.size(12.dp).background(accentColor, CircleShape))
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, color = Ink)
                Text(text = message, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
            }
            Text(text = actionLabel, style = MaterialTheme.typography.labelLarge, color = accentColor)
        }
    }
}

@Composable
fun FormSectionCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    UsahaNaikCard(modifier = modifier.fillMaxWidth(), containerColor = SurfaceElevated) {
        Text(text = title, style = MaterialTheme.typography.titleLarge, color = InkStrong)
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        Spacer(modifier = Modifier.height(AppSpacing.md))
        content()
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
            style = MaterialTheme.typography.titleLarge,
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
    modifier: Modifier = Modifier,
    contentDescription: String = text
) {
    Button(
        modifier = modifier
            .height(52.dp)
            .semantics { this.contentDescription = contentDescription },
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = CoralPrimary,
            contentColor = SurfaceWarm
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun DemoStateCard(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    containerColor: Color = SurfaceWarm,
    badgeLabel: String? = null
) {
    UsahaNaikCard(modifier = modifier.fillMaxWidth(), containerColor = containerColor) {
        badgeLabel?.let {
            PillBadge(text = it)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(text = message, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        if (actionLabel != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(AppSpacing.md))
            PrimaryActionButton(
                text = actionLabel,
                onClick = onActionClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun EmptyStateCard(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    DemoStateCard(
        title = title,
        message = message,
        modifier = modifier,
        actionLabel = actionLabel,
        onActionClick = onActionClick,
        containerColor = SurfaceElevated,
        badgeLabel = "Empty state"
    )
}

@Composable
fun LoadingStateCard(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    UsahaNaikCard(modifier = modifier.fillMaxWidth(), containerColor = SurfaceWarm) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = CoralPrimary,
                strokeWidth = 3.dp
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = message, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
            }
        }
    }
}

@Composable
fun ErrorStateCard(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    DemoStateCard(
        title = title,
        message = message,
        modifier = modifier,
        actionLabel = actionLabel,
        onActionClick = onActionClick,
        containerColor = SurfaceElevated,
        badgeLabel = "Needs attention"
    )
}

@Composable
fun CtaCard(
    title: String,
    message: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = SurfaceWarm
) {
    DemoStateCard(
        title = title,
        message = message,
        modifier = modifier,
        actionLabel = actionLabel,
        onActionClick = onActionClick,
        containerColor = containerColor,
        badgeLabel = "Next action"
    )
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
