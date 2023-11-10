package com.draccoapp.poker.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.draccoapp.poker.ui.fragments.contract.fragments.AllContractFragment
import com.draccoapp.poker.ui.fragments.contract.fragments.CurrentContractFragment
import com.draccoapp.poker.ui.fragments.contract.fragments.FinishContractFragment
import com.draccoapp.poker.ui.fragments.contract.fragments.PendingContractFragment
import com.draccoapp.poker.ui.fragments.tournament.applicant.fragments.AllApplicantFragment
import com.draccoapp.poker.ui.fragments.tournament.applicant.fragments.FinishApplicantFragment
import com.draccoapp.poker.ui.fragments.tournament.applicant.fragments.PendingApplicantFragment
import com.draccoapp.poker.ui.fragments.tournament.applicant.fragments.ValidatedApplicantFragment

class ViewPagerContractListAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                AllContractFragment()
            }
            1->{
                PendingContractFragment()
            }
            2->{
                CurrentContractFragment()
            }
            3->{
                FinishContractFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}