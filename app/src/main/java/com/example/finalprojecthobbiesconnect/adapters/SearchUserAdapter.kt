package com.example.finalprojecthobbiesconnect.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojecthobbiesconnect.R
import com.example.finalprojecthobbiesconnect.databinding.HorizontalUserItemBinding
import com.example.finalprojecthobbiesconnect.interfaces.Callback_SearchUserCallback
import com.example.finalprojecthobbiesconnect.models.User
import com.example.finalprojecthobbiesconnect.utilties.ImageLoader
import java.time.LocalDate

class SearchUserAdapter(private var userList: List<User>) : RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {
    var callbackSearchUserCallback:Callback_SearchUserCallback?=null
    inner class ViewHolder(val binding: HorizontalUserItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.searchUserCard.setOnClickListener {
                if(callbackSearchUserCallback!=null){
                    callbackSearchUserCallback!!.searchUserClick(getItem(adapterPosition),adapterPosition)
                }
            }
        }

    }

    private fun getItem(position: Int) = userList[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=HorizontalUserItemBinding.inflate(android.view.LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    fun updateUsers(newUserList: List<User>) {
        this.userList= newUserList
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(userList[position]) {

                val age = LocalDate.now().year - userList[position].birthyear
                binding.searchUserDetails.text = buildString {
                    append(userList[position].username)
                    append(" ,")
                    append("Age: ")
                    append(age.toString())
                }

                binding.searchUserHobbies.text = buildString {
                    userList[position].hobbies.forEach { hobby ->
                        if (hobby != userList[position].hobbies.last())
                            append("$hobby , ")
                        else {
                            append(hobby)
                        }

                    }
                }
                ImageLoader.getInstance().load(
                    userList[position].profilePhoto,
                    binding.searchUserProfilePhoto,
                    R.drawable.avatar
                )
            }

        }
    }
}