package com.example.interventions

import java.time.Duration
import java.time.LocalTime

data class Intervention(
    val id: Int,

    val text: String,
    val extraText: MutableList<String> = mutableListOf(),
    var targetTime: LocalTime = LocalTime.MAX,
    var duration: Duration? = null,
    var startTime: LocalTime? = null,
    val isSupplement: Boolean = false,
    val isFood: Boolean = false,
    val isBinder: Boolean = false,
    var earliestBinder: LocalTime = LocalTime.MIN,
    var binderTime: LocalTime = LocalTime.MIN,
    var done: Boolean = false,
) {
    fun extra( text: String): Intervention {
        extraText.add(text)
        return this
    }

    fun time(setTime: String): Intervention {
        targetTime = LocalTime.parse(setTime)
        return this
    }

    fun duration(setTime: String): Intervention {
        duration = Duration.parse(setTime)
        return this
    }

}

val interventionSource = listOf<Intervention>(
    Intervention(id = 0, "Wake up").time("07:00"),
    Intervention(id = 0, "Morning pills", isSupplement = true).extra("Includes probiotic - two hours apart from Nystatin"),
    Intervention(id = 0, "Artemisinin", isSupplement = true),
    Intervention(id = 0, "C-RLA", isSupplement = true).extra("away from food so it absorbs better"),
    Intervention(id = 0, "BitterX", isSupplement = true).extra("Six squirts").extra("Let absorb through mouth for 60-90 seconds before swallowing"),
    Intervention(id = 0, "Glutathione", isSupplement = true),
//    Intervention(id = 0, "Wait one hour").duration("PT1H"),
    Intervention(id = 0, "Ultra Binder", isBinder = true).extra("Two teaspoons in a glass of water"),
//    Intervention(id = 0, "Wait two hours").duration("PT2H"),
    Intervention(id = 0, "Make Breakfast"),
    Intervention(id = 0, "Take mushroom pills", isSupplement = true).extra("20 minutes before eating"),
    Intervention(id = 0, "Eat breakfast", isFood = true),
    Intervention(id = 0, "Pills after breakfast", isSupplement = true),
    Intervention(id = 0, "Eat lunch", isFood = true),
    Intervention(id = 0, "Eat supper", isFood = true),
    Intervention(id = 0, "Pills after last food", isSupplement = true),
//    Intervention(id = 0, "Wait one hour").duration("PT1H"),
    Intervention(id = 0, "Ultra Binder", isBinder = true).extra("Two teaspoons in a glass of water"),
//    Intervention(id = 0, "Wait two hours").duration("PT2H"),
    Intervention(id = 0, "BitterX", isSupplement = true).extra("Six squirts").extra("Let absorb through mouth for 60-90 seconds before swallowing"),
    Intervention(id = 0, "Last pills", isSupplement = true),
    Intervention(id = 0, "Glutathione", isSupplement = true),
    Intervention(id = 0, "Bedtime").time("22:00"),
)
