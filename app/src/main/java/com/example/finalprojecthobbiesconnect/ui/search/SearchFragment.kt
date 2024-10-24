package com.example.finalprojecthobbiesconnect.ui.search

//noinspection SuspiciousImport
import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojecthobbiesconnect.ProfileFriendActivity
import com.example.finalprojecthobbiesconnect.adapters.SearchUserAdapter
import com.example.finalprojecthobbiesconnect.databinding.FragmentSearchBinding
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
import java.time.LocalDate

class SearchFragment : Fragment() {
    private lateinit var userAdapter: SearchUserAdapter
    private val databaseRef = FirebaseDatabase.getInstance().reference.child("users")

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initAutoFillTextFieldViews()
        initRecyclerView()
        initSearchButton()


        return root
    }

    private fun initSearchButton() {
        binding.searchButton.setOnClickListener {
            val hobbies = binding.searchHobbies.text.toString()
            val name = binding.searchName.text.toString()
            val ageMin = binding.searchAgeMin.text.toString()
            val ageMax = binding.searchAgeMax.text.toString()
            binding.loadingAnimation.visibility = View.VISIBLE
            binding.searchUserRV.visibility = View.GONE
            searchUsersFromFirebase(hobbies, name, ageMin, ageMax)
            // Hide the loading animation and show the RecyclerView


        }
    }

    private fun searchUsersFromFirebase(hobbies: String, name: String, ageMin: String, ageMax: String) {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val filteredUsers = dataSnapshot.children.mapNotNull { it.getValue(User::class.java) }
                    .filter { user ->
                        // Check if the user's hobbies contain any of the provided hobbies
                        val hobbiesList = hobbies.split(",").map { it.trim() }
                        val hasMatchingHobby = hobbiesList.any { hobby ->
                            user.hobbies.contains(hobby)
                        }
                        // Filter users based on the provided criteria
                        ((hobbies.isEmpty() || hasMatchingHobby) &&
                                (name.isEmpty() || user.username.contains(name, ignoreCase = true)) &&
                                (ageMin.isEmpty() || (LocalDate.now().year - user.birthyear) >= ageMin.toInt()) &&
                                (ageMax.isEmpty() || (LocalDate.now().year - user.birthyear) <= ageMax.toInt())&&
                                        (user.email!= MyActiveUserManager.getUser().email))
                    }

                _binding?.let {
                    it.loadingAnimation.visibility = View.GONE
                    it.searchUserRV.visibility = View.VISIBLE
                }
                // Update the RecyclerView with the filtered users
                userAdapter.updateUsers(filteredUsers)


            }


            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                SignalManager.getInstance().vibrateAndToast("Failed to load users")

            }
        })

    }

    private fun initRecyclerView() {
        userAdapter=SearchUserAdapter(emptyList())
        userAdapter.callbackSearchUserCallback=object: Callback_UserCallback {
            override fun userClick(user: User, position: Int) {
                OtherUserManager.getInstance().setUser(user)
                changeActivity()
            }

        }
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.searchUserRV.layoutManager = linearLayoutManager
        binding.searchUserRV.adapter=userAdapter

    }

    private fun changeActivity() {
        val intent = Intent(requireContext(), ProfileFriendActivity::class.java)
        val b = Bundle()
        b.putInt(Constants.NAVIGATION_KEY, Constants.SEARCH_FRAGMENT)
        intent.putExtras(b)
        startActivity(intent)
        activity?.finish()
    }

    private fun initAutoFillTextFieldViews() {
        initHobbiesTextFieldAutoFill()
        initAgeTextFieldAutoFill(binding.searchAgeMin)
        initAgeTextFieldAutoFill(binding.searchAgeMax)
    }

    private fun initAgeTextFieldAutoFill(searchAge: MultiAutoCompleteTextView) {
        val suggestion = mutableListOf<String>()
        val currentYear = LocalDate.now().year
        for (i in 1900..currentYear) {
            val age: Int = currentYear - i
            suggestion.add(age.toString())
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, suggestion)
        searchAge.setAdapter(adapter)
        searchAge.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
    }

    private fun initHobbiesTextFieldAutoFill() {
        val suggestion=Constants.HOBBIES_LIST
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, suggestion)
        binding.searchHobbies.setAdapter(adapter)
        binding.searchHobbies.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}