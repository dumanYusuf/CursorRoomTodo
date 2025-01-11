package com.dumanyusuf.cursorroom.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumanyusuf.cursorroom.data.Todo
import com.dumanyusuf.cursorroom.repository.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    val allTodos: StateFlow<List<Todo>> = repository.allTodos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addTodo(title: String, description: String, dueDate: Long, priority: Todo.Priority) {
        viewModelScope.launch {
            if (title.isNotBlank()) {
                repository.insertTodo(
                    Todo(
                        title = title,
                        description = description,
                        dueDate = dueDate,
                        priority = priority
                    )
                )
            }
        }
    }

    fun toggleTodoStatus(todo: Todo) {
        viewModelScope.launch {
            repository.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }

    fun updateTodoDetails(todoId: Int, title: String, description: String, dueDate: Long) {
        viewModelScope.launch {
            if (title.isNotBlank()) {
                val currentTodos = allTodos.value
                val todo = currentTodos.find { it.id == todoId }
                todo?.let {
                    repository.updateTodo(
                        it.copy(
                            title = title,
                            description = description,
                            dueDate = dueDate,
                            priority = it.priority,
                            isCompleted = it.isCompleted
                        )
                    )
                }
            }
        }
    }
} 