package com.draccoapp.poker.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.response.homeFrament.HomeFragmentResponse
import com.draccoapp.poker.api.model.response.homeFrament.TournamentsImIn
import com.draccoapp.poker.api.modelOld.response.Tournament
import com.draccoapp.poker.api.modelOld.response.User
import com.draccoapp.poker.databinding.FragmentProfileBinding
import com.draccoapp.poker.ui.activities.AccountActivity
import com.draccoapp.poker.ui.adapters.TournamentAdapter
import com.draccoapp.poker.ui.adapters.adaptersNew.TournamentMineAdapterNew
import com.draccoapp.poker.ui.fragments.login.LoginFragment
import com.draccoapp.poker.utils.Preferences
import com.draccoapp.poker.utils.SharedUtils
import com.draccoapp.poker.viewModel.HomeViewModel
import com.draccoapp.poker.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

private const val TAG = "HomeFragment"

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<UserViewModel>()
    private val homeViewModel by viewModel<HomeViewModel>()

    private lateinit var applicantAdapter: TournamentMineAdapterNew

    //    private val listaMeusTorneios = mutableListOf<TournamentsImIn>()
    private lateinit var preferences: Preferences
    private var statusTour = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        onclick()
        setupRecycler()
        preferences = Preferences(requireContext())
//        homeViewModel.getMeusDados()
        homeViewModel.getHomeFragment()
    }

    private fun setupObserver() {

        homeViewModel.successHomeFragment.observe(viewLifecycleOwner) { response ->
            atualizaListaDeTorneios(response)

            //CURRENT CONTRACT INFOS
            binding.textName.text = response.myself.name
            Glide.with(requireContext()).load(response.myself.imageUrl).into(binding.imgProfile)
            binding.txtTempoRestanteContrato.text = response.myself.contractExpiresIn
            binding.txtSeusTorneios.text = response.myself.tournamentsCount.toString()
            binding.txtLucroContratoAtual.text = response.myself.contractProfit.toString()
            binding.textView7.text = response.myself.ranking.toString()

            //OVERALL INFOS
            binding.txtSeusTorneiosGerais.text = response.myself.overallInfos.tournamentsCount.toString()
            binding.txtLucroGeral.text = response.myself.overallInfos.profit.toString()
            binding.txtRankingGeral.text = response.myself.overallInfos.ranking.toString()
        }
    }

    private fun atualizaListaDeTorneios(response: HomeFragmentResponse) {
        val listTournaments = mutableListOf<com.draccoapp.poker.api.model.response.homeFrament.Tournament>()
        response.tournamentsImIn?.forEach {
            listTournaments.add(it.tournament)
        }
        applicantAdapter.updateList(listTournaments)
    }

    private fun setupUI(response: User) {
        binding.apply {

            imgProfile.load(response.profilePicture) {
                crossfade(true)
                placeholder(R.drawable.ic_photo_profile)
                error(R.drawable.ic_photo_profile)
            }
            textName.text = response.name
        }
    }

    private fun onclick() {
        binding.apply {

            btnApplicantSeeAll.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToApplicantTournamentFragment()
                    )
            }

            buttonProfileAbout.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToAboutFragment()
                    )
            }

            buttonProfileContract.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToContractFragment()
                    )
            }

            buttonProfileHelp.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToCenterHelpFragment()
                    )
            }

            buttonProfileTerms.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToTermsUseFragment()
                    )
            }

            buttonProfilePolitic.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToPoliticPrivacyFragment()
                    )
            }

            buttonProfileContact.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToContactFragment()
                    )
            }

            buttonProfileLogout.setOnClickListener {
                viewModel.logout()
                startActivity(Intent(requireContext(), AccountActivity::class.java))
                requireActivity().finishAffinity()
            }

            binding.btnSelectBrazil.setOnClickListener {
                setLanguagePt()
            }

            binding.btnSelectEUA.setOnClickListener {
                setLanguageEn()
            }

        }
    }

    private fun setupRecycler() {

        applicantAdapter = TournamentMineAdapterNew(requireContext()) {
            //click no card
        }

        binding.recyclerDone.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = applicantAdapter
        }


    }


    private fun setLanguagePt() {
        preferences.setLanguage("pt")
        setLocale("pt")
    }

    private fun setLanguageEn() {
        preferences.setLanguage("en")
        setLocale("en")
    }

    fun setLocale(lang: String?) {
        val myLocale = Locale(lang)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

        // Recriar o LoginFragment usando o NavController
        val navController = findNavController()
        navController.navigate(R.id.profileFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}