package com.jimpgetaxi.taximanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jimpgetaxi.taximanager.ui.components.ActivityRow
import com.jimpgetaxi.taximanager.ui.components.AmbientBackground
import com.jimpgetaxi.taximanager.ui.theme.TextPrimary
import com.jimpgetaxi.taximanager.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftDetailScreen(
    shiftId: Int,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val shift by viewModel.getShiftById(shiftId).collectAsState(initial = null)
    
    Box(modifier = Modifier.fillMaxSize()) {
        AmbientBackground()

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Λεπτομέρειες Βάρδιας") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Πίσω")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )

            shift?.let { s ->
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val startStr = dateFormat.format(Date(s.startTime))
                val endStr = s.endTime?.let { dateFormat.format(Date(it)) } ?: "Ενεργή"

                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Έναρξη: $startStr", color = TextPrimary)
                    Text("Λήξη: $endStr", color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Αρχικά Χλμ: ${s.startOdometer}", color = TextSecondary)
                    Text("Τελικά Χλμ: ${s.endOdometer ?: "-"}", color = TextSecondary)
                    Text("Κόστος Φθοράς: ${s.vehicleCost ?: "-"} €", color = TextSecondary)
                    
                    // We can add editing forms later here!
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Δραστηριότητα Βάρδιας", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val activities by viewModel.getRecentActivityForShift(shiftId).collectAsState(initial = emptyList())
                    
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(activities) { activity ->
                            ActivityRow(item = activity)
                        }
                    }
                }
            } ?: run {
                Text("Φόρτωση...", modifier = Modifier.padding(16.dp), color = TextSecondary)
            }
        }
    }
}
