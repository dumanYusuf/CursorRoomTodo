package com.dumanyusuf.cursorroom.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dumanyusuf.cursorroom.data.Todo

@Composable
fun TodoItem(
    todo: Todo,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                todo.isCompleted -> MaterialTheme.colorScheme.surfaceVariant
                todo.isOverdue() -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Öncelik göstergesi
                        Icon(
                            imageVector = when (todo.priority) {
                                Todo.Priority.HIGH -> Icons.Default.Warning
                                Todo.Priority.NORMAL -> Icons.Default.CheckCircle
                                Todo.Priority.LOW -> Icons.Default.Delete
                            },
                            contentDescription = "Öncelik",
                            tint = when (todo.priority) {
                                Todo.Priority.HIGH -> MaterialTheme.colorScheme.error
                                Todo.Priority.NORMAL -> MaterialTheme.colorScheme.onSurface
                                Todo.Priority.LOW -> MaterialTheme.colorScheme.primary
                            },
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = todo.title,
                            style = MaterialTheme.typography.titleMedium,
                            textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null
                        )
                    }
                    
                    if (todo.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = todo.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (todo.isOverdue()) MaterialTheme.colorScheme.error 
                                  else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when {
                                todo.isCompleted -> "Tamamlandı"
                                todo.isOverdue() -> "Gecikmiş!"
                                else -> formatRemainingTime(todo.getRemainingTime())
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = if (todo.isOverdue()) MaterialTheme.colorScheme.error 
                                   else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Row {
                    IconButton(onClick = onToggleComplete) {
                        Icon(
                            imageVector = if (todo.isCompleted) 
                                Icons.Default.CheckCircle 
                            else 
                                Icons.Default.CheckCircle,
                            contentDescription = "Tamamlandı",
                            tint = if (todo.isCompleted) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Sil",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

private fun formatRemainingTime(remainingMillis: Long): String {
    val days = remainingMillis / (1000 * 60 * 60 * 24)
    val hours = (remainingMillis % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
    
    return when {
        days > 0 -> "$days gün kaldı"
        hours > 0 -> "$hours saat kaldı"
        else -> "Bugün son gün!"
    }
} 