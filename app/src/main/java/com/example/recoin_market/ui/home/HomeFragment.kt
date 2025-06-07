package com.example.recoin_market.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.recoin_market.R
import com.example.recoin_market.databinding.FragmentHomeBinding
import com.example.recoin_market.models.Char_result
import com.example.recoin_market.showToast
import com.example.recoin_market.visibility
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    var my_adapter: CharAdapter? = null
    var lotty_earn_coins: LottieAnimationView? = null

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = mAuth.currentUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        lotty_earn_coins = binding.lottyCoins
        lotty_earn_coins!!.setAnimation(R.raw.one_coin)
        binding.progressBar.visibility(true)

        homeViewModel.getResponseLiveData().observe(
            viewLifecycleOwner,
            object : Observer<List<Char_result?>?> {   // viewLiffecycleOwner следит за жизнью фрагментов

                override fun onChanged(model_list: List<Char_result?>?) {

                    binding.progressBar.visibility(false)

                    my_adapter = CharAdapter(requireActivity(), model_list)
                    binding.salihList.adapter = my_adapter
                    showToast(requireActivity(), "AAAAAAAAA")
                    Log.e("TAG", "GGGGGGGG +${model_list.toString()} " )


                }
            }
        )

        setUpOnBackPressed()





        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnFromMainPage.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_login_Activity)
        }
        binding.bainTv.setOnClickListener {
            var intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.cartoonnetwork.co.uk/videos"))
            startActivity (intent)
        }
        binding.boomTv.setOnClickListener {
            var intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.boomerangtv.co.uk/videos"))
            startActivity(intent)
        }
        binding.nicelodianTv.setOnClickListener {
            var intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/@NickelodeonCyrillic")
                )
            startActivity(intent)
        }
        binding.titanTv.setOnClickListener {
            var intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=JgTbF05edtM"))
            startActivity (intent)
        }
        binding.howMoreBtn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard)
        }
        binding.changeFavoriteBtn.setOnClickListener {
            binding.explain1200Coins.visibility(true)
        }
        binding.okBtn.setOnClickListener {
            binding.explain1200Coins.visibility(false)
        }
        binding.changeMyPage.setOnClickListener {
            if (user == null) {
                showToast(
                    requireContext(),
                    "You can't go to Your Individual Page. Please Login and try again"
                )
                findNavController().navigate(R.id.action_navigation_home_to_login_Activity)
            } else {
                findNavController().navigate(R.id.action_navigation_home_to_cabinetFragment)
            }
        }
        binding.okMyPage.setOnClickListener {
            binding.cardviewGoToMyPage.visibility(false)
        }
        binding.logOutBtn.setOnClickListener {
            mAuth.signOut()
            findNavController().navigate(R.id.action_navigation_home_to_login_Activity)
        }


    }


    private fun setUpOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isEnabled) {
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