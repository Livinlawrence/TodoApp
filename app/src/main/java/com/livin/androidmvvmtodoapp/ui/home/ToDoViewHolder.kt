package com.livin.androidmvvmtodoapp.ui.home

import androidx.recyclerview.widget.RecyclerView
import com.livin.androidmvvmtodoapp.R
import com.livin.androidmvvmtodoapp.data.local.TodoItem
import com.livin.androidmvvmtodoapp.databinding.ItemTodoBinding
import com.livin.androidmvvmtodoapp.other.Constants.TODO_TYPE_DAILY
import com.livin.androidmvvmtodoapp.other.Constants.TODO_TYPE_WEEKLY
import com.livin.androidmvvmtodoapp.utils.Converters.fromTimestampToDate
import com.livin.androidmvvmtodoapp.utils.Converters.fromTimestampToTime

class ToDoViewHolder(
    private val binding: ItemTodoBinding,
    private val onTodoItemClick: (TodoItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(todoItem: TodoItem) {
        with(binding) {
            textViewTitle.text = todoItem.title
            textViewDescription.text = todoItem.description
            textViewType.text = getTodoType(todoItem.type)
            textViewDateTime.text = getDatetime(todoItem.date, todoItem.time)
            root.setOnClickListener {
                onTodoItemClick.invoke(todoItem)
            }
        }
    }

    private fun getDatetime(date: Long, time: Long): String {
        return fromTimestampToDate(date) + ", " + fromTimestampToTime(time)
    }

    private fun getTodoType(type: Int): String {
        return when (type) {
            TODO_TYPE_DAILY -> {
                binding.root.context.getString(R.string.label_daily)
            }

            TODO_TYPE_WEEKLY -> {
                binding.root.context.getString(R.string.label_weekly)
            }

            else -> ""
        }
    }
}