package com.example.minifleettracker.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.minifleettracker.data.database.TripLog
import com.example.minifleettracker.data.model.CircleParameters
import com.example.minifleettracker.data.model.LineParameters
import com.example.minifleettracker.helper.TripNodePosition
import com.example.minifleettracker.helper.defaults.CircleParametersDefaults

@Composable
fun TripLog(logs: List<TripLog>, onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        )

        Row(
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trip Log",
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            IconButton(onClose) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            content = {
                itemsIndexed(logs) { index: Int, log: TripLog ->
                    TripNode(
                        position = mapToTripNodePosition(index, logs.size),
                        circleParameters = CircleParametersDefaults.circleParameters(
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            stroke = null,
                            icon = Icons.Default.LocationOn,
                            radius = 16.dp
                        ),
                        lineParameters = getLineBrush(
                            index = index,
                            items = logs
                        ),
                        contentStartOffset = 16.dp,
                        spacerBetweenNodes = 24.dp
                    ) { modifier ->
                        TripItem(log = log, modifier)
                    }
                }
            },
            contentPadding = PaddingValues(16.dp)
        )
    }
}

@Composable
fun TripNode(
    position: TripNodePosition,
    circleParameters: CircleParameters,
    lineParameters: LineParameters? = null,
    contentStartOffset: Dp = 16.dp,
    spacerBetweenNodes: Dp = 8.dp,
    content: @Composable BoxScope.(modifier: Modifier) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(circleParameters.radius * 2)
                .drawBehind {
                    val circleRadiusInPx = circleParameters.radius.toPx()
                    val centerX = size.width / 2

                    lineParameters?.let {
                        drawLine(
                            brush = it.brush,
                            start = Offset(x = circleRadiusInPx, y = circleRadiusInPx * 2),
                            end = Offset(x = circleRadiusInPx, y = this.size.height + circleRadiusInPx * 6),
                            strokeWidth = it.strokeWidth.toPx()
                        )
                    }

                    drawCircle(
                        color = circleParameters.backgroundColor,
                        radius = circleRadiusInPx,
                        center = Offset(centerX, circleRadiusInPx)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            circleParameters.icon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(circleParameters.radius)
                )
            }
        }

        Spacer(modifier = Modifier.width(contentStartOffset))

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = spacerBetweenNodes)
        ) {
            content(
                Modifier
            )
        }
    }
}

@Composable
private fun getLineBrush(index: Int, items: List<TripLog>): LineParameters? {
    return if (index != items.lastIndex) {
        LineParameters(
            strokeWidth = 2.dp,
            brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary
                )
            )
        )
    } else null
}

private fun mapToTripNodePosition(index: Int, size: Int): TripNodePosition = when (index) {
    0 -> TripNodePosition.FIRST
    size - 1 -> TripNodePosition.LAST
    else -> TripNodePosition.MIDDLE
}
