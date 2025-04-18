package com.draccoapp.poker.ui.fragments.coach

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.api.model.response.chat.ChatResponse
import com.draccoapp.poker.databinding.FragmentCoachBinding
import com.draccoapp.poker.ui.adapters.ChatListAdapter
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.CoachViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CoachFragment : Fragment() {
    private var _binding: FragmentCoachBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CoachViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoachBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadChats()
    }

    private fun setupObservers() {
        viewModel.chats.observe(viewLifecycleOwner) { chatsList ->
            chatRvSetup(chatsList)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            mostrarToast(errorMessage, requireContext())
        }
    }

    private fun setupClickListeners(){
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun chatRvSetup(chats: List<ChatResponse>) {
        binding.apply {
            rvChatList.layoutManager = LinearLayoutManager(requireContext())
            rvChatList.adapter = ChatListAdapter(chats) { chatInfos ->
                findNavController().navigate(CoachFragmentDirections.actionCoachFragmentToChatFragment(chatInfos.id))
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}