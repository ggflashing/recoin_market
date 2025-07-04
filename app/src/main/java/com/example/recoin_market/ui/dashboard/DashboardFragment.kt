package com.example.recoin_market.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.recoin_market.R
import com.example.recoin_market.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    var lotty_dash_page: LottieAnimationView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        lotty_dash_page = binding.lottyDashPage
        lotty_dash_page!!.setAnimation(R.raw.three_coins)
        setUpOnBackPressed()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_home)
        }
        binding.tvMoreAboutOferta.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_offertaFragment)
        }
        binding.tvPay.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_payment)
        }
    }

    private fun setUpOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isEnabled) {
                        findNavController().navigate(R.id.navigation_home)
                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}