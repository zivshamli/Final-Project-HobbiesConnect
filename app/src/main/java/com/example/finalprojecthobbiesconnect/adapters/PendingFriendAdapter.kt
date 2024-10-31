package com.example.finalprojecthobbiesconnect.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojecthobbiesconnect.R
import com.example.finalprojecthobbiesconnect.databinding.HorizontalPendingRequestBinding
import com.example.finalprojecthobbiesconnect.interfaces.Callback_PendingFriendCallback
import com.example.finalprojecthobbiesconnect.models.Chats
import com.example.finalprojecthobbiesconnect.models.Message
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
        val chatId : String = generateChatId(MyActiveUserManager.getUser().email,user.email)
        if(!MyActiveUserManager.getUser().chatList.contains(chatId)) {

            sendMessages(user,MyActiveUserManager.getUser(),chatId)
        }


    }

    private fun sendMessages(user: User, myUser: User, chatId:String) {
        // Send a message to the user
        // Create a new chat with the user
        val messageMap: MutableMap<String,Message> = mutableMapOf()
        // Add the chat to the user's chat list

            myUser.chatList.add(chatId)
            MyActiveUserManager.setUser(myUser)
            // Add the chat to the other user's chat list
            user.chatList.add(chatId)




        val message =Message(MyActiveUserManager.getUser().email,Constants.STARTER_DEFUALT_MASSEGE)
        val participantsStatus = linkedMapOf(
            MyActiveUserManager.getUser().email.replace(".",",") to true,
            user.email.replace(".",",") to false)
        val chat = Chats(messageMap,participantsStatus)
        chat.addMessage(message)




        // Add the chat to the Firebase database
        val database = FirebaseDatabase.getInstance()
        val chatRef = database.getReference("chats").child(chatId)
        chatRef.setValue(chat).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Handle success
                if(!MyActiveUserManager.getUser().chatList.contains(chatId)) {
                    addChatListInFirebaseUser(user)
                    addChatListInFirebaseUser(myUser)
                }
                Log.d("PendingFriendAdapter", "Chat added to Firebase database")
            } else {
                // Handle failure
                SignalManager.getInstance()
                    .vibrateAndToast("Failed to add chat to Firebase database")
            }


        }

    }











    private fun addChatListInFirebaseUser(user: User) {
        // Add the chat to the user's chat list in Firebase
       val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(user.email.replace(".",","))
        userRef.child("chatList").setValue(user.chatList).addOnCompleteListener{task ->
            if (task.isSuccessful) {
                // Handle success

                Log.d("PendingFriendAdapter", "Chat added to user's chat list in Firebase")
            }
            else {
                // Handle failure
                SignalManager.getInstance().vibrateAndToast("Failed to add chat to user's chat list")
            }
        }

    }

    private fun generateChatId(email: String, email1: String): String {
        val email1replace = email1.replace(".",",")
        val emailreplace = email.replace(".",",")
        return if (emailreplace < email1replace) {
            "$emailreplace-$email1replace"
        } else {
            "$email1replace-$emailreplace"
        }


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





