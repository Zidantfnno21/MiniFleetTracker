package com.example.minifleettracker.helper.defaults

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.minifleettracker.data.model.CircleParameters
import com.example.minifleettracker.data.model.StrokeParameters

object CircleParametersDefaults {

    private val defaultCircleRadius = 12.dp

    fun circleParameters(
        radius: Dp = defaultCircleRadius,
        backgroundColor: Color = Color.Cyan,
        stroke: StrokeParameters? = null,
        icon: ImageVector?= Icons.Default.LocationOn
    ) = CircleParameters(
        radius,
        backgroundColor,
        stroke,
        icon
    )
}