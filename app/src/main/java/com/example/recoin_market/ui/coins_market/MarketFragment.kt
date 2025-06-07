package com.example.recoin_market.ui.coins_market

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.number.IntegerWidth
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IntegerRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.parser.IntegerParser
import com.example.recoin_market.R
import com.example.recoin_market.databinding.FragmentMarketBinding
import com.example.recoin_market.models.UserModels
import com.example.recoin_market.showToast
import com.example.recoin_market.visibility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MarketFragment : Fragment() {


    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!

    private lateinit var marketAdapter: MarketAdapter
    private var target_message: String = ""
    private var to_sale: Int = 0
    private var to_buy: Int = 0

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = mAuth.currentUser

    private lateinit var new_list: List<UserModels>

    private val databaseReferenceUsers: DatabaseReference = FirebaseDatabase.getInstance().reference.child("user_persons")
    private val marketViewModel: MarketViewModel by viewModels() // без обьявления Provaider пропрадителя

@SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        if (user == null) {
            binding.textCurrentUsersEmail.text = user?.email
        } else {
            binding.textCurrentUsersEmail.text = "User is not logged in"
        }
        marketViewModel.getResponseLiveData().observe( //observe сделит за живым фрагментом
            viewLifecycleOwner, object : Observer<List<UserModels>?> {
                override fun onChanged(tempList: List<UserModels>?) {
                    marketAdapter =
                        MarketAdapter(
                            requireContext(),
                            tempList as MutableList<UserModels>
                        ) // MutableList может прийти неточное количество
                    binding.rvCoinsMarket.adapter = marketAdapter
                    new_list = tempList
                }

            }
        )

        setUpOnBackPresses()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.makeMessageBtn.setOnClickListener {
            if (user == null) {
                showToast(
                    requireContext(), "Only Authenticated Users can upload messages," +
                            "please read offers Use Condition * on Main page and Sign In"
                )

                binding.cardviewEnterMessage.visibility(false)


            } else {
                if (binding.cardviewEnterMessage.visibility == View.GONE) {
                    binding.cardviewEnterMessage.visibility(true)

                } else {
                    binding.cardviewEnterMessage.visibility(false)
                }
            }


        }

        binding.uploadNewMessage.setOnClickListener {
            target_message = binding.etMakeMessage.text.toString()
            if (target_message.isEmpty()) {
                showToast(requireContext(), "Please enter a message")
                return@setOnClickListener //возвращает к слушателю к 105

            }else {
                val updates = hashMapOf<String, Any>( //посылаем на бекенд с изменениями Any зменяния
                    "message" to target_message )
                databaseReferenceUsers.child(user!!.uid).updateChildren(updates) // относится к 112
                    .addOnSuccessListener {
                        showToast(requireContext(), "Message uploaded successfully")
                        binding.etMakeMessage.text.clear()
                    }.addOnFailureListener { error ->
                        showToast(requireContext(), "Failed to update message ${error.message}"  ) // $ расспечатывает то что я внесу
                        binding.etMakeMessage.text.clear() // стирает всегда
                        binding.cardviewEnterMessage.visibility(false)

                    }
            }

            binding.saleBuyBtn.setOnClickListener{
                if (user == null) {

                    showToast(requireContext(), "Only Authenticated Users can sale and buy coins" +
                    "please read offers use condition on main page and sign in")
                }else {
                    if (binding.cardviewEnterSaleBuyCoins.visibility == View.GONE){
                        binding.cardviewEnterSaleBuyCoins.visibility(true)
                    }else {
                        binding.cardviewEnterSaleBuyCoins.visibility(true)
                    }
                }
            }

            binding.uploadChangeSaleBuy.setOnClickListener{
                var to_saleStr = binding.etToSale.text.toString()
                var to_buyStr = binding.etToBuy.text.toString()
                if (to_saleStr.isEmpty() || to_buyStr.isEmpty()) {
                    showToast(requireContext(), "Please enter a number please" +
                    "If would to sale or buy, enter 0 instead")
                    return@setOnClickListener
                }else {
                    to_sale =  Integer.parseInt(binding.etToSale.text.toString())
                    to_buy = Integer.parseInt(binding.etToBuy.text.toString())

                    val updatesSaleCoins = hashMapOf<String,Any>(
                        "sales" to to_sale // под ключем модельков usermodel
                    )

                    val updatesBuyCoins = hashMapOf<String,Any>(
                        "bying" to to_buy
                    )

                    databaseReferenceUsers.child(user!!.uid).updateChildren(updatesSaleCoins)
                        .addOnSuccessListener {
                            showToast(requireContext(), "Coins to sale uploadrd successfully")
                            binding.etToSale.text.clear()

                        }
                        .addOnSuccessListener {
                            showToast(requireContext(),"failed to update coins to sale")
                            binding.etToSale.text.clear()
                        }
                    databaseReferenceUsers.child(user!!.uid).updateChildren(updatesBuyCoins)
                        .addOnSuccessListener {
                            showToast(requireContext(),"Coins to buy uploaded successfully")
                            binding.etToBuy.text.clear()
                        }
                        .addOnFailureListener{
                            showToast(requireContext(), "Failed to update coins to buy")
                            binding.etToBuy.text.clear()
                        }

                    binding.cardviewEnterSaleBuyCoins.visibility(false)

                }



            }

        }


    }

    private fun setUpOnBackPresses() {
        requireActivity().onBackPressedDispatcher
            .addCallback(object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    if (isEnabled) {

                        findNavController().navigate(R.id.navigation_home)
                    }
                }

            })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
