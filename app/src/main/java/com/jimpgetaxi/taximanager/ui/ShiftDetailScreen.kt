package com.jimpgetaxi.taximanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jimpgetaxi.taximanager.ui.components.ActivityRow
import com.jimpgetaxi.taximanager.ui.components.AmbientBackground
import com.jimpgetaxi.taximanager.ui.theme.*
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
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showEditShift by remember { mutableStateOf(false) }
    var selectedActivity by remember { mutableStateOf<ActivityItem?>(null) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        AmbientBackground()

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Λεπτομέρειες Βάρδιας") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Πίσω")
                    }
                },
                actions = {
                    IconButton(onClick = { showEditShift = true }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Επεξεργασία")
                    }
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Διαγραφή", tint = NegativeRed)
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
                    Text("Κόστος/Km: ${s.costPerKm} €", color = TextSecondary)
                    Text("Κόστος Φθοράς: ${s.vehicleCost ?: "-"} €", color = TextSecondary)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Δραστηριότητα Βάρδιας", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val activities by viewModel.getRecentActivityForShift(shiftId).collectAsState(initial = emptyList())
                    
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(activities) { activity ->
                            ActivityRow(item = activity, onClick = { selectedActivity = activity })
                        }
                    }
                }
            } ?: run {
                Text("Φόρτωση...", modifier = Modifier.padding(16.dp), color = TextSecondary)
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Διαγραφή Βάρδιας", color = TextPrimary) },
            text = { Text("Είστε σίγουροι; Θα διαγραφούν οριστικά η βάρδια, καθώς και όλες οι κούρσες και τα έξοδα που περιέχει.", color = TextSecondary) },
            containerColor = BackgroundDark,
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteShift(shiftId)
                    showDeleteConfirm = false
                    onBack()
                }) {
                    Text("Διαγραφή", color = NegativeRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Ακύρωση", color = TextSecondary)
                }
            }
        )
    }

    if (showEditShift && shift != null) {
        var startOdo by remember { mutableStateOf(shift!!.startOdometer.toString()) }
        var endOdo by remember { mutableStateOf(shift!!.endOdometer?.toString() ?: "") }
        var costKm by remember { mutableStateOf(shift!!.costPerKm.toString()) }
        
        val autoVehCost = remember(startOdo, endOdo, costKm) {
            val s = startOdo.replace(",", ".").toDoubleOrNull() ?: 0.0
            val e = endOdo.replace(",", ".").toDoubleOrNull() ?: 0.0
            val c = costKm.replace(",", ".").toDoubleOrNull() ?: 0.0
            if (e > s) {
                kotlin.math.round((e - s) * c * 10) / 10.0
            } else {
                0.0
            }
        }
        var vehCost by remember(autoVehCost) { mutableStateOf(autoVehCost.toString()) }

        AlertDialog(
            onDismissRequest = { showEditShift = false },
            title = { Text("Επεξεργασία", color = TextPrimary) },
            containerColor = BackgroundDark,
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = startOdo,
                        onValueChange = { startOdo = it },
                        label = { Text("Αρχικά Χλμ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                    OutlinedTextField(
                        value = endOdo,
                        onValueChange = { endOdo = it },
                        label = { Text("Τελικά Χλμ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                    OutlinedTextField(
                        value = costKm,
                        onValueChange = { costKm = it },
                        label = { Text("Κόστος/Km") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                    OutlinedTextField(
                        value = vehCost,
                        onValueChange = { vehCost = it },
                        label = { Text("Κόστος Φθοράς (€) (Αυτόματα)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateShiftDetails(
                        shiftId = shiftId,
                        startOdo = startOdo.replace(",", ".").toDoubleOrNull() ?: shift!!.startOdometer,
                        endOdo = endOdo.replace(",", ".").toDoubleOrNull(),
                        costPerKm = costKm.replace(",", ".").toDoubleOrNull() ?: shift!!.costPerKm,
                        vehicleCost = vehCost.replace(",", ".").toDoubleOrNull()
                    )
                    showEditShift = false
                }) {
                    Text("Αποθήκευση", color = BrandAccent)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditShift = false }) {
                    Text("Ακύρωση", color = TextSecondary)
                }
            }
        )
    }

    selectedActivity?.let { activity ->
        var amount by remember { mutableStateOf(activity.amount.toString()) }
        // For rides, subtitle holds receipt amount
        var receiptStr by remember { mutableStateOf(
            if (activity.category == "ride") activity.subtitle.replace(Regex("[^0-9.,]"), "").replace(",", ".") else ""
        ) }
        
        AlertDialog(
            onDismissRequest = { selectedActivity = null },
            title = { Text("Επεξεργασία", color = TextPrimary) },
            containerColor = BackgroundDark,
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Στοιχείο: ${activity.title}", color = TextSecondary)
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text(if (activity.category == "ride") "Είσπραξη (€)" else "Ποσό (€)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                    if (activity.category == "ride") {
                        OutlinedTextField(
                            value = receiptStr,
                            onValueChange = { receiptStr = it },
                            label = { Text("Απόδειξη (€)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                        )
                    }
                }
            },
            confirmButton = {
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = {
                        viewModel.deleteActivityItem(activity)
                        selectedActivity = null
                    }) {
                        Text("Διαγραφή", color = NegativeRed)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        viewModel.updateActivityItem(
                            activity,
                            amount.replace(",", ".").toDoubleOrNull() ?: activity.amount,
                            if (activity.category == "ride") receiptStr.replace(",", ".").toDoubleOrNull() else null
                        )
                        selectedActivity = null
                    }) {
                        Text("Αποθήκευση", color = BrandAccent)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedActivity = null }) {
                    Text("Ακύρωση", color = TextSecondary)
                }
            }
        )
    }
}
