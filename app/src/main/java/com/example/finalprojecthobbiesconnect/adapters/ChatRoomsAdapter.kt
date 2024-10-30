package com.example.finalprojecthobbiesconnect.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojecthobbiesconnect.R
import com.example.finalprojecthobbiesconnect.databinding.HorizontalChatRoomItemBinding
import com.example.finalprojecthobbiesconnect.interfaces.Callback_ChatRoomCallback
import com.example.finalprojecthobbiesconnect.models.Chats
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.FuncUtlis
import com.example.finalprojecthobbiesconnect.utilties.ImageLoader
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager

class ChatRoomsAdapter(private var chats: List<Chats>) : RecyclerView.Adapter<ChatRoomsAdapter.ViewHolder>()  {
    var callbackChatRoomCallback:Callback_ChatRoomCallback?=null
    inner class ViewHolder(val binding: HorizontalChatRoomItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init{
            binding.userChatPartnerCard.setOnClickListener {
                if(callbackChatRoomCallback!=null)
                    callbackChatRoomCallback!!.chatRoomClicked(getItem(adapterPosition),adapterPosition)
            }
        }

    }

    private fun getItem(adapterPosition: Int)=chats[adapterPosition]



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding=HorizontalChatRoomItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun updateChats(newChats:List<Chats>){
        chats=newChats
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(chats[position]){
                binding.lastMessage.text= this.messages.values.maxByOrNull { it.timestamp }!!.content
                if (this.participantsStatus[MyActiveUserManager.getUser().email.replace(".",",")]==false)
                {
                    binding.lastMessage.setTypeface(null, Typeface.BOLD)
                }
                else{
                    binding.lastMessage.setTypeface(null, Typeface.NORMAL)
                }
               FuncUtlis.loadPartner(this){partner->
                    if(partner!=null){
                        val age=partner.getAge()
                        binding.userChatPartnerDetails.text= buildString {
                            append(partner.username)
                            append(", ")
                            append("Age: ")
                            append(age)
                        }
                        ImageLoader.getInstance().load(partner.profilePhoto,binding.userChatPartnerProfilePhoto,
                            R.drawable.avatar)
                    }
                   else{
                       binding.userChatPartnerDetails.text= buildString {
                           append(Constants.UNKNOWN_USER)
                       }

                    }
                }
            }
        }
    }
}