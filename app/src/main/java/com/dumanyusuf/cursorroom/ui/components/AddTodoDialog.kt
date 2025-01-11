package com.dumanyusuf.cursorroom.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dumanyusuf.cursorroom.data.Todo
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Long, Todo.Priority) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var priority by remember { mutableStateOf(Todo.Priority.NORMAL) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Yeni Todo",
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Başlık") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Açıklama") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Öncelik seçici
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Öncelik:")
                    Row {
                        PriorityButton(
                            priority = Todo.Priority.LOW,
                            selected = priority == Todo.Priority.LOW,
                            onClick = { priority = Todo.Priority.LOW }
                        )
                        PriorityButton(
                            priority = Todo.Priority.NORMAL,
                            selected = priority == Todo.Priority.NORMAL,
                            onClick = { priority = Todo.Priority.NORMAL }
                        )
                        PriorityButton(
                            priority = Todo.Priority.HIGH,
                            selected = priority == Todo.Priority.HIGH,
                            onClick = { priority = Todo.Priority.HIGH }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Son Tarih: ${
                            SimpleDateFormat("dd MMMM yyyy", Locale("tr"))
                                .format(Date(selectedDate))
                        }"
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("İptal")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onConfirm(title, description, selectedDate, priority)
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Text("Ekle")
                    }
                }
            }
        }
    }
    
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = { 
                        datePickerState.selectedDateMillis?.let { 
                            selectedDate = it 
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Tamam")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }
}

@Composable
fun PriorityButton(
    priority: Todo.Priority,
    selected: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = when (priority) {
                Todo.Priority.HIGH -> Icons.Default.Warning
                Todo.Priority.NORMAL -> Icons.Default.CheckCircle
                Todo.Priority.LOW -> Icons.Default.Delete
            },
            contentDescription = when (priority) {
                Todo.Priority.HIGH -> "Yüksek Öncelik"
                Todo.Priority.NORMAL -> "Normal Öncelik"
                Todo.Priority.LOW -> "Düşük Öncelik"
            },
            tint = if (selected) {
                when (priority) {
                    Todo.Priority.HIGH -> MaterialTheme.colorScheme.error
                    Todo.Priority.NORMAL -> MaterialTheme.colorScheme.primary
                    Todo.Priority.LOW -> MaterialTheme.colorScheme.secondary
                }
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
} 