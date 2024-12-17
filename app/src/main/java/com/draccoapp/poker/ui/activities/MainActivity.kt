package com.draccoapp.poker.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.ActivityMainBinding
import com.draccoapp.poker.utils.Preferences

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val preferences by lazy { Preferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
        supportActionBar?.hide()

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        navController = navHostFragment.findNavController()
        binding.navView.setupWithNavController(navController)
        hideTournamentCaseDosntHaveContract()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.homeFragment -> binding.navView.visibility = View.VISIBLE
                R.id.tournamentFragment -> binding.navView.visibility = View.VISIBLE
                R.id.coachFragment -> binding.navView.visibility = View.VISIBLE
                R.id.profileFragment -> binding.navView.visibility = View.VISIBLE
                else -> binding.navView.visibility = View.GONE
            }
        }

    }

    private fun hideTournamentCaseDosntHaveContract() {
        val contractStatus = preferences.getContractStatus()
        Log.i("dadosTeste", "hideTournamentCaseDosntHaveContract: $contractStatus")
        val item = binding.navView.findViewById<View>(R.id.tournamentFragment)
        if (contractStatus) {
            item.visibility = View.VISIBLE
        } else {
            item.visibility = View.GONE
        }
    }

}