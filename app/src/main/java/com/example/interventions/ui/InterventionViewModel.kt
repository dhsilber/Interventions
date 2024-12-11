package com.example.interventions.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.interventions.Intervention
import com.example.interventions.interventionSource
import com.example.interventions.planningEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import java.util.Objects.isNull

class InterventionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var current: Int = 0
        get() = uiState.value.current

    private var viewing: Int = -1

    private var showingDone: Boolean = false
    private var interventions: MutableList<Intervention> = mutableListOf()
    private var showingInterventions: MutableList<Intervention> = interventions

    init {
        resetUiState()
    }

    private fun resetUiState() {
        interventions = planningEngine()
        showingInterventions = interventions
        _uiState.value = UiState(
            current = 0,
            viewing = -1,
            interventions = interventions,
            noFoodOrSupplementsEndTime = LocalTime.MIN,
            currentBinder = null,
        )
    }

    fun ui(intervention: Intervention) : InterventionUi {

        val active = current == intervention.id
        val view = uiState.value.viewing == intervention.id

//        if (active and !isNull(intervention.duration)) {
//            if (isNull(intervention.startTime)) {
//                intervention.startTime = LocalTime.now()
//            }
//        }

        val weight = if (active or view) FontWeight.Bold else FontWeight.Normal
        val color = when {
            intervention.done -> Color.Gray
            active -> Color.White
            else -> Color.Black
        }
        val background = when {
            intervention.done -> Color.LightGray
            intervention.isBinder and (intervention.earliestBinder > LocalTime.MIN) -> Color.Red
            (uiState.value.noFoodOrSupplementsEndTime > LocalTime.now()) and (intervention.isSupplement or intervention.isFood) -> Color(0xFFFFC0CB)
            active -> Color.Blue
            view -> Color.Green
            else -> Color.White
        }
        val height = if (active or view) 30.sp else 20.sp
        val padding = if (active or view) 8.dp else 4.dp

        return InterventionUi(
            weight = weight,
            color = color,
            background = background,
            lineHeight = height,
            padding = padding,
        )
    }

    private fun incrementCurrent(interventionId: Int) {
        if (interventionId == interventions.size - 1)
            resetUiState()
        else
            _uiState.update { currentState ->
                currentState.copy(current = interventionId + 1)
        }
    }

    private fun interventionList() : MutableList<Intervention> {
        return if (showingDone)
            interventions
        else
            showingInterventions
    }

    fun clickAction(
        id: Int
    ) {
        val clickedOn = interventions.find { it.id == id }
        if (null != clickedOn) {
            if (clickedOn.isBinder) {
                clickedOn.binderTime = LocalTime.now()
                _uiState.update { currentState ->
                    currentState.copy(
                        interventions = interventionList(),
                        noFoodOrSupplementsEndTime = LocalTime.now().plusHours(2)
                    )
                }
                return
            }
            else {
                clickedOn.done = true
                showingInterventions = showingInterventions.filter { it.id != clickedOn.id }.toMutableList()
                _uiState.update { currentState ->
                    currentState.copy(
                        interventions = interventionList(),
                    )
                }
            }
        }

        if (current == id) {
            incrementCurrent(id)
            val intervention = showingInterventions.find { it.id == current }
            if (intervention != null) {
                if( intervention.isBinder) {
                    intervention.earliestBinder = LocalTime.now().plusHours(1)
                    _uiState.update { currentState ->
                        currentState.copy(
                            interventions = interventionList(),
                            noFoodOrSupplementsEndTime = LocalTime.now().plusHours(3),
                            currentBinder = intervention,
                        )
                    }
                    incrementCurrent(intervention.id)
                }
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(viewing = id)
            }
        }
    }

    fun longClickAction(
        id: Int
    ) {
        val clickedOn = interventions.find { it.id == id }
        if (null != clickedOn) {
            if (clickedOn.isBinder) {
                clickedOn.done = true
                clickedOn.earliestBinder = LocalTime.MIN
                showingInterventions = showingInterventions.filter { it.id != clickedOn.id }.toMutableList()
                _uiState.update { currentState ->
                    currentState.copy(
                        currentBinder = null,
                        noFoodOrSupplementsEndTime = LocalTime.MIN,
                        interventions = interventionList(),
                    )
                }
            }
        }
    }

    fun toggleShowDone() {
        showingDone = !showingDone

        val list = if (showingDone) {
            interventions
        }
        else {
            showingInterventions
        }
        val buttonText = if (showingDone)
            "Hide Done"
        else
            "Show Done"
        _uiState.update { currentState ->
            currentState.copy(
                interventions = list,
                showDoneButtonText = buttonText,
            )
        }
    }

}