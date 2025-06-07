package com.example.recoin_market.ui.oferts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.recoin_market.R
import com.example.recoin_market.databinding.FragmentOffertaBinding
import com.example.recoin_market.visibility


class OffertaFragment : Fragment() {

    private var _binding: FragmentOffertaBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOffertaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpOnBackPressed()




        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_offertaFragment_to_navigation_dashboard)
        }
        binding.chekOffertaBtn.setOnClickListener {
            binding.registrationBtn.visibility(true)
            binding.registrationBlobBtn.visibility (false)
        }
        binding.registrationBtn.setOnClickListener {
            findNavController().navigate(R.id.action_offertaFragment_to_reg_Activity)
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


}