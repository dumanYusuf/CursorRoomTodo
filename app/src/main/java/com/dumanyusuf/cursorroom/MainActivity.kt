package com.dumanyusuf.cursorroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dumanyusuf.cursorroom.data.TodoDatabase
import com.dumanyusuf.cursorroom.repository.TodoRepository
import com.dumanyusuf.cursorroom.ui.TodoScreen
import com.dumanyusuf.cursorroom.ui.TodoDetailScreen
import com.dumanyusuf.cursorroom.ui.theme.CursorRoomTheme
import com.dumanyusuf.cursorroom.viewmodel.TodoViewModel
import androidx.compose.runtime.*
import com.dumanyusuf.cursorroom.data.Todo

class MainActivity : ComponentActivity() {
    private val database by lazy { TodoDatabase.getDatabase(this) }
    private val repository by lazy { TodoRepository(database.todoDao()) }
    private val viewModel by viewModels<TodoViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TodoViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CursorRoomTheme {
                var currentTodo by remember { mutableStateOf<Todo?>(null) }
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (currentTodo != null) {
                        TodoDetailScreen(
                            todo = currentTodo!!,
                            viewModel = viewModel,
                            onNavigateBack = { currentTodo = null }
                        )
                    } else {
                        TodoScreen(
                            viewModel = viewModel,
                            onNavigateToDetail = { todo -> currentTodo = todo }
                        )
                    }
                }
            }
        }
    }
}