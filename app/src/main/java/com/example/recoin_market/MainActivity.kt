package com.example.recoin_market

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recoin_market.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.cabinetFragment)
        )

        navView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener(
            object :NavController.OnDestinationChangedListener{
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {

                    val list = ArrayList<Int>()
                    list.add(R.id.marketFragment)
                    list.add(R.id.reg_Activity)
                    list.add(R.id.login_Activity)
                    list.add(R.id.PDFFragment)
                    list.add(R.id.navigation_home)
                    list.add(R.id.navigation_payment)
                    list.add(R.id.navigation_dashboard)

                    if(list.contains(destination.id)) {
                        navView.visibility = View.GONE
                    }else {
                        navView.visibility = View.VISIBLE
                    }

                }
            }
        )

    }
}