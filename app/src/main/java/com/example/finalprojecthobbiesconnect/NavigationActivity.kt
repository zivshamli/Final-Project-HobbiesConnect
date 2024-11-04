package com.example.finalprojecthobbiesconnect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.finalprojecthobbiesconnect.databinding.ActivityNavigationBinding
import com.example.finalprojecthobbiesconnect.models.Chats
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.example.finalprojecthobbiesconnect.utilties.SoundManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding
    private lateinit var badgePendingNotification: BadgeDrawable
    private lateinit var badgeReadChat: BadgeDrawable
    private lateinit var navController: NavController
    private  val soundManager: SoundManager = SoundManager(this)

     private  val database=   FirebaseDatabase.getInstance()


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
        initBadgeReadChat()
        listenerOfNavBar()
        initNavigation()

    }

    private fun initBadgeReadChat() {
        badgeReadChat = binding.navView.getOrCreateBadge(R.id.navigation_chats)
        badgeReadChat.isVisible = false
        listenForReadChat()
    }

    private fun initBadgeNotifications() {
         badgePendingNotification = binding.navView.getOrCreateBadge(R.id.navigation_notifications)
        badgePendingNotification.isVisible = false
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

        val databaseRef = FirebaseDatabase.getInstance().reference.child(Constants.USERS_REF).child(
            MyActiveUserManager.getUser().email.replace(".",",")).child(Constants.READ_PENDING_REF)

        databaseRef.setValue(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                badgePendingNotification.isVisible = false
            } else {
                SignalManager.getInstance().vibrateAndToast(Constants.ALERT_SAVE_USER)
            }


        }
    }


     private fun listenForPendingStatus() {
        val userRef =database.reference.child(Constants.USERS_REF).child(
            MyActiveUserManager.getUser().email.replace(".",",")).child(Constants.READ_PENDING_REF)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val readPend = snapshot.getValue(Boolean::class.java)
                if (readPend != null) {
                    if (!readPend) {
                        if(::binding.isInitialized) {
                            badgePendingNotification.isVisible = true
                            val app = applicationContext as App

                            if(!isFinishing&&!isDestroyed&&app.read_notification_flag) {
                                soundManager.playSound(R.raw.notification_pending_friend_request_sound)
                                app.read_notification_flag=false

                            }

                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                SignalManager.getInstance().vibrateAndToast(Constants.ALERT_LOAD_USER)
            }
        })

        }

    private fun listenForReadChat(){
        val chatRef=database.getReference(Constants.CHATS_REF)
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatRooms = snapshot.children.mapNotNull {
                    it.getValue(Chats::class.java)
                }.filter {
                    it.participantsStatus.keys.contains(
                        MyActiveUserManager.getUser().email.replace(".", ","))
                            &&
                            it.participantsStatus[MyActiveUserManager.getUser().email
                                .replace(".", ",")] == false
                }
                if (::binding.isInitialized)
                {
                    badgeReadChat.isVisible = chatRooms.isNotEmpty()
                    val app = applicationContext as App
                    if (chatRooms.isNotEmpty()&&!isFinishing&&!isDestroyed&&app.read_chat_flag) {
                        soundManager.playSound(R.raw.chat_notfication_sound)
                        app.read_chat_flag=false

                    }
                }
            }





            override fun onCancelled(error: DatabaseError) {
               SignalManager.getInstance().vibrateAndToast(Constants.ALERT_LOAD_CHATS)
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
                Constants.CHATS_FRAGMENT->binding.navView.selectedItemId=R.id.navigation_chats

            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        soundManager.stopSound()
    }
    override fun onPause() {
        super.onPause()
        soundManager.stopSound()
    }
}