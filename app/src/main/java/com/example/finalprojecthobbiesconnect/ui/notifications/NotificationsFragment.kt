package com.example.finalprojecthobbiesconnect.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojecthobbiesconnect.ProfileFriendActivity
import com.example.finalprojecthobbiesconnect.adapters.PendingFriendAdapter
import com.example.finalprojecthobbiesconnect.databinding.FragmentNotificationsBinding
import com.example.finalprojecthobbiesconnect.interfaces.Callback_PendingFriendCallback
import com.example.finalprojecthobbiesconnect.models.User
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import com.example.finalprojecthobbiesconnect.utilties.OtherUserManager
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var pendingFriendAdapter: PendingFriendAdapter
    private val databaseRef = FirebaseDatabase.getInstance().reference.child(Constants.USERS_REF)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initRecyclerView()
        loadPendingFriendsFromFirebase()



        return root
    }

    private fun loadPendingFriendsFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pendingFriends = dataSnapshot.children.mapNotNull { it.getValue(User::class.java) }
                    .filter { user ->
                       MyActiveUserManager.getUser().pendingFriendsList.contains(user.email)
                    }
                // Update the RecyclerView with the pending friends list
                // If the list is empty, hide the RecyclerView and show a message
                _binding?.let{
                    if(pendingFriends.isEmpty()){
                        it.noPendingRequestsTV.visibility=View.VISIBLE
                        it.notificationsRV.visibility=View.GONE
                    }else{
                        it.noPendingRequestsTV.visibility=View.GONE
                        it.notificationsRV.visibility=View.VISIBLE
                    }

                }
                pendingFriendAdapter.updatePendingFriends(pendingFriends)



            }

            override fun onCancelled(error: DatabaseError) {
                SignalManager.getInstance().vibrateAndToast(Constants.ALERT_LOAD_USERS)
            }
        })

    }

    private fun initRecyclerView() {
        pendingFriendAdapter=PendingFriendAdapter(emptyList<User>().toMutableList())
        pendingFriendAdapter.callbackPendingFriendCallback=object: Callback_PendingFriendCallback {
            override fun pendingFriendClick(user: User, position: Int) {
                OtherUserManager.getInstance().setUser(user)
                changeActivity()
            }

        }
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.notificationsRV.layoutManager = linearLayoutManager
        binding.notificationsRV.adapter=pendingFriendAdapter


    }

    private fun changeActivity() {
        val intent = Intent(requireContext(), ProfileFriendActivity::class.java)
        val b = Bundle()
        b.putInt(Constants.NAVIGATION_KEY, Constants.NOTIFICATIONS_FRAGMENT)
        intent.putExtras(b)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}