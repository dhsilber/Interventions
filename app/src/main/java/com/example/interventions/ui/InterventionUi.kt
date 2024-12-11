package com.example.interventions.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class InterventionUi(
    val weight: FontWeight = FontWeight.Normal,
    val color: Color = Color.Black,
    val background: Color = Color.White,
    val lineHeight: TextUnit = 20.sp,
    val padding: Dp = 4.dp,
)
