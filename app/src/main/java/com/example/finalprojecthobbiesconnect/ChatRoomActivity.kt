package com.example.finalprojecthobbiesconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojecthobbiesconnect.adapters.MessagesAdapter
import com.example.finalprojecthobbiesconnect.databinding.ActivityChatRoomBinding
import com.example.finalprojecthobbiesconnect.models.Message
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.FuncUtlis
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import com.example.finalprojecthobbiesconnect.utilties.OtherUserManager
import com.example.finalprojecthobbiesconnect.utilties.SoundManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatRoomActivity : AppCompatActivity() {
    private var navigation :Int=0
    private var navigation2 :Int=0
    private lateinit var messageAdapter: MessagesAdapter
    private lateinit var messagesList: MutableList<Message>
    private val database= FirebaseDatabase.getInstance()
    private  lateinit var messagesRef: DatabaseReference
    private lateinit var chatId: String
    private val soundManager: SoundManager = SoundManager(this)


    private lateinit var binding: ActivityChatRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        treatIntent()
        FuncUtlis.setupUI(this, binding.root)
        connectToDatabase()
        initViews()


    }

    private fun connectToDatabase() {
         chatId=generateChatId(MyActiveUserManager.getUser().email,
            OtherUserManager.getInstance().getUser()!!.email)
        messagesRef=database.getReference("chats").child(chatId).child("messages")

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

    private fun treatIntent() {
        navigation=intent.getIntExtra(Constants.NAVIGATION_KEY,0)
        navigation2=intent.getIntExtra(Constants.NAVIGATION_KEY2,0)


    }

    private fun initViews() {
        initRecyclerView()

        loadAllMessages()
        initUserNameTextView()
        initSendBTN()
        initBackBTN()

    }





    private fun initSendBTN() {
        binding.sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val content = binding.messageEditText.text.toString().trim()
        if (content.isNotEmpty()) {
            val senderEmail = MyActiveUserManager.getUser().email
            val newMessage = Message(senderEmail = senderEmail, content = content)

            // Push the message to the database
            messagesRef.push().setValue(newMessage).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Clear the input field
                    binding.messageEditText.text?.clear()

                    // Play sound for sending a message
                    soundManager.playSound(R.raw.send_message_sound)

                    // Update participant status
                    updateParticipateStatus()
                } else {
                    println("Failed to send message: ${task.exception?.message}")
                }
            }
        }
    }


    private fun updateParticipateStatus() {
        val chatRef=database.getReference("chats").child(chatId).child("participantsStatus")
       val participateNewStatus= linkedMapOf(
           MyActiveUserManager.getUser().email.replace(".",",") to true,
           OtherUserManager.getInstance().getUser()!!.email.replace(".",",") to false
       )
        chatRef.setValue(participateNewStatus).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("ChatRoomActivity", "Participate status updated successfully")
            } else {
                Log.e("ChatRoomActivity", "Failed to update participate status: ${task.exception?.message}")
            }
        }
    }

    private fun loadAllMessages() {
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesList.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    message?.let { messagesList.add(it) }
                }

                messagesList.sortBy { message -> message.timestamp }

                messageAdapter.notifyDataSetChanged()

                binding.recyclerViewMessages.scrollToPosition(messagesList.size - 1)
                //THIS IS THE PART
                if(!isFinishing&&!isDestroyed)
                    if (messagesList.isNotEmpty()) {
                        if (messagesList.last().senderEmail != MyActiveUserManager.getUser().email) {
                            soundManager.playSound(R.raw.received_message_sound)
                            updateMyUserStatus()
                        }

                    }

            }


            override fun onCancelled(error: DatabaseError) {
                println("Failed to load messages: ${error.message}")
            }
        })
    }

    private fun updateMyUserStatus() {
        val chatRef=database.getReference("chats").child(chatId).child("participantsStatus").child(MyActiveUserManager.getUser().email.replace(".",","))
        chatRef.setValue(true).addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                Log.d("ChatRoomActivity", "My user status updated successfully")
            } else {
                Log.e("ChatRoomActivity", "Failed to update my user status: ${task.exception?.message}")
            }
        }

    }

    private fun initBackBTN() {
        binding.backButton.setOnClickListener {
            changeActivity()
        }
    }

    private fun changeActivity() {
        if (navigation==Constants.PROFILE_ACTIVITY){
            val intent = Intent(this, ProfileFriendActivity::class.java)
            val b = Bundle()
            b.putInt(Constants.NAVIGATION_KEY, navigation2)
            intent.putExtras(b)
            startActivity(intent)
            finish()
        }
        else if (navigation==Constants.CHATS_FRAGMENT){
            val intent = Intent(this, NavigationActivity::class.java)
            val b = Bundle()
            b.putInt(Constants.NAVIGATION_KEY, navigation)
            intent.putExtras(b)
            startActivity(intent)
            finish()
        }
    }

    private fun initRecyclerView() {
        messagesList = mutableListOf()
        messageAdapter = MessagesAdapter(messagesList)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation= RecyclerView.VERTICAL
        binding.recyclerViewMessages.layoutManager = linearLayoutManager
        binding.recyclerViewMessages.adapter = messageAdapter



    }

    private fun initUserNameTextView() {

        binding.userNameTextView.text= OtherUserManager.getInstance().getUser()!!.username

    }


}