package com.example.finalprojecthobbiesconnect.ui.chats

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.finalprojecthobbiesconnect.R
import com.example.finalprojecthobbiesconnect.databinding.FragmentChatsBinding
import com.example.finalprojecthobbiesconnect.ui.chats.ChatsViewModel

class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatsViewModel =
            ViewModelProvider(this)[ChatsViewModel::class.java]

        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textChats
        chatsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}