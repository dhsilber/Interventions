package com.example.interventions.ui

import androidx.lifecycle.ViewModel
import com.example.interventions.Intervention
import com.example.interventions.interventionSource
import com.example.interventions.planningEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalTime

class InterventionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var current: Int = 0
        get() = uiState.value.current

    private var currentBinder: Int = -1
        get() = uiState.value.currentBinder

    private var viewing: Int = -1

    private var showingDone: Boolean = false
    private var interventions: MutableList<Intervention> = planningEngine()
    private var showingInterventions: MutableList<Intervention> = interventions

    init {
        _uiState.update { currentState ->
            currentState.copy( interventions = interventions)
        }
    }

    private fun resetUiState() {
        interventions = planningEngine()
        _uiState.value = UiState(
            current = 0,
            viewing = -1,
            interventions = interventions,
            noFoodOrSupplementsEndTime = LocalTime.MIN,
            currentBinder = -1,
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
                            currentBinder = intervention.id,
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
                _uiState.update { currentState ->
                    currentState.copy(
                        currentBinder = -1,
                        noFoodOrSupplementsEndTime = LocalTime.MIN,
                        interventions = showingInterventions,
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