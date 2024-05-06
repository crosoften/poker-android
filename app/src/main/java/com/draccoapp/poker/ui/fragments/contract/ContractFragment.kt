package com.draccoapp.poker.ui.fragments.contract

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentApplicantTournamentBinding
import com.draccoapp.poker.databinding.FragmentContractBinding
import com.draccoapp.poker.ui.adapters.ViewPagerContractListAdapter
import com.draccoapp.poker.ui.adapters.ViewPagerTournamentApplicantAdapter
import com.google.android.material.tabs.TabLayoutMediator


class ContractFragment : Fragment() {


    private var _binding: FragmentContractBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentContractBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        onclick()
    }

    private fun onclick() {

        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }

        }
    }

    private fun setupViewPager() {
        val adapter = ViewPagerContractListAdapter(childFragmentManager, lifecycle)

        binding.viewPagerList.adapter = adapter

        TabLayoutMediator(binding.tabListContract, binding.viewPagerList){ tab, position->
            when(position){
                0->{
                    tab.text = getString(R.string.todos)
                }
                1->{
                    tab.text = getString(R.string.pendentes)
                }
                2->{
                    tab.text = getString(R.string.vigentes)
                }
                3 -> {
                    tab.text = getString(R.string.finalizados)
                }
            }
        }.attach()

//        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                if (position == 2) {
//                    binding.nextButton.isVisible = true
//                }
//            }
//        })
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}