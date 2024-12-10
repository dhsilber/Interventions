package com.example.interventions.ui

import com.example.interventions.Intervention
import java.time.LocalTime

data class UiState (
    val current: Int = 0,
    val viewing: Int = -1,
    val interventions: List<Intervention> = emptyList(),
    val noFoodOrSupplementsEndTime: LocalTime = LocalTime.MIN,
    val currentBinder: Int = -1,
    val showDoneButtonText: String = "Show Done"
)