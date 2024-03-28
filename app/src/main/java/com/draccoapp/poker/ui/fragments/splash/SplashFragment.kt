package com.draccoapp.poker.ui.fragments.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentSplashBinding
import com.draccoapp.poker.ui.activities.AccountActivity
import com.draccoapp.poker.ui.activities.MainActivity
import com.draccoapp.poker.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val TAG = "SplashFragment"
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDelay()
    }

    private fun initDelay() {
//        val progressBarHorizontal = binding.progressBar

        val totalProgressTime = 3000
        val progressBarMax = 100

        val animator = ValueAnimator.ofInt(0, progressBarMax)
        animator.duration = totalProgressTime.toLong()

        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
//            progressBarHorizontal.progress = progress
        }

        animator.start()


        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if(viewModel.getIsLogged()){
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finishAffinity()
                } else {
                    startActivity(Intent(requireContext(), AccountActivity::class.java))
                    requireActivity().finishAffinity()
                }


            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}