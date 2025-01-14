package com.draccoapp.poker.ui.fragments.home


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.response.homeFrament.NextTournament
import com.draccoapp.poker.api.model.type.DataState
import com.draccoapp.poker.databinding.FragmentHomeBinding
import com.draccoapp.poker.extensions.getPreferenceData
import com.draccoapp.poker.ui.adapters.adaptersNew.TournamentAdapterNew
import com.draccoapp.poker.ui.adapters.adaptersNew.TournamentMineAdapterNew
import com.draccoapp.poker.utils.mapTournamentInImTournament
import com.draccoapp.poker.viewModel.HomeViewModel
import com.draccoapp.poker.viewModel.TournamentViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val TAG = "HomeFragment"
    private val homeViewModel: HomeViewModel by viewModel()
    private val viewModel: TournamentViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var tournamentMineAdapter: TournamentMineAdapterNew
    private lateinit var nextTournamentAdapter: TournamentAdapterNew
    private lateinit var nextTournamentData: List<NextTournament>
    private lateinit var currentLocation: LatLng
    private lateinit var locationManager: LocationManager

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
        location()
        setupRecycler()
        setupLocation()
        checkPermissions()
        setupObserver()
        onclick()
    }

    fun location() {
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                currentLocation = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                requireContext().getPreferenceData().location(currentLocation)
                homeViewModel.getHomeFragment()
                viewModel.getTounamentImIn()
            } else {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,
                    { location ->
                        Log.e("gps", "dentro do else")
                        currentLocation = LatLng(location.latitude, location.longitude)
                        requireContext().getPreferenceData().location(currentLocation)
                        homeViewModel.getHomeFragment()
                        viewModel.getTounamentImIn()
                    }, null)
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_LOCATION
            )
        }
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
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
            it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
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
                    nextTournamentAdapter.updateLocation(location)
                    Log.i("localizacao", "setLocation: $location")
                }
            }

    }

    private fun setupObserver() {
        lifecycleScope.launch {
            homeViewModel.appState.collect { state ->
                when(state){
                    DataState.Success -> hideLoading()
                    DataState.Loading -> showLoading()
                    DataState.Error -> hideLoading()
                    DataState.Idle -> hideLoading()
                }
            }
        }

        observeTournaments()
        observeViewData()
    }

    private fun showLoading() {
        binding.apply {
            loadingIndicator.visibility = View.VISIBLE
            mainContainer.visibility = View.INVISIBLE
        }
    }

    private fun hideLoading() {
        binding.apply {
            loadingIndicator.visibility = View.GONE
            mainContainer.visibility = View.VISIBLE
        }
    }

    private fun observeViewData() {
        homeViewModel.successHomeFragment.observe(viewLifecycleOwner) { response ->
            binding.textName.text = response.myself.name

            Glide.with(requireContext()).load(response.myself.imageUrl)
                .placeholder(R.drawable.ic_profile)
                .into(binding.shapeableImageView)
            binding.tvPlayerLevelValue.text = response.myself.playerLevel
            binding.txtTempoRestanteContrato.text = response.myself.contractExpiresIn ?: "0"
            binding.txtSeusTorneios.text = response.myself.tournamentsCount.toString()
            binding.txtLucroContratoAtual.text = response.myself.contractProfit.toString()
            binding.tvPlayerRakingValue.text = response.myself.ranking.toString()
            response.nextTournaments?.let { nextTournamentAdapter.updateList(it) }
            nextTournamentData = response?.nextTournaments ?: emptyList()
        }
    }

    private fun observeTournaments() {
        viewModel.successTournamentInIm.observe(viewLifecycleOwner) { response ->
            response.data?.let { tournamentMineAdapter.updateList(it) }
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
        setupMyTournamentRecycler()

        nextTournamentAdapter = TournamentAdapterNew(requireContext(), location = null) {
            findNavController()
                .navigate(
                    HomeFragmentDirections
                        .actionHomeFragmentToDetailTournamentFragment(
                            it,
                            null,
                            "null",
                            "next"
                        )
                )
        }

        binding.recyclerNext.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = nextTournamentAdapter
        }
    }

    private fun setupMyTournamentRecycler() {
        tournamentMineAdapter = TournamentMineAdapterNew(requireContext(), { tournament ->
            val nextTournamente = mapTournamentInImTournament(tournament)
            findNavController()
                .navigate(
                    HomeFragmentDirections
                        .actionHomeFragmentToDetailTournamentFragment(
                            nextTournamente,
                            "approved",
                            tournament.id!!,
                            "sub"
                        )
                )
        }, {
            if (it.status == "approved") {
                findNavController()
                    .navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToAddUpdateFragment(it.id!!)
                    )
            } else if (it.status == "pending") {
                Toast.makeText(requireContext(), "Aguardando aprovação", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Torneio fechado", Toast.LENGTH_SHORT).show()
            }
        })

        binding.recyclerDone.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = tournamentMineAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null


    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val PERMISSION_REQUEST_LOCATION = 1001
    }

}