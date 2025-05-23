package com.example.recoin_market.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.recoin_market.R
import com.example.recoin_market.databinding.FragmentHomeBinding
import com.example.recoin_market.models.Char_result
import com.example.recoin_market.visibility
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    var adapter: charAdapter? = null
    var lotty_earn_coins: LottieAnimationView? = null

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = mAuth.currentUser



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        lotty_earn_coins = binding.lottyCoins
        lotty_earn_coins!!.setAnimation(R.raw.one_coin)
        binding.progressBar.visibility(true)

        homeViewModel.getResponseLiveData().observe(
            viewLifecycleOwner, object  : Observer <List<Char_result?>?>{   // viewLiffecycleOwner следит за жизнью фрагментов

                override fun onChanged(model_list: List<Char_result?>?) {

                    binding.progressBar.visibility(false)
                    adapter = charAdapter(requireActivity(), model_list)
                    binding.rvMainList.adapter = adapter

                    mAuth.signOut()
                    findNavController().navigate(R.id.ac)



                }
            }
        )

        setUpOnBackPressed()





        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.


    }




    private fun setUpOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    if(isEnabled) {
                        requireActivity()
                            .finish()
                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}