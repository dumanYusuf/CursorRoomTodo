package com.dumanyusuf.cursorroom.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val dueDate: Long, // Bitiş tarihi
    val priority: Priority = Priority.NORMAL
) {
    // Öncelik seviyeleri
    enum class Priority {
        LOW, NORMAL, HIGH
    }

    // Görevin gecikip gecikmediğini kontrol eden fonksiyon
    fun isOverdue(): Boolean {
        val now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return !isCompleted && dueDate < now
    }

    // Kalan süreyi hesaplayan fonksiyon
    fun getRemainingTime(): Long {
        val now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return dueDate - now
    }
} 