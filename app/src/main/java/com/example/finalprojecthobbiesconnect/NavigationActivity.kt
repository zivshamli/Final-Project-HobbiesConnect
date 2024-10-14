package com.example.finalprojecthobbiesconnect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.finalprojecthobbiesconnect.databinding.ActivityNavigationBinding
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragmentActivityNavigation.id) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_profile, R.id.navigation_search, R.id.navigation_notifications, R.id.navigation_chats
            )
        )
        initNavigation()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun initNavigation() {
        val navigation=intent.getIntExtra(Constants.NAVIGATION_KEY,0)
        if(navigation!=0){
            when(navigation){
                Constants.SEARCH_FRAGMENT->binding.navView.selectedItemId=R.id.navigation_search
            }
        }

    }
}