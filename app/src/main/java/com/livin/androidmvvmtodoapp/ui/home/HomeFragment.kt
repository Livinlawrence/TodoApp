package com.livin.androidmvvmtodoapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.livin.androidmvvmtodoapp.R
import com.livin.androidmvvmtodoapp.data.local.TodoItem
import com.livin.androidmvvmtodoapp.databinding.FragmentHomeBinding
import com.livin.androidmvvmtodoapp.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: TodoItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TodoItemsAdapter(onTodoItemClick = { todoItem ->
            val bundle = bundleOf("todo_id" to todoItem.id)
            findNavController().navigate(
                R.id.action_navigation_add_edit_todo,
                bundle
            )
        })
        binding.recyclerTodo.adapter = adapter
        collectLatestLifecycleFlow(homeViewModel.fullTodoItems()) {
            setData(it)
            setInfoMessageVisibility(it.isEmpty())
        }
        binding.fabAddTodo.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_add_edit_todo
            )
        }
    }

    private fun setData(list: List<TodoItem>) {
        adapter.updateList(list)
    }

    private fun setInfoMessageVisibility(showProgress: Boolean) {
        with(binding) {
            if (showProgress) {
                textViewInfo.visibility = View.VISIBLE
            } else {
                textViewInfo.visibility = View.GONE
            }
        }
    }
}