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
import coil.load
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.request.UpdateLocation
import com.draccoapp.poker.api.model.response.Tournament
import com.draccoapp.poker.api.model.response.User
import com.draccoapp.poker.databinding.FragmentHomeBinding
import com.draccoapp.poker.ui.adapters.TournamentAdapter
import com.draccoapp.poker.extensions.showSnackBarRed
import com.draccoapp.poker.viewModel.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val TAG = "HomeFragment"
    private val viewModel : UserViewModel by viewModel()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var applicantAdapter: TournamentAdapter
    private lateinit var nextAdapter: TournamentAdapter
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

    }

    private fun checkPermissions(){
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
        ActivityResultContracts.RequestMultiplePermissions()){
        when {
            it.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
            it.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && ActivityCompat.checkSelfPermission(
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
        ActivityResultContracts.RequestPermission()){
        if(it){
            setLocation()
        } else {
            Log.e(TAG, "Permissão negada")
        }
    }


    private fun setupLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    @SuppressLint("MissingPermission")
    private fun setLocation(){
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

//        viewModel.getUserById()
//        viewModel.getTournamentsAvailableToUser()
//        viewModel.getTournamentsJoinedByUser()
//
//        viewModel.user.observe(viewLifecycleOwner) { response ->
//            setupUI(response)
//        }
//
        viewModel.tournamentApplicant.observe(viewLifecycleOwner) { response ->
            applicantAdapter.updateList(response)
            applicantAdapter.setUnit(viewModel.getUnit())
        }
//
        viewModel.tournamentsByUser.observe(viewLifecycleOwner) { response ->
            nextAdapter.updateList(response)
            nextAdapter.setUnit(viewModel.getUnit())
        }
//
//        viewModel.error.observe(viewLifecycleOwner) { error ->
//            error?.let {
//                binding.root.showSnackBarRed(it)
//            }
//        }

    }

    private fun setupUI(response: User) {
        binding.apply {

//            shapeableImageView.load(response.profilePicture) {
//                crossfade(true)
//                placeholder(R.drawable.ic_profile)
//                error(R.drawable.ic_profile)
//            }
            textName.text = response.name
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

        applicantAdapter = TournamentAdapter(::onClickTournament)

        binding.recyclerDone.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = applicantAdapter
        }

        nextAdapter = TournamentAdapter(::onClickTournament)

        binding.recyclerNext.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = nextAdapter
        }
    }

    private fun onClickTournament(tournament: Tournament){
        findNavController()
            .navigate(
                HomeFragmentDirections
                    .actionHomeFragmentToDetailTournamentFragment(
                        tournament
                    )
            )
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}