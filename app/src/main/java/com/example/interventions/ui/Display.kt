package com.example.interventions.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.interventions.Intervention
import com.example.interventions.ui.theme.InterventionsTheme
import java.time.LocalTime
import java.util.Objects.isNull
import kotlin.time.toKotlinDuration


//fun createSchedule() : List<Intervention> {
//    return
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterventionApplication(
    viewModel: InterventionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    InterventionsTheme {
        Scaffold(
            modifier = Modifier,
            topBar = {},
            bottomBar = { BottomAppBar {
                Button(
                    onClick = { viewModel.toggleShowDone() }
                ) {
                    Text( uiState.showDoneButtonText )
                }
            } }
        ) { innerPadding ->
            InterventionList(
                current = uiState.current,
                interventions = uiState.interventions,
                itemcount = uiState.interventions.size,
//                clickCurrent = uiState.clickCurrent,
//                clickAction = viewModel.clickAction,
                currentBinder = uiState.currentBinder,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
fun InterventionList(
    current: Int,
    itemcount: Int,
//    viewing: Int,
//    scheduled: MutableList<Intervention>,
//    clickAction: (Int) -> Unit,
//    clickCurrent: Int,
    interventions: List<Intervention>,
    currentBinder: Intervention?,
    modifier: Modifier = Modifier
) {

//    val viewing = rememberSaveable { mutableIntStateOf(-1) }

    LazyColumn(
        modifier
            .background(Color.Black)
    ) {
//        item { Text( text = "Item Count: $itemcount", modifier = Modifier.background(Color.Cyan)) }
//        item { Text( text = "Current: $current", modifier = Modifier.background(Color.Cyan)) }
//        item { Text( text = "Click Current: $clickCurrent", modifier = Modifier.background(Color.Cyan)) }
        if (null != currentBinder) {
                item {
                    InterventionDisplay(
                        intervention = currentBinder,
//                onClickAction = { viewModel.clickAction() },
//                onLongClickAction = {
//                    viewing.intValue = -1
//                    current = intervention.id
//                },
                        current = current,
//                viewing = viewing.intValue,
//                clickAction = clickAction,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
            }
        }
        items(
            items = interventions,
            key = null,
        ) { intervention ->
//            val active = current.intValue == intervention.id
            if (intervention.id != currentBinder?.id) {
                InterventionDisplay(
                    intervention = intervention,
//                onClickAction = { viewModel.clickAction() },
//                onLongClickAction = {
//                    viewing.intValue = -1
//                    current = intervention.id
//                },
                    current = current,
//                viewing = viewing.intValue,
//                clickAction = clickAction,
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InterventionDisplay(
    viewModel: InterventionViewModel = viewModel(),
    intervention: Intervention,
//    onClickAction: () -> Unit,
//    onLongClickAction: () -> Unit,
    current: Int,
//    viewing: Int,
//    clickAction: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    val active = current == intervention.id
    val view = uiState.viewing == intervention.id
    var waiting = false

    if (active and !isNull(intervention.duration)) {
        waiting = true
        if (isNull(intervention.startTime)) {
            intervention.startTime = LocalTime.now()
        }
    }

//    val weight = if (active or view) FontWeight.Bold else FontWeight.Normal
//    val color = when {
//        intervention.done -> Color.Gray
//        active -> Color.White
//        else -> Color.Black
//    }
//    val background = when {
//        intervention.done -> Color.LightGray
//        intervention.isBinder and (intervention.earliestBinder > LocalTime.MIN) -> Color.Red
//        (uiState.noFoodOrSupplementsEndTime > LocalTime.now()) and (intervention.isSupplement or intervention.isFood) -> Color(0xFFFFC0CB)
//        active -> Color.Blue
//        view -> Color.Green
//        else -> Color.White
//    }
//    val height = if (active or view) 30.sp else 20.sp
//    val padding = if (active or view) 8.dp else 4.dp

    if (waiting) {
        val endTime = intervention.startTime?.plus(intervention.duration)
    }

    var ui = viewModel.ui(intervention)

    Card(
        colors = CardDefaults.cardColors(
            contentColor = ui.color,
            containerColor = ui.background
        ),
        modifier = modifier
            .background(ui.background)
            .combinedClickable(
                enabled = true,
                onClick = { viewModel.clickAction(intervention.id) },
                onLongClick = { viewModel.longClickAction(intervention.id) },
            )
    ) {
        Text(
            text = "${intervention.id} - ${intervention.text}",
            fontWeight = ui.weight,
            lineHeight = ui.lineHeight,
            modifier = modifier
                .padding(ui.padding)
                .fillMaxWidth(),
        )
        if (active or view) {
            intervention.extraText.forEach {
                Text(
                    text = " - $it"
                )
            }
        }
        if (view and !isNull( intervention.targetTime)) {
            Text( " - Time: ${intervention.targetTime}" )
        }
        if (view and !isNull( intervention.duration)) {
            Text( " - Duration: ${intervention.duration?.toKotlinDuration()}" )
        }
        if (waiting) {
            Text(" - Started: ${intervention.startTime}")
        }
        if (intervention.isBinder and (intervention.earliestBinder > LocalTime.MIN)) {
            Text("Earliest time for binder: ${intervention.earliestBinder}")
            Text("Earliest time for next food: ${uiState.noFoodOrSupplementsEndTime}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InterventionDisplayPreview() {
    InterventionsTheme {
        InterventionDisplay(
            intervention = Intervention( id = 17, text = "Floober").extra("Joining in is hard to do"),
//            onClickAction = {},
//            onLongClickAction = {},
            current = 16,
//            viewing = -1,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InterventionDisplayCurrentPreview() {
    InterventionsTheme {
        InterventionDisplay(
            intervention = Intervention( id = 17, text = "Floober").extra("Joining in is hard to do"),
//            onClickAction = {},
//            onLongClickAction = {},
            current = 17,
//            viewing = -1,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InterventionDisplayViewingReview() {
    InterventionsTheme {
        InterventionDisplay(
            intervention = Intervention( id = 17, text = "Floober").extra("Joining in is hard to do"),
//            onClickAction = {},
//            onLongClickAction = {},
            current = 1,
//            viewing = 17,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InterventionDisplayViewingTimeReview() {
    InterventionsTheme {
        InterventionDisplay(
            intervention = Intervention( id = 17, text = "Floober").time("17:00"),
//            onClickAction = {},
//            onLongClickAction = {},
            current = 1,
//            viewing = 17,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InterventionDisplayViewingDurationReview() {
    InterventionsTheme {
        InterventionDisplay(
            intervention = Intervention( id = 17, text = "Floober").duration("PT2H17M"),
//            onClickAction = {},
//            onLongClickAction = {},
            current = 1,
//            viewing = 17,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InterventionsTheme {
        InterventionApplication()
    }
}

//@Preview(showBackground = true)
//@Composable
//fun InterventionPreview() {
//    InterventionsTheme {
//        InterventionList()
//    }
//}
