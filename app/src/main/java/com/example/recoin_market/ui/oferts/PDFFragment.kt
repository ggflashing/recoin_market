package com.example.recoin_market.ui.oferts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.recoin_market.R
import com.example.recoin_market.databinding.FragmentPDFBinding
import com.example.recoin_market.models.PDFModel
import com.example.recoin_market.remote_data.RetrofitB.okHttpClient
import com.example.recoin_market.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Request
import java.io.File


class PDFFragment : Fragment() {

    private var _binding: FragmentPDFBinding? = null
    private val binding get() = _binding!!

    private lateinit var pdfModel: PDFModel
    private lateinit var title_doc: String
    private var total_outPage_counter: String? = null
    private lateinit var id_user_s: String
    var mAuth:FirebaseAuth? = null
    var current_user: FirebaseUser? = null

    val databaseReferencePDFModel = FirebaseDatabase.getInstance().reference.child("pdfs")
    val databaseReferenceUsers: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("user_persons")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        current_user = mAuth!!.currentUser
        arguments?.let {
            pdfModel = it.getSerializable("key_pdfUrl") as PDFModel
            val firebaseKey = it.getString("firebase_key_pdf")
            pdfModel.id = firebaseKey
                    id_user_s = pdfModel.idUser
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPDFBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val pdfDownloadUrl = pdfModel.downloadUrl
        title_doc = pdfModel.fileTitle
        binding.titleOfDocOnWordPage.text = title_doc
        downloadModel(pdfDownloadUrl)
        return root

    }

    private fun downloadModel(pdfUrl: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(pdfUrl).build()

                val response = okHttpClient.newCall(request).execute()

                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        val file = File(requireContext().filesDir, "${pdfModel.fileTitle}.pdf")
                        responseBody.byteStream().use { inputStream ->

                            file.outputStream().use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                        withContext(Dispatchers.Main) {
                            loadPdfFromFile(file)
                        }

                    } ?: withContext(Dispatchers.Main) {
                        showToast(requireContext(), "Failed to download PDF: Empty response body")

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast(
                            requireContext(), "No Response from server," +
                                    "   Failed to download PDF: ${response.code}"
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast(requireContext(), "Download error: ${e.message}")
                }
            }
        }
    }


    private fun loadPdfFromFile(pdfFile: File) {
        binding.pdfViewer.fromFile(pdfFile)
            .defaultPage(0).enableDoubletap(true)
            .enableSwipe(true).swipeVertical(true)
            .onError { error ->
                showToast(requireContext(), "Error loading PDF: ${error.message}")
            }.load()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_PDFFragment_to_cabinetFragment)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }









}


