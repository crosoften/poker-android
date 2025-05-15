package com.draccoapp.poker.ui.fragments.tournament

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.response.tournamentInIm.TournamentInImData
import com.draccoapp.poker.databinding.FragmentDetailTournamentBinding
import com.draccoapp.poker.extensions.getPreferenceData
import com.draccoapp.poker.utils.converterDataNextTournament
import com.draccoapp.poker.utils.converterDistance
import com.draccoapp.poker.viewModel.TournamentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailTournamentFragment : Fragment() {

    private var _binding: FragmentDetailTournamentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TournamentViewModel by viewModel()

    private val args by navArgs<DetailTournamentFragmentArgs>()

    private val tournament by lazy { args.tournament }

    // Variável para armazenar o status atualizado do torneio
    private var currentStatus: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailTournamentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        viewModel.getStatusTournaments()

        viewModel.successTournamentInIm.observe(viewLifecycleOwner) { response ->
            val tournamentList = response?.data
            val myTournament = tournamentList?.find { it.tournament?.id == tournament.id }

            currentStatus = myTournament?.status ?: "unknown"

            setupViewForStatus(currentStatus ?: "")
            onClick(currentStatus)
        }
    }

    private fun setupUI() {
        Glide.with(requireContext())
            .load(tournament.imageUrl)
            .placeholder(R.drawable.img_placeholder_poker)
            .into(binding.imageView2)

        binding.textView13.text = tournament.title
        binding.textView14.text = converterDataNextTournament(tournament.startDatetime.toString())
        binding.textView20.text = buildString {
            append(converterDataNextTournament(tournament.startDatetime.toString()))
            append(" - ")
            append(tournament.time)
        }
        binding.dateFinish.text = converterDataNextTournament(tournament.finalDatetime.toString())
        binding.textView16.text = tournament.prize.toString()
        binding.textView18.text = tournament.description

        binding.textView22.text = buildString {
            append(tournament.location?.street)
            append(", ")
            append(tournament.location?.number)
            append(", ")
            append(tournament.location?.city)
            append("\n")
            append(getString(R.string.zip_code))
            append(": ")
            append(tournament.location?.zipCode)
            append("\n")
            append(tournament.location?.state)
            append(", ")
            append(tournament.location?.country)
        }

        val distance = tournament.location.distance
        val type = requireContext().getPreferenceData().getLanguage().ifBlank { "PT" }
        binding.textView23.text = converterDistance(distance, type)

        binding.buttonLink.setOnClickListener {
            if (!tournament.eventUrl.isNullOrEmpty() && tournament.eventUrl!!.contains("https")) {
                Log.d("BUTTON_LINK", "Opening URL: ${tournament.eventUrl}")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tournament.eventUrl))
                startActivity(intent)
            } else {
                Log.d("BUTTON_LINK", "No URL found")
                Toast.makeText(requireContext(), "Link não encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupViewForStatus(status: String) {
        when (status.lowercase()) {
            "approved" -> {
                binding.tvStatus.backgroundTintList = resources.getColorStateList(R.color.status_aproved, null)
                binding.tvAttTour.visibility = View.VISIBLE
                binding.tvStatus.visibility = View.VISIBLE
                binding.btnReport.visibility = View.VISIBLE
                binding.tvStatus.text = getString(R.string.approved)
                binding.buttonInscrever.visibility = View.VISIBLE
                binding.btnReport.visibility = View.VISIBLE
                binding.buttonInscrever.text = getString(R.string.complete)
            }
            "pending" -> {
                binding.tvStatus.backgroundTintList = resources.getColorStateList(R.color.status_peding, null)
                binding.tvStatus.visibility = View.VISIBLE
                binding.tvStatus.text = getString(R.string.pending)
                binding.buttonInscrever.visibility = View.GONE
                binding.btnReport.visibility = View.GONE
                binding.tvAttTour.visibility = View.GONE
            }
            "repproved" -> {
                binding.tvStatus.backgroundTintList = resources.getColorStateList(R.color.status_repproved, null)
                binding.tvStatus.visibility = View.VISIBLE
                binding.tvStatus.text = getString(R.string.repproved)
                binding.buttonInscrever.visibility = View.GONE
                binding.btnReport.visibility = View.GONE
                binding.tvAttTour.visibility = View.GONE
            }
            "closed" -> {
                binding.tvStatus.backgroundTintList = resources.getColorStateList(R.color.status_closed, null)
                binding.tvStatus.visibility = View.VISIBLE
                binding.tvStatus.text = getString(R.string.closed)
                binding.buttonInscrever.visibility = View.GONE
                binding.btnReport.visibility = View.GONE
                binding.tvAttTour.visibility = View.GONE
            }
            else -> {
                binding.tvStatus.visibility = View.GONE
                binding.buttonInscrever.visibility = View.VISIBLE
                binding.btnReport.visibility = View.GONE
                binding.tvAttTour.visibility = View.GONE
                binding.buttonInscrever.text = getString(R.string.sign_up)
            }
        }
    }

    private fun onClick(status: String?) {
        Log.d("DetailTournament", "onClick: status = $status")

        binding.apply {
            buttonInscrever.setOnClickListener {
                Log.d("DetailTournament", "buttonInscrever clicked with status: $status")

                when (status) {
                    "approved" -> {
                        findNavController().navigate(
                            DetailTournamentFragmentDirections.actionDetailTournamentFragmentToFinishTournamentFragment(
                                tournament.id ?: ""
                            )
                        )
                    }
                    "completed" -> {
                        Toast.makeText(requireContext(), "Exibindo resultados do torneio", Toast.LENGTH_SHORT).show()
                    }
                    "cancelled" -> {
                        Toast.makeText(requireContext(), "Este torneio foi cancelado", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Log.d("DetailTournament", "Status is $status, navigating to gateway")
                        findNavController().navigate(
                            DetailTournamentFragmentDirections.actionDetailTournamentFragmentToGatewayFragment(
                                tournament
                            )
                        )
                    }
                }
            }

            back.setOnClickListener {
                Log.d("DetailTournament", "back button clicked")
                findNavController().popBackStack()
            }

            btnReport.setOnClickListener {
                Log.d("DetailTournament", "btnReport clicked with idSub: ${args.idSub}, status: ${args.status}")
                findNavController().navigate(
                    DetailTournamentFragmentDirections.actionDetailTournamentFragmentToTournamentUpdateFragment(
                        args.idSub,
                        args.status ?: ""
                    )
                )
            }

            buttonLink.setOnClickListener {
                Log.d("DetailTournament", "buttonLink clicked with status: $status")

                if (status == "pending" || status == "approved") {
                    Log.d("DetailTournament", "Status is pending or approved, navigating back")
                    findNavController().popBackStack()
                    return@setOnClickListener
                }

                if (!tournament.eventUrl.isNullOrEmpty() && tournament.eventUrl!!.contains("https")) {
                    Log.d("DetailTournament", "Opening URL: ${tournament.eventUrl}")
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tournament.eventUrl))
                    startActivity(intent)
                } else {
                    Log.d("DetailTournament", "No URL found")
                    Toast.makeText(requireContext(), "Link não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
