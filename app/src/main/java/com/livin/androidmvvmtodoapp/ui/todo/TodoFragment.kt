package com.livin.androidmvvmtodoapp.ui.todo

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.livin.androidmvvmtodoapp.R
import com.livin.androidmvvmtodoapp.data.local.TodoItem
import com.livin.androidmvvmtodoapp.databinding.FragmentTodoBinding
import com.livin.androidmvvmtodoapp.other.Constants.TODO_TYPE_DAILY
import com.livin.androidmvvmtodoapp.other.Constants.TODO_TYPE_WEEKLY
import com.livin.androidmvvmtodoapp.receiver.AlarmReceiver
import com.livin.androidmvvmtodoapp.utils.Converters.fromTimestampToDate
import com.livin.androidmvvmtodoapp.utils.Converters.fromTimestampToTime
import com.livin.androidmvvmtodoapp.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private val todoViewModel by viewModels<TodoViewModel>()
    private val args: TodoFragmentArgs by navArgs()
    private var calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val todoId = args.todoId
        if (todoId != 0) {
            binding.buttonAdd.text = getString(R.string.label_update)
            binding.buttonDelete.visibility = View.VISIBLE
            todoViewModel.getTodoDetails(todoId)
        }
        collectLatestLifecycleFlow(todoViewModel.todoDetailsState) {
            when (it) {
                is TodoViewModel.TodoUiState.Error -> {
                    showErrorMessage(it.message)
                    setProgressVisibility(false)
                }
                is TodoViewModel.TodoUiState.Loading -> {
                    setProgressVisibility(true)
                }
                is TodoViewModel.TodoUiState.Success -> {
                    setData(it.data)
                    setProgressVisibility(false)
                }
                TodoViewModel.TodoUiState.Pop -> {
                    activity?.onBackPressed()
                }
            }
        }

        collectLatestLifecycleFlow(todoViewModel.todoCrudState) {
            when (it) {
                is TodoViewModel.TodoUiState.Error -> {
                    showErrorMessage(it.message)
                    setProgressVisibility(false)
                }
                is TodoViewModel.TodoUiState.Loading -> {
                    setProgressVisibility(true)
                }
                is TodoViewModel.TodoUiState.Success -> {
                    setProgressVisibility(false)
                    setUpAlarm(it.data)
                    activity?.onBackPressed()
                }
                TodoViewModel.TodoUiState.Pop -> {
                    activity?.onBackPressed()
                }
            }
        }

        binding.textViewDate.setOnClickListener {
            showDatePicker()
        }

        binding.textViewTime.setOnClickListener {
            showTimePicker()
        }

        binding.buttonAdd.setOnClickListener {
            var id: Int? = todoId
            if (id == 0) {
                id = null
            }
            todoViewModel.insertTodoItem(
                id,
                binding.editTextTitle.text.toString(),
                binding.editTextDescription.text.toString(),
                binding.textViewDate.text.toString(),
                binding.textViewTime.text.toString(),
                calendar.timeInMillis,
                getTypeFromCheckedRadioButton()
            )
        }
        binding.buttonDelete.setOnClickListener {
            todoViewModel.deleteTodo(todoId)
        }
    }

    private fun setData(todoItem: TodoItem) {
        calendar.timeInMillis = todoItem.date
        val timeCalendar = Calendar.getInstance()
        timeCalendar.timeInMillis = todoItem.time
        calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
        binding.editTextTitle.setText(todoItem.title)
        binding.editTextDescription.setText(todoItem.description)
        binding.textViewDate.text = fromTimestampToDate(todoItem.date)
        binding.textViewTime.text = fromTimestampToTime(todoItem.time)
        when (todoItem.type) {
            TODO_TYPE_DAILY -> {
                binding.radioButtonDaily.isChecked = true
            }

            TODO_TYPE_WEEKLY -> {
                binding.radioButtonWeekly.isChecked = true
            }
        }
    }

    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dpd =
            DatePickerDialog(requireContext(), { _, yearSelected, monthSelected, daySelected ->
                calendar.set(Calendar.YEAR, yearSelected)
                calendar.set(Calendar.MONTH, monthSelected)
                calendar.set(Calendar.DAY_OF_MONTH, daySelected)
                binding.textViewDate.text = fromTimestampToDate(calendar.timeInMillis)
            }, year, month, day)
        dpd.datePicker.minDate = Calendar.getInstance().timeInMillis
        dpd.show()
    }

    private fun showTimePicker() {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(requireContext(), { _, hourSelected, minuteSelected ->
            calendar.set(Calendar.HOUR_OF_DAY, hourSelected)
            calendar.set(Calendar.MINUTE, minuteSelected)
            binding.textViewTime.text = fromTimestampToTime(calendar.timeInMillis)
        }, hour, minute, false)
        tpd.show()
    }

    private fun getTypeFromCheckedRadioButton(): Int {
        return when (binding.radioGroupType.checkedRadioButtonId) {
            R.id.radioButtonDaily -> {
                TODO_TYPE_DAILY
            }
            R.id.radioButtonWeekly -> {
                TODO_TYPE_WEEKLY
            }
            else -> {
                return -1
            }
        }
    }

    private fun setProgressVisibility(showProgress: Boolean) {
        with(binding) {
            if (showProgress) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showErrorMessage(message: String) {
        Snackbar
            .make(
                binding.root,
                message,
                Snackbar.LENGTH_LONG
            )
            .show()
    }

    private fun setUpAlarm(todoItem: TodoItem) {
        val alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->

            var flags = PendingIntent.FLAG_UPDATE_CURRENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            }
            intent.apply {
                putExtra("todo_id", todoItem.id)
            }
            PendingIntent.getBroadcast(
                context, todoItem.id ?: -1, intent,
                flags
            )
        }

        var interval = AlarmManager.INTERVAL_DAY
        if (todoItem.type == TODO_TYPE_WEEKLY) {
            interval = AlarmManager.INTERVAL_DAY * 7
        }
        calendar.set(Calendar.SECOND, 0)
        alarmMgr.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            interval,
            alarmIntent
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}