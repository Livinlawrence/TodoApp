package com.livin.androidmvvmtodoapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livin.androidmvvmtodoapp.data.local.TodoItem
import com.livin.androidmvvmtodoapp.databinding.ItemTodoBinding


class TodoItemsAdapter(
    private var todoItems: List<TodoItem> = mutableListOf(),
    private val onTodoItemClick: (TodoItem) -> Unit
) : RecyclerView.Adapter<ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = ItemTodoBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding, onTodoItemClick)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todoItem = todoItems[position]
        holder.bind(todoItem)
    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    fun updateList(todoItems: List<TodoItem>) {
        this.todoItems = todoItems
        notifyItemChanged(todoItems.size)
    }
}