package com.example.finalprojecthobbiesconnect

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.finalprojecthobbiesconnect.databinding.ActivityProfileFriendBinding
import com.example.finalprojecthobbiesconnect.models.User
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.ImageLoader
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import com.example.finalprojecthobbiesconnect.utilties.OtherUserManager
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.google.android.material.chip.Chip
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate

class ProfileFriendActivity : AppCompatActivity() {
    private var navigation:Int = 0
    private lateinit var selectedHobbies: MutableList<String>
    private lateinit var binding: ActivityProfileFriendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        treatIntent()

        initViews()


        }

    private fun treatIntent() {
        navigation=intent.getIntExtra(Constants.NAVIGATION_KEY,0)

    }

    private fun initViews() {
        initProfileImage()
        initTextViews()
        initAddFriendBTN()
        initAddFriendBTNOnClick()
        initHobbiesChipGroup()
        initBackBTN()
        initMessageBTN()
    }

    private fun initMessageBTN() {
        binding.profileUserMessageBTN.setOnClickListener {
            changeActivityToChatRoom()
        }
    }

    private fun changeActivityToChatRoom() {
        val intent = Intent(this, ChatRoomActivity::class.java)
        val b = Bundle()
        b.putInt(Constants.NAVIGATION_KEY,Constants.PROFILE_ACTIVITY)
        b.putInt(Constants.NAVIGATION_KEY2,navigation)
        intent.putExtras(b)
        startActivity(intent)
        finish()
    }

    private fun initAddFriendBTNOnClick() {
        if((MyActiveUserManager.getUser().pendingFriendsList.contains(OtherUserManager.getInstance().getUser()?.email))||
            (OtherUserManager.getInstance().getUser()!!.pendingFriendsList.contains(MyActiveUserManager.getUser().email))){
            initAddFriendBTN()
        }
        else if(MyActiveUserManager.getUser().friendsList.contains(OtherUserManager.getInstance().getUser()?.email)) {
            binding.profileUserAddFriendBTN.setOnClickListener {removeFriends()}

        }
        else {
            binding.profileUserAddFriendBTN.setOnClickListener {addPendingFriend()}
        }

    }

    private fun addPendingFriend() {
        //add pending friend
        val updateUser=OtherUserManager.getInstance().getUser()
        if (updateUser != null){
            updateUser.pendingFriendsList.add(MyActiveUserManager.getUser().email)
            updateUser.isReadPend=false
            OtherUserManager.getInstance().setUser(updateUser)
            // save changes in database
            saveChangesAddPendingFriendAndReadInDatabase(updateUser)
        }





    }

    private fun saveChangesAddPendingFriendAndReadInDatabase(user: User) {
        val databaseRef = FirebaseDatabase.getInstance().reference.child("users").child(user.email.replace(".",","))
        val updates = hashMapOf(
            "pendingFriendsList" to user.pendingFriendsList,
            "readPend" to user.isReadPend
        )
        databaseRef.updateChildren(updates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                initAddFriendBTN()

            } else {
                SignalManager.getInstance().vibrateAndToast("Failed to save user details")

            }    }
    }



    private fun removeFriends() {
        MyActiveUserManager.getUser().friendsList.remove(OtherUserManager.getInstance().getUser()?.email)
        val updateUser=OtherUserManager.getInstance().getUser()
        if (updateUser != null){
        updateUser.friendsList.remove(MyActiveUserManager.getUser().email)
        OtherUserManager.getInstance().setUser(updateUser)

        saveChangesRemoveFriendOtherInDatabase(OtherUserManager.getInstance().getUser()!!)
        initAddFriendBTN()
      }
    }

    private fun saveChangesRemoveFriendInDatabase(user: User) {
        //save changes in database
        val databaseRef = FirebaseDatabase.getInstance().reference.child("users").child(user.email.replace(".",",")).child("friendsList")
        databaseRef.setValue(user.friendsList).addOnCompleteListener { task ->
            if (task.isSuccessful) {
               initAddFriendBTN()
                initAddFriendBTNOnClick()


            } else {
                SignalManager.getInstance().vibrateAndToast("Failed to save user details")
            }

        }
    }

    private fun saveChangesRemoveFriendOtherInDatabase(user: User) {
        //save changes in database
        val databaseRef = FirebaseDatabase.getInstance().reference.child("users").child(user.email.replace(".",",")).child("friendsList")
        databaseRef.setValue(user.friendsList).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                saveChangesRemoveFriendInDatabase(MyActiveUserManager.getUser())
            } else {
                SignalManager.getInstance().vibrateAndToast("Failed to save user details")
            }

        }



    }

    private fun initAddFriendBTN() {
        if((MyActiveUserManager.getUser().pendingFriendsList.contains(OtherUserManager.getInstance().getUser()?.email))||
            (OtherUserManager.getInstance().getUser()!!.pendingFriendsList.contains(MyActiveUserManager.getUser().email))){
            binding.profileUserAddFriendBTN.text=getString(R.string.pending)
            binding.profileUserAddFriendBTN.backgroundTintList=ContextCompat.getColorStateList(this,R.color.white)
            binding.profileUserAddFriendBTN.setTextColor(ContextCompat.getColor(this,R.color.black))
            binding.profileUserAddFriendBTN.isClickable=false
        }
        else if(MyActiveUserManager.getUser().friendsList.contains(OtherUserManager.getInstance().getUser()?.email)){
            binding.profileUserAddFriendBTN.text=getString(R.string.friends)
            binding.profileUserAddFriendBTN.backgroundTintList=ContextCompat.getColorStateList(this,R.color.white)
            binding.profileUserAddFriendBTN.setTextColor(ContextCompat.getColor(this,R.color.black))
            binding.profileUserMessageBTN.visibility=View.VISIBLE
            binding.profileUserAddFriendBTN.isClickable=true
        }
        else {
            binding.profileUserAddFriendBTN.text=getString(R.string.add_friend)
            binding.profileUserAddFriendBTN.backgroundTintList=ContextCompat.getColorStateList(this,R.color.blue_800)
            binding.profileUserAddFriendBTN.setTextColor(ContextCompat.getColor(this,R.color.white))
            binding.profileUserMessageBTN.visibility=View.GONE
            binding.profileUserAddFriendBTN.isClickable=true

        }
    }

    private fun initProfileImage() {
        val profileUrl = OtherUserManager.getInstance().getUser()?.profilePhoto
        if (profileUrl != null) {
            ImageLoader.getInstance().load(profileUrl, binding.profileUserPhoto, R.drawable.avatar)
        }
    }

    private fun initBackBTN() {
        binding.backBTN.setOnClickListener{backToActivity(navigation)}
    }

    private fun backToActivity(navigation: Int) {

            val intent = Intent(this, NavigationActivity::class.java)
            val b = Bundle()
            b.putInt(Constants.NAVIGATION_KEY, navigation)
            intent.putExtras(b)
            startActivity(intent)
            finish()


    }

    private fun initHobbiesChipGroup() {
        selectedHobbies=OtherUserManager.getInstance().getUser()?.hobbies ?: mutableListOf()
        Constants.HOBBIES_LIST.forEach { hobby ->
            val chip = Chip(this).apply {
                text = hobby
                isCheckable = true
                isClickable = false

            }
            chip.isChecked = selectedHobbies.contains(chip.text.toString())
            if(chip.isChecked){
                chip.setTextColor(ContextCompat.getColor(this,android.R.color.white))
                chip.chipBackgroundColor = ContextCompat.getColorStateList(this,R.color.blue_800)
            }
            else{
                chip.setTextColor(ContextCompat.getColor(this,android.R.color.black))
                chip.chipBackgroundColor = ContextCompat.getColorStateList(this,android.R.color.white)
            }
            binding.profileUserChipGroupHobbies.addView(chip)
        }
        refreshUserHobbiesText()

    }

    private fun refreshUserHobbiesText() {
        binding.profileUserMyHobbiesText.text = buildString {
            append("My hobbies: \n")
            selectedHobbies.forEach { hobby ->
                if (hobby != selectedHobbies.last())
                    append("$hobby , ")
                else {
                    append(hobby)
                }

            }
        }
    }

    private fun initTextViews() {
        binding.profileUserNameTextView.text= OtherUserManager.getInstance().getUser()?.username ?:""
        val age=OtherUserManager.getInstance().getUser()?.birthyear?.let { LocalDate.now().year-it}
        binding.profileUserAgeText.text=buildString {
            append("Age: ")
            append(age.toString())
        }



    }
}
