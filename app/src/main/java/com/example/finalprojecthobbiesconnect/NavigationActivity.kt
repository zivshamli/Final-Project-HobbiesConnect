package com.example.finalprojecthobbiesconnect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.finalprojecthobbiesconnect.databinding.ActivityNavigationBinding
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding
    private lateinit var badge: BadgeDrawable
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragmentActivityNavigation.id) as NavHostFragment
         navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_profile,R.id.navigation_friend_list ,R.id.navigation_search, R.id.navigation_notifications, R.id.navigation_chats
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        initBadgeNotifications()
        listenerOfNavBar()
        initNavigation()

    }

    private fun initBadgeNotifications() {
         badge = binding.navView.getOrCreateBadge(R.id.navigation_notifications)
        badge.isVisible = false
        listenForPendingStatus()



    }

    private fun listenerOfNavBar() {
        binding.navView.setOnItemSelectedListener {item->

           when(item.itemId){
               R.id.navigation_notifications->{

                  updateFireBaseReadStatus()
                  navController.navigate(R.id.navigation_notifications)
                   true
               }
               R.id.navigation_profile->{
                   navController.navigate(R.id.navigation_profile)
                   true
               }
               R.id.navigation_search->{
                   navController.navigate(R.id.navigation_search)
                   true
               }
               R.id.navigation_chats->{
                   navController.navigate(R.id.navigation_chats)
                   true

            }
               R.id.navigation_friend_list->{
                   navController.navigate(R.id.navigation_friend_list)
                   true
               }
            else -> false
               }
        }
        }

    private fun updateFireBaseReadStatus() {
        MyActiveUserManager.getUser().isReadPend=true

        val databaseRef = FirebaseDatabase.getInstance().reference.child("users").child(
            MyActiveUserManager.getUser().email.replace(".",",")).child("readPend")

        databaseRef.setValue(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                badge.isVisible = false
            } else {
                SignalManager.getInstance().vibrateAndToast("Failed to save user details")
            }


        }
    }


    private fun listenForPendingStatus() {
        val databaseRef = FirebaseDatabase.getInstance().reference.child("users").child(
            MyActiveUserManager.getUser().email.replace(".",",")).child("readPend")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val readPend = snapshot.getValue(Boolean::class.java)
                if (readPend != null) {
                    if (!readPend) {
                        if(::binding.isInitialized) {
                            badge.isVisible = true
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        }

    private fun initNavigation() {
        val navigation=intent.getIntExtra(Constants.NAVIGATION_KEY,0)
        if(navigation!=0){
            when(navigation){
                Constants.SEARCH_FRAGMENT->binding.navView.selectedItemId=R.id.navigation_search
                Constants.NOTIFICATIONS_FRAGMENT->binding.navView.selectedItemId=R.id.navigation_notifications
                Constants.FRIEND_LIST_FRAGMENT->binding.navView.selectedItemId=R.id.navigation_friend_list

            }
        }

    }
}