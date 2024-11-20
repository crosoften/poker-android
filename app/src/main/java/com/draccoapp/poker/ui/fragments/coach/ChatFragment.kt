package com.draccoapp.poker.ui.fragments.coach

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.ChatMessageReceived
import com.draccoapp.poker.api.model.response.chat.ChatResponse
import com.draccoapp.poker.api.model.response.chat.MessageAdapter
import com.draccoapp.poker.databinding.FragmentChatBinding
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.CoachViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CoachViewModel by viewModel()
    private val args: ChatFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.connect()
        viewModel.readMessages()
        changeStatusBarColor()
        loadMessages()
        observers()
        setupClickListeners()
    }

    private fun observers() {
        viewModel.chatInformation.observe(viewLifecycleOwner) { chatInformation ->
            setupChatInformation(chatInformation)
        }

        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            setupRvChat(messages)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            mostrarToast(errorMessage, requireContext())
        }
    }

    private fun setupClickListeners() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSendMessage.setOnClickListener {
            setupSendMessage()
        }
    }

    private fun setupSendMessage() {
        val message = binding.inputMessage.text.toString()
        viewModel.sendMessage(message, args.chatId)
        binding.inputMessage.text?.clear()
    }

    private fun loadMessages() {
        viewModel.getChatById(args.chatId)
    }

    private fun setupChatInformation(chatInformation: ChatResponse) {
        binding.tvChatName.text = chatInformation.playerName
        binding.tvReceiverName.text = chatInformation.type
    }

    private fun setupRvChat(messages: List<ChatMessageReceived>) {
        binding.rvChatMessages.adapter = MessageAdapter(messages)
        val linearLayout =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvChatMessages.layoutManager = linearLayout
        linearLayout.scrollToPosition(messages.size - 1)
    }

    private fun changeStatusBarColor() {
        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.button_background)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.disconnect()
    }

}