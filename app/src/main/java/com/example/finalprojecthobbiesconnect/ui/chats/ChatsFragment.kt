package com.example.finalprojecthobbiesconnect.ui.chats

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojecthobbiesconnect.ChatRoomActivity
import com.example.finalprojecthobbiesconnect.adapters.ChatRoomsAdapter
import com.example.finalprojecthobbiesconnect.databinding.FragmentChatsBinding
import com.example.finalprojecthobbiesconnect.interfaces.Callback_ChatRoomCallback
import com.example.finalprojecthobbiesconnect.models.Chats
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.FuncUtlis
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import com.example.finalprojecthobbiesconnect.utilties.OtherUserManager
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private lateinit var chatRoomsAdapter: ChatRoomsAdapter
    private val database = FirebaseDatabase.getInstance()
    private val chatsRef = database.getReference("chats")



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        FuncUtlis.setupUI(requireActivity(), root)
        initRecyclerView()
        loadChatRooms()

        return root
    }

    private fun loadChatRooms() {
        chatsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var chatRooms=dataSnapshot.children.mapNotNull { it.getValue(Chats::class.java) }
                    .filter {
                        it.participantsStatus.keys.contains(MyActiveUserManager.getUser().email.replace(".", ","))
                    }
                _binding?.let{
                    if(chatRooms.isEmpty()){
                        it.chatsRoomRV.visibility=View.GONE
                        it.noChatsTV.visibility=View.VISIBLE
                    }
                    else{

                        it.chatsRoomRV.visibility=View.VISIBLE
                        it.noChatsTV.visibility=View.GONE
                        chatRooms=chatRooms.sorted()

                        chatRoomsAdapter.updateChats(chatRooms)
                        chatRoomsAdapter.notifyDataSetChanged()

                    }

                }


            }

            override fun onCancelled(error: DatabaseError) {
                SignalManager.getInstance().vibrateAndToast("Failed to load chats details")
            }
        })
    }

    private fun initRecyclerView() {
        chatRoomsAdapter= ChatRoomsAdapter(emptyList<Chats>().toMutableList())
        chatRoomsAdapter.callbackChatRoomCallback=object : Callback_ChatRoomCallback{
            override fun chatRoomClicked(chatRoom: Chats, position: Int) {
                FuncUtlis.loadPartner(chatRoom){partner->
                    if(partner!=null){
                        OtherUserManager.getInstance().setUser(partner)
                        changeActivity()
                    }
                    else{
                        SignalManager.getInstance().toast(Constants.PARTNER_NOT_FOUND)
                    }

                }
            }

        }
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.chatsRoomRV.layoutManager = linearLayoutManager
        binding.chatsRoomRV.adapter = chatRoomsAdapter


    }


    private fun changeActivity() {
        val intent = Intent(requireContext(), ChatRoomActivity::class.java)
        val b = Bundle()
        b.putInt(Constants.NAVIGATION_KEY, Constants.CHATS_FRAGMENT)
        intent.putExtras(b)
        startActivity(intent)
        activity?.finish()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}