package com.example.interventions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.interventions.ui.theme.InterventionsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InterventionApplication()
        }
    }
}

@Composable
fun InterventionApplication() {
    InterventionsTheme {
        Scaffold(
            modifier = Modifier,
//            topBar = {},
//            bottomBar = {}
        ) { innerPadding ->
            InterventionList(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

data class Intervention(
    val text: String,
    val id: Int = -1,
    val extraText: MutableList<String> = mutableListOf()
) {
    fun extra( text: String): Intervention {
        extraText.add(text)
        return this
    }
}

@Composable
fun InterventionList(modifier: Modifier = Modifier) {
    val interventionStrings = listOf<Intervention>(
//        "",
        Intervention( "Wake up"),
        Intervention( "Artemisinin"),
        Intervention( "BitterX").extra("Six squirts").extra("Let absorb through mouth for 60-90 seconds before swallowing"),
        Intervention( "C-RLA").extra("away from food so it absorbs better"),
        Intervention( "Wait one hour"),
        Intervention( "Ultra Binder"),
        Intervention( "Wait two hours"),
        Intervention( "Morning pills").extra("Includes probiotic - two hours apart from Nystatin"),
        Intervention( "Glutathione"),
        Intervention( "Make Breakfast"),
        Intervention( "Take mushroom pills").extra("20 minutes before eating"),
        Intervention( "Eat breakfast"),
        Intervention( "Pills after breakfast"),
        Intervention( "Pills after last food"),
        Intervention( "Wait one hour"),
        Intervention( "Ultra Binder"),
        Intervention( "Wait two hours"),
        Intervention( "BitterX"),
        Intervention( "Last pills"),
        Intervention( "Glutathione"),
    )

    val interventions: List<Intervention> = interventionStrings.mapIndexed { index, it ->
        it.copy( id = index)
    }

    fun currentIntervention(interventionId: Int): Int {
        return if (interventionId == interventions.size - 1) 0
        else interventionId + 1
    }

    val current = rememberSaveable { mutableIntStateOf(0) }
    val viewing = rememberSaveable { mutableIntStateOf(-1) }

    LazyColumn(
        modifier
            .background(Color.Black)
//            .padding(1.dp)
    ) {
        items(
            items = interventions,
            key = null,
        ) { intervention ->
            val active = current.intValue == intervention.id
            InterventionDisplay(
                intervention = intervention,
                onClickAction = {
                    if (viewing.intValue == intervention.id) {
                        viewing.intValue = -1
                    }
                    else if (current.intValue == intervention.id) {
                        current.intValue = currentIntervention(intervention.id)
                        viewing.intValue = -1
                    }
                    else
                        viewing.intValue = intervention.id
                                },
                onLongClickAction = {
                    viewing.intValue = -1
                    current.intValue = intervention.id
                                    },
//                modifier = Modifier,
//                highlight = intervention.id == current
                current = current.intValue,
                viewing = viewing.intValue,
                modifier = Modifier.padding(vertical = 3.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InterventionDisplay(
    intervention: Intervention,
    onClickAction: () -> Unit,
    onLongClickAction: () -> Unit,
//    highlight: Boolean
    current: Int,
    viewing: Int,
    modifier: Modifier = Modifier,
) {
    val active = current == intervention.id
    val view = viewing == intervention.id
    val weight = if (active or view) FontWeight.Bold else FontWeight.Normal
    val color = if (active) Color.White else Color.Black
    val background = if (active) Color.Blue else if (view) Color.Green else Color.White
    val height = if (active or view) 30.sp else 20.sp
    val padding = if (active or view) 8.dp else 4.dp
    Card(
        colors = CardDefaults.cardColors(
            contentColor = color,
            containerColor = background
        ),
        modifier = modifier
            .background(background)
            .combinedClickable(
                enabled = true,
                onClick = onClickAction,
                onLongClick = onLongClickAction,
            )
//            .clickable(enabled = true, onClick = onClickAction)
    ) {
//        TextButton(
//            onClick = onClickAction,
//            modifier = modifier.clickable(enabled = true, onClick = onClickAction)
//        ) {
            Text(
    //            "${intervention.id} - ${intervention.text}",
                text = intervention.text,
                fontWeight = weight,
                lineHeight = height,
                modifier = modifier
                    .padding(padding)
//                    .clickable(enabled = true, onClick = onClickAction)
//                    .padding(vertical = 4.dp, horizontal = 8.dp)
                .fillMaxWidth(),
            )
        if (active or view) {
            intervention.extraText.forEach {
                Text(
                    text = " - $it"
                )
            }
        }
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InterventionsTheme {
        InterventionApplication()
    }
}

@Preview(showBackground = true)
@Composable
fun InterventionPreview() {
    InterventionsTheme {
        InterventionList()
    }
}

@Preview(showBackground = true)
@Composable
fun InterventionDisplayCurrentPreview() {
    InterventionsTheme {
        InterventionDisplay(
            intervention = Intervention( id = 17, text = "Floober").extra("Joining in is hard to do"),
            onClickAction = {},
            onLongClickAction = {},
            current = 17,
            viewing = -1,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InterventionDisplayViewingReview() {
    InterventionsTheme {
        InterventionDisplay(
            intervention = Intervention( id = 17, text = "Floober").extra("Joining in is hard to do"),
            onClickAction = {},
            onLongClickAction = {},
            current = 1,
            viewing = 17,
        )
    }
}
