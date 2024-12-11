package com.example.interventions

import com.example.interventions.ui.InterventionUi
import java.time.Duration
import java.time.LocalTime

data class Intervention(
    val id: Int = 0,

    val text: String,
    val extraText: MutableList<String> = mutableListOf(),
    var targetTime: LocalTime = LocalTime.MAX,
//    var duration: Duration? = null,
    var startTime: LocalTime? = null,
    val isSupplement: Boolean = false,
    val isFood: Boolean = false,
    val isBinder: Boolean = false,
    var earliestBinder: LocalTime = LocalTime.MIN,
    var binderTime: LocalTime = LocalTime.MIN,
    var done: Boolean = false,
//    var ui: InterventionUi = InterventionUi()
    var alarmForFutureIntervention: AlarmForFutureIntervention = AlarmForFutureIntervention(),
) {
    fun extra( text: String): Intervention {
        extraText.add(text)
        return this
    }

    fun time(setTime: String): Intervention {
        targetTime = LocalTime.parse(setTime)
        return this
    }

    fun alarm(setTime: String, intervention: Intervention): Intervention {
        alarmForFutureIntervention = AlarmForFutureIntervention(
            duration = Duration.parse(setTime),
            intervention = intervention
        )
        return this
    }

}

data class AlarmForFutureIntervention(
    val duration: Duration = Duration.ZERO,
    val intervention: Intervention = Intervention(text = ""),
)

val amBinder = Intervention(id = 0, "Ultra Binder", isBinder = true).extra("Two teaspoons in a glass of water")
val pmBinder = Intervention(id = 0, "Ultra Binder", isBinder = true).extra("Two teaspoons in a glass of water")

val interventionSource = listOf<Intervention>(
    Intervention(id = 0, "Wake up").time("07:00"),
    Intervention(id = 0, "BitterX", isSupplement = true).extra("Six squirts").extra("Let absorb through mouth for 60-90 seconds before swallowing"),
    Intervention(id = 0, "Artemisinin", isSupplement = true).alarm("DT30M", amBinder),
    Intervention(text = "Put on hearing aids"),
    Intervention(text = "Dress to go outside"),
    amBinder,
    Intervention(text = "Morning walk").extra("Get out as early as I can"),
    Intervention(id = 0, "Make Breakfast"),
    Intervention(id = 0, "Eat breakfast", isFood = true),
    Intervention(id = 0, "Pills after breakfast", isSupplement = true),
    Intervention(id = 0, "Glutathione", isSupplement = true),
    Intervention(id = 0, "Nystatin", isSupplement = true),
    Intervention(id = 0, "C-RLA", isSupplement = true).extra("away from food so it absorbs better"),
    Intervention(id = 0, "Take mushroom pills", isSupplement = true).extra("20 minutes before eating"),
    Intervention(id = 0, "Eat lunch", isFood = true),
    Intervention(id = 0, "Nystatin", isSupplement = true),
    Intervention(id = 0, "Morning pills", isSupplement = true).extra("Includes probiotic - two hours apart from Nystatin"),
    Intervention(id = 0, "BitterX", isSupplement = true)
        .extra("Six squirts")
        .extra("Let absorb through mouth for 60-90 seconds before swallowing")
        .alarm("DT30M", pmBinder),
    pmBinder,
    Intervention(id = 0, "Ortho biotic", isSupplement = true).extra("Take 2 hours apart from binder"),
    Intervention(id = 0, "Eat supper", isFood = true),
    Intervention(id = 0, "Pills after last food", isSupplement = true),
    Intervention(text = "Floss & brush teeth").extra("Rinse & gargle with antiseptic mouthwash and then hydrogen peroxide 1:1 with water"),
    Intervention(text = "Turn on electric blanket"),
    Intervention(id = 0, "Nystatin", isSupplement = true),
    Intervention(id = 0, "Last pills", isSupplement = true),
    Intervention(id = 0, "Glutathione", isSupplement = true),
    Intervention(text = "Deploy 'production' version of Interventions"),
    Intervention(id = 0, "Bedtime").time("22:00"),
)
