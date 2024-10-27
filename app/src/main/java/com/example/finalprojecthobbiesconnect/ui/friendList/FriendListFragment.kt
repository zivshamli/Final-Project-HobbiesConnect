package com.example.finalprojecthobbiesconnect.ui.friendList

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojecthobbiesconnect.ProfileFriendActivity
import com.example.finalprojecthobbiesconnect.adapters.SearchUserAdapter
import com.example.finalprojecthobbiesconnect.databinding.FragmentFriendListBinding
import com.example.finalprojecthobbiesconnect.interfaces.Callback_UserCallback
import com.example.finalprojecthobbiesconnect.models.User
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import com.example.finalprojecthobbiesconnect.utilties.OtherUserManager
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FriendListFragment : Fragment() {
    private var _binding :FragmentFriendListBinding? =null
    private val binding get() = _binding!!
    private lateinit var friendListAdapter: SearchUserAdapter
    private val database=FirebaseDatabase.getInstance()
    private val userRef=database.getReference().child("users")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentFriendListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initRecyclerView()
        loadFriends()





        return root
    }

    private fun loadFriends() {
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friends = snapshot.children.mapNotNull {
                    it.getValue(User::class.java)
                }
                    .filter { user->
                        MyActiveUserManager.getUser().friendsList.contains(user.email)
                    }
                // update adapter
                // if no friends show no friends textview
                _binding?.let {
                    if(friends.isEmpty()){
                        it.friendListRV.visibility=View.GONE
                        it.noFriendsTV.visibility=View.VISIBLE
                    }else{
                        it.friendListRV.visibility=View.VISIBLE
                        it.noFriendsTV.visibility=View.GONE
                    }

                }
                friendListAdapter.updateUsers(friends)


            }

            override fun onCancelled(error: DatabaseError) {
                SignalManager.getInstance().vibrateAndToast("Failed to load friends")
            }
        })

        }

    private fun initRecyclerView() {
        friendListAdapter = SearchUserAdapter(emptyList<User>().toMutableList())
        friendListAdapter.callbackSearchUserCallback=object :Callback_UserCallback {
            override fun userClick(user: User, position: Int) {

                OtherUserManager.getInstance().setUser(user)
                changeActivity()

            }


        }
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.friendListRV.layoutManager = linearLayoutManager
        binding.friendListRV.adapter = friendListAdapter


    }

    private fun changeActivity() {
        val intent = Intent(requireContext(), ProfileFriendActivity::class.java)
        val b = Bundle()
        b.putInt(Constants.NAVIGATION_KEY, Constants.FRIEND_LIST_FRAGMENT)
        intent.putExtras(b)
        startActivity(intent)
        activity?.finish()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}