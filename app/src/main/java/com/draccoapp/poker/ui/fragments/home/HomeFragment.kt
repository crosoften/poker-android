package com.draccoapp.poker.ui.fragments.home


import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.draccoapp.poker.api.model.response.homeFrament.NextTournament
import com.draccoapp.poker.api.model.response.homeFrament.TournamentsImIn
import com.draccoapp.poker.api.modelOld.response.Tournament
import com.draccoapp.poker.databinding.FragmentHomeBinding
import com.draccoapp.poker.ui.adapters.TournamentAdapter
import com.draccoapp.poker.ui.adapters.adaptersNew.TournamentAdapterNew
import com.draccoapp.poker.ui.adapters.adaptersNew.TournamentMineAdapterNew
import com.draccoapp.poker.utils.Preferences
import com.draccoapp.poker.utils.mapTournamentToNextTournament
import com.draccoapp.poker.viewModel.HomeViewModel
import com.draccoapp.poker.viewModel.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val TAG = "HomeFragment"
    private val viewModel: UserViewModel by viewModel()
    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var tournamentMineAdapter: TournamentMineAdapterNew
    private lateinit var nextTournamentAdapter: TournamentAdapterNew
    private var listApplicant: MutableList<Tournament> = mutableListOf()
    private var listNext: MutableList<Tournament> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupLocation()
        checkPermissions()
        setupObserver()
        onclick()
        setupRecycler()
//        initModels()
        val preferences = Preferences(requireContext())

        Log.i("TokenWill", "onViewCreated: Token no homefrag é   ${preferences.getToken()}")
        homeViewModel.getHomeFragment()


    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissions.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            setLocation()
        }

    }

    private val locationPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        when {
            it.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

            it.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionLauncher.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
            }
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            setLocation()
        } else {
            Log.e(TAG, "Permissão negada")
        }
    }


    private fun setupLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    @SuppressLint("MissingPermission")
    private fun setLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
//                    viewModel.updateLocation(
//                        UpdateLocation(
//                            location.latitude,
//                            location.longitude
//                        )
//                    )
                }
            }

    }

    private fun setupObserver() {
        homeViewModel.successHomeFragment.observe(viewLifecycleOwner) { response ->
            Log.i(TAG, "setupObserver: Os dados todos da home foram $response")


            binding.textName.text = response.myself.name

            Glide.with(requireContext()).load(response.myself.imageUrl).into(binding.shapeableImageView)
            binding.textView6.text = response.myself.playerLevel
            binding.txtTempoRestanteContrato.text = response.myself.contractExpiresIn
            binding.txtSeusTorneios.text = response.myself.tournamentsCount.toString()
            binding.txtLucroContratoAtual.text = response.myself.contractProfit.toString()
            binding.textView7.text = response.myself.ranking.toString()

            nextTournamentAdapter.updateList(response.nextTournaments)

            val listTournaments = mutableListOf<com.draccoapp.poker.api.model.response.homeFrament.Tournament>()
            response.tournamentsImIn.forEach {
                listTournaments.add(it.tournament)
            }

            tournamentMineAdapter.updateList(listTournaments)


        }
    }


    private fun onclick() {
        binding.apply {

            btnApplicantSeeAll.setOnClickListener {
                findNavController()
                    .navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToApplicantTournamentFragment()
                    )
            }

            btnNextSeeAll.setOnClickListener {
                findNavController()
                    .navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToNextTournamentFragment()
                    )
            }

        }
    }

    private fun setupRecycler() {

        tournamentMineAdapter = TournamentMineAdapterNew(){
           val nextTournamente = mapTournamentToNextTournament(it)
            findNavController()
                .navigate(
                    HomeFragmentDirections
                        .actionHomeFragmentToDetailTournamentFragment(
                            nextTournamente
                        )
                )
        }

        binding.recyclerDone.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = tournamentMineAdapter
        }

        nextTournamentAdapter = TournamentAdapterNew(requireContext()) {
            findNavController()
                .navigate(
                    HomeFragmentDirections
                        .actionHomeFragmentToDetailTournamentFragment(
                            it
                        )
                )
        }

        binding.recyclerNext.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = nextTournamentAdapter
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}