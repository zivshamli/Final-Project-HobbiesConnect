package com.example.finalprojecthobbiesconnect.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojecthobbiesconnect.databinding.ChatBubbleReceivedBinding
import com.example.finalprojecthobbiesconnect.databinding.ChatBubbleSentBinding
import com.example.finalprojecthobbiesconnect.models.Message
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager

class MessagesAdapter(private var messages: MutableList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        const val SENT_MESSAGE = 1
        const val RECEIVED_MESSAGE = 2
    }

    inner class SentMessageViewHolder( val binding: ChatBubbleSentBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(message: Message)
        {
            binding.messageTextView.text = message.content
        }
    }
    inner class ReceivedMessageViewHolder( val binding: ChatBubbleReceivedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.messageTextView.text = message.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SENT_MESSAGE) {
            val binding = ChatBubbleSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SentMessageViewHolder(binding)
        }
        else {
            val binding = ChatBubbleReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SentMessageViewHolder) {
            holder.bind(messages[position])
        }
        else if (holder is ReceivedMessageViewHolder) {
            holder.bind(messages[position])
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }
    // Function to decide which view holder to use
    override fun getItemViewType(position: Int): Int {
        return if(messages[position].senderEmail==MyActiveUserManager.getUser().email)
            SENT_MESSAGE
        else
            RECEIVED_MESSAGE
    }




}