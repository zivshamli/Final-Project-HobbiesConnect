package com.example.finalprojecthobbiesconnect.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojecthobbiesconnect.R
import com.example.finalprojecthobbiesconnect.databinding.HorizontalPendingRequestBinding
import com.example.finalprojecthobbiesconnect.interfaces.Callback_PendingFriendCallback
import com.example.finalprojecthobbiesconnect.models.User
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.ImageLoader
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.google.firebase.database.FirebaseDatabase

class PendingFriendAdapter(private var pendingFriends: MutableList<User>) : RecyclerView.Adapter<PendingFriendAdapter.ViewHolder>() {
    var callbackPendingFriendCallback: Callback_PendingFriendCallback?=null
    inner class ViewHolder(val binding: HorizontalPendingRequestBinding): RecyclerView.ViewHolder(binding.root) {
        init{
            binding.pendingUserCard.setOnClickListener{
                if(callbackPendingFriendCallback!=null){
                    callbackPendingFriendCallback!!.pendingFriendClick(getItem(adapterPosition),adapterPosition)
                }
            }
        }

    }
    private fun getItem(position: Int) = pendingFriends[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=HorizontalPendingRequestBinding.inflate(android.view.LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pendingFriends.size
    }
    fun updatePendingFriends(newPendingFriends: List<User>) {
        this.pendingFriends= newPendingFriends.toMutableList()
        this.notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(pendingFriends[position]){
                // Set the text for the for friend request
                binding.pendingUserDetails.text= buildString {
                   append( pendingFriends[position].username)
                    append(" ")
                    append(Constants.FRIENDREQUESTMESSAGE)
                }
                // Load the profile photo using ImageLoader
                ImageLoader.getInstance().load(pendingFriends[position].profilePhoto,binding.pendingUserProfilePhoto,
                    R.drawable.avatar)

                binding.pendingUserAcceptBTN.setOnClickListener{addFriend(pendingFriends[position],position)}
                binding.pendingUserDeclineBTN.setOnClickListener{removeFriend(pendingFriends[position],position)}
            }



            }
        }

    private fun removeFriend(user: User, position: Int) {
        // Remove the friend from the pending list

        MyActiveUserManager.getUser().pendingFriendsList.remove(user.email)
        removePendingFriendListInFirebase(user,position)



    }

    private fun removePendingFriendListInFirebase(user: User,position: Int) {
        // Remove the friend from the pending friend list in Firebase
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(MyActiveUserManager.getUser().email.replace(".",","))
        userRef.child("pendingFriendsList").setValue(MyActiveUserManager.getUser().pendingFriendsList)
            .addOnSuccessListener {
                // Handle success
                pendingFriends.remove(user)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, pendingFriends.size)

            }
            .addOnFailureListener {
                // Handle failure
                SignalManager.getInstance().vibrateAndToast("Failed to remove friend from pending list")
            }

    }

    private fun addFriend(user: User, position: Int) {
        // Add the friend to the friends list
        MyActiveUserManager.getUser().friendsList.add(user.email)
        MyActiveUserManager.getUser().pendingFriendsList.remove(user.email)
        user.friendsList.add(MyActiveUserManager.getUser().email)
        addFriendListInFirebase(user,position)
        addFriendListInFirebaseToOtherUser(user)



    }

    private fun addFriendListInFirebaseToOtherUser(user: User) {
        // Add the friend to the friends list in Firebase
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(user.email.replace(".",","))
        userRef.child("friendsList").setValue(user.friendsList)
            .addOnSuccessListener {
                // Handle success
                Log.d("PendingFriendAdapter", "Friend added to friends list in Firebase")
            }
            .addOnFailureListener {
                // Handle failure
                SignalManager.getInstance().vibrateAndToast("Failed to add friend to friends list")
            }


    }

    private fun addFriendListInFirebase(user: User, position: Int) {
        // Add the friend to the friends list in Firebase
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(MyActiveUserManager.getUser().email.replace(".",","))
        val updates = hashMapOf<String, Any>(
            "friendsList" to MyActiveUserManager.getUser().friendsList,
            "pendingFriendsList" to MyActiveUserManager.getUser().pendingFriendsList

        )
        userRef.updateChildren(updates).addOnCompleteListener {task->
            if(task.isSuccessful) {
                // Handle success
                pendingFriends.remove(user)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, pendingFriends.size)
            }
            else{
                // Handle failure
                SignalManager.getInstance().vibrateAndToast("Failed to add friend to friends list")
            }

        }

    }


}





