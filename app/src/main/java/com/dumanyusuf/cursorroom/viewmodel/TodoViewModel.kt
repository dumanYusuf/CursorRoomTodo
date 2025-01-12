package com.dumanyusuf.cursorroom.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumanyusuf.cursorroom.data.Todo
import com.dumanyusuf.cursorroom.repository.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val allTodos: StateFlow<List<Todo>> = _todos

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.allTodos.collect { todos ->
                _todos.value = todos
            }
        }
    }

    val searchResults: StateFlow<List<Todo>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank() || !_isSearchActive.value) {
                repository.allTodos
            } else {
                repository.searchTodos(query)
            }
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    fun addTodo(title: String, description: String, dueDate: Long, priority: Todo.Priority) {
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(todo)
        }
    }

    fun updateTodoDetails(todoId: Int, title: String, description: String, dueDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
    }
} 