package com.livin.androidmvvmtodoapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.livin.androidmvvmtodoapp.R
import com.livin.androidmvvmtodoapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigationGraph()
    }

    private fun setNavigationGraph() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment)
                .navController
        val graph = navController
            .navInflater.inflate(R.navigation.app_navigation)
        val appBarConfiguration = AppBarConfiguration(graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}