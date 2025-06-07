package com.example.recoin_market.ui.notifications

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.recoin_market.databinding.FragmentPaymentBinding

class payment_fragment : Fragment() {

    var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPaymentBinding.inflate(inflater,container,false)
        val root:View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardPaypal.setOnClickListener{ v1 ->
            val myIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://paypal.com/")
            )
            startActivity(myIntent)

        }

        binding.cardMbank.setOnClickListener{ v2 ->
            val myIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://paypal.com/")
            )
            startActivity(myIntent)

        }

        binding.cardVisa.setOnClickListener{ v3 ->
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.com/"))
            startActivity(myIntent)

        }

        binding.cardNurtelecom.setOnClickListener{ v4 ->
            val myIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://paypal.com/")
            )
            startActivity(myIntent)

        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}