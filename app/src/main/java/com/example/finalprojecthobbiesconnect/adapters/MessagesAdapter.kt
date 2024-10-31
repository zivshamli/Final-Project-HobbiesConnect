package com.example.finalprojecthobbiesconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojecthobbiesconnect.databinding.ChatBubbleReceivedBinding
import com.example.finalprojecthobbiesconnect.databinding.ChatBubbleSentBinding
import com.example.finalprojecthobbiesconnect.models.Message
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        // Function to show or hide the time  based on the position
        fun showTime(position: Int) {

            if (position > 0 && messages[position].date == messages[position - 1].date) {
                binding.timeTextView.visibility = View.GONE
                binding.viewSpace.visibility=View.GONE
            }
            else {
                binding.timeTextView.visibility = View.VISIBLE
                binding.viewSpace.visibility=View.VISIBLE
                binding.timeTextView.text = messages[position].date
                val todayDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                // Check if the date is today's date
                if(messages[position].date==todayDate)
                    binding.timeTextView.text =Constants.TODAYS_DATE
                else{
                    binding.timeTextView.text = messages[position].date
                }
            }
        }
    }
    inner class ReceivedMessageViewHolder( val binding: ChatBubbleReceivedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.messageTextView.text = message.content
        }
        // Function to show or hide the time  based on the position
        fun showTime(position: Int) {
            if (position > 0 && messages[position].date == messages[position - 1].date) {
                binding.timeTextView.visibility = View.GONE
                binding.viewSpace.visibility=View.GONE
            }
            else {
                binding.timeTextView.visibility = View.VISIBLE
                binding.viewSpace.visibility=View.VISIBLE
                val todayDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                // Check if the date is today's date
                if(messages[position].date==todayDate)
                    binding.timeTextView.text =Constants.TODAYS_DATE
                else{
                binding.timeTextView.text = messages[position].date
                }
            }
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
            holder.showTime(position)
        }
        else if (holder is ReceivedMessageViewHolder) {
            holder.bind(messages[position])
            holder.showTime(position)
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