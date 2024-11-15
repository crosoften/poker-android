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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.response.homeFrament.NextTournament
import com.draccoapp.poker.api.modelOld.response.Tournament
import com.draccoapp.poker.databinding.FragmentHomeBinding
import com.draccoapp.poker.extensions.getPreferenceData
import com.draccoapp.poker.ui.adapters.adaptersNew.TournamentAdapterNew
import com.draccoapp.poker.ui.adapters.adaptersNew.TournamentMineAdapterNew
import com.draccoapp.poker.utils.Preferences
import com.draccoapp.poker.utils.mapTournamentInImTournament
import com.draccoapp.poker.utils.mapTournamentToNextTournament
import com.draccoapp.poker.viewModel.HomeViewModel
import com.draccoapp.poker.viewModel.TournamentViewModel
import com.draccoapp.poker.viewModel.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
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

    private var listApplicant: MutableList<Tournament> = mutableListOf()
    private var listNext: MutableList<Tournament> = mutableListOf()

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
        setupLocation()
        checkPermissions()
        setupObserver()
        onclick()
        setupRecycler()
        val preferences = Preferences(requireContext())
        Log.i("TokenWill", "onViewCreated: Token no homefrag é   ${preferences.getToken()}")
    }

    fun location(){
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Verifique se a permissão de localização foi concedida
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Obter a última localização conhecida
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            Log.e("gps",lastKnownLocation.toString())

            // Verificar se a última localização conhecida é válida
            if (lastKnownLocation != null) {
                Log.e("gps","dentro do if")
                currentLocation = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                requireContext().getPreferenceData().location(currentLocation)
                homeViewModel.getHomeFragment()
                viewModel.getTounamentImIn()
            } else {
                // Caso a última localização não esteja disponível, solicite uma nova atualização de localização
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location) {
                        Log.e("gps","dentro do else")
                        currentLocation = LatLng(location.latitude, location.longitude)
                        requireContext().getPreferenceData().location(currentLocation)
                        homeViewModel.getHomeFragment()
                        viewModel.getTounamentImIn()
                    }

                    override fun onProviderDisabled(provider: String) {
                        // Tratamento quando o provedor de localização está desativado
                    }

                    override fun onProviderEnabled(provider: String) {
                        // Tratamento quando o provedor de localização é ativado
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        // Tratamento de mudanças de status do provedor de localização
                    }
                }, null)
            }
        } else {
            // Solicitar permissão de localização, se necessário
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_LOCATION
            )
        }

        // Restante do seu código aqui...
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
        viewModel.successTournamentInIm.observe(viewLifecycleOwner) { response ->
            response.data?.let { tournamentMineAdapter.updateList(it) }
        }


        homeViewModel.successHomeFragment.observe(viewLifecycleOwner) { response ->
            Log.i(TAG, "setupObserver: Os dados todos da home foram $response")
            binding.textName.text = response.myself.name

            Glide.with(requireContext()).load(response.myself.imageUrl)
                .placeholder(R.drawable.ic_profile)
                .into(binding.shapeableImageView)
            binding.textView6.text = response.myself.playerLevel
            binding.txtTempoRestanteContrato.text = response.myself.contractExpiresIn
            binding.txtSeusTorneios.text = response.myself.tournamentsCount.toString()
            binding.txtLucroContratoAtual.text = response.myself.contractProfit.toString()
            binding.textView7.text = response.myself.ranking.toString()

            response.nextTournaments?.let { nextTournamentAdapter.updateList(it) }

//            val listTournaments = mutableListOf<com.draccoapp.poker.api.model.response.homeFrament.Tournament>()
//            response.tournamentsImIn?.forEach {
//                listTournaments.add(it.tournament)
//            }

            nextTournamentData = response.nextTournaments.let{
                it!!
            }

//            tournamentMineAdapter.updateList(listTournaments)


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

        tournamentMineAdapter = TournamentMineAdapterNew(requireContext(),{
           val nextTournamente = mapTournamentInImTournament(it)
            findNavController()
                .navigate(
                    HomeFragmentDirections
                        .actionHomeFragmentToDetailTournamentFragment(
                            nextTournamente,
                            "approved",
                            it.id!!,
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
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = tournamentMineAdapter
        }

        nextTournamentAdapter = TournamentAdapterNew(requireContext()) {
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
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = nextTournamentAdapter
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