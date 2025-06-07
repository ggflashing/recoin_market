package com.example.recoin_market.ui.oferts

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.recoin_market.R
import com.example.recoin_market.databinding.FragmentCabinetBinding
import com.example.recoin_market.models.PDFModel
import com.example.recoin_market.models.WordModel
import com.example.recoin_market.showToast
import com.example.recoin_market.visibility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class CabinetFragment : Fragment() {


    private var _binding: FragmentCabinetBinding? = null
    private val binding get() = _binding!!
    var mAuth: FirebaseAuth? = null
    var current_user: FirebaseUser? = null
    private lateinit var pdfFileLauncher: ActivityResultLauncher<String>
    private lateinit var wordFileLauncher: ActivityResultLauncher<String>
    private lateinit var storageReferencePDF: StorageReference
    private lateinit var databaseReferencePDF: DatabaseReference
    private lateinit var storageReferenceWord: StorageReference
    private lateinit var databaseReferenceWord: DatabaseReference
    private var pdfFileUri: Uri? = null
    private var wordFileUri: Uri? = null
    private lateinit var docPDFAdapter: DocPDFAdapter
    private lateinit var docWordAdapter: DocWordAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mAuth = FirebaseAuth.getInstance()
        current_user = mAuth!!.currentUser // есть ли user? зарегистрированный на нашей базе данных firebase



        storageReferencePDF = FirebaseStorage.getInstance().reference.child("pdfs") //storageReferencePDF фотки база данных databaseReferencePDF
        databaseReferencePDF = FirebaseDatabase.getInstance().reference.child("pdfs")
        storageReferenceWord = FirebaseStorage.getInstance().reference.child("words")//storageReferenceWord фотки база данных databaseReferenceWord
        databaseReferenceWord = FirebaseDatabase.getInstance().reference.child("words")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCabinetBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.textCurrentUsersEmail.text = current_user!!.email.toString()

        setUpOnBackPressed()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_cabinetFragment_to_navigation_home)
        )
        binding.selectPdfBtn.setOnClickListener {
            pdfFileLauncher.launch("application/pdf/*")
        }
        binding.selectWordBtn.setOnClickListener {
            wordFileLauncher.launch("application/docx/*")
        }
        pdfFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            pdfFileUri = uri
            handlePdfFileSelection(uri)
        }
        wordFileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            wordFileUri = uri
            handleWordFileSelection(uri)
        }
        binding.uploadPdfBtn.setOnClickListener {
            if (pdfFileUri != null) {
                upLoadPdfFileToFirebase()
            } else {
                showToast(requireActivity(), "PLEASE SELECT PDF FILE FROM  MEDIA STORE")
            }
        }
        binding.uploadWordBtn.setOnClickListener {
            if (wordFileUri != null) {
                upLoadWordFileToFirebase()
            } else {
                showToast(requireActivity(), "PLEASE SELECT WORD FILE FROM  MEDIA STORE")
            }
        }
        binding.showAllSentScenarios.setOnClickListener {
            docPDFAdapter = DocPDFAdapter()
            docWordAdapter = DocWordAdapter()

            //Здесь вы присоединяете прослушиватель к ссылке на базу данных Firebase для документов pdf
            databaseReferencePDF.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (_binding == null) {
                        Log.w("CabinetFragment", "onDataChange called after view destroyed")
                        return
                        // Exit the listener if the CabinetFragment is destroyed
                    }
                    //формируете сначала пустой список для базы данных pdf
                    val tempListPdf = mutableListOf<PDFModel>()
                    //Перебирает дочерние элементы  для "снимка" (фиксации) в базе данных Firebase
                    // Каждый дочерний (children) элемент будет представлять запись документа Pdf.
                    snapshot.children.forEach { dataSnapshot ->
                        val pdfModel = dataSnapshot.getValue(PDFModel::class.java)
                        pdfModel?.let { it ->
                            //Set the Firebase key to the WordModel object
                            it.id = dataSnapshot.key.toString()
                            tempListPdf.add(it)
                        }
                    }
                    if (tempListPdf == null) {
                        showToast(requireActivity(), "Data not found")
                    } else {
                        docPDFAdapter.submitList(tempListPdf)
                        binding.rvListOfScenesPdf.adapter = docPDFAdapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Check if the binding is still valid before accessing it
                    if (_binding == null) {
                        Log.w(
                            "CabinetFragment", "onCancelled called after view destroyed"
                        )
                        return // Exit the listener if the view is destroyed
                    }
                    showToast(requireActivity(), "Data's getting was canceled " + error.message)
                }
            })


//Здесь вы присоединяете прослушиватель
// к ссылке на базу данных Firebase для документов Word.

            databaseReferenceWord.addValueEventListener(object : ValueEventListener {
                // метод onDataChange автоматически вызывается Firebase
                // при изменении данных в databaseReferenceWord.
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (_binding == null) {
                        return
                        // Exit the listener if the CabinetFragment is destroyed
                    }
                    //формируете сначала пустой список для базы данных

                    val tempListWord = mutableListOf<WordModel>()
                    //Перебирает дочерние элементы  для "снимка" (фиксации) в базе данных Firebase

                    // Каждый дочерний (children) элемент будет представлять запись документа Word.

                    snapshot.children.forEach { dataSnapshot ->
                        val wordModel = dataSnapshot.getValue(WordModel::class.java)
                        wordModel?.let { it ->
                            //Сажает ключ Firebase к объекту WordModel
                            it.id = dataSnapshot.key.toString()
                            tempListWord.add(it)
                        }
                    }
                    if (tempListWord.isEmpty()) {
                        showToast(requireActivity(), "Word documents data not found")
                    } else {
                        docWordAdapter.submitList(tempListWord)
                        binding.rvListOfScenesWord.adapter = docWordAdapter
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    // Проверяет не закрыта ли совсем страница CabinetFragment
                    if (_binding == null) {
                        return // Exit the listener if the view is destroyed
                    }
                    showToast(requireActivity(), "Data's getting was canceled " + error.message)
                }

            })

        }
    }


    private fun upLoadPdfFileToFirebase() {
        val fileName = binding.titlePdfFile.text.toString()
        val my_referencePDF =
            storageReferencePDF.child("${System.currentTimeMillis()}/$fileName") // System.currentTimeMillis() берет с нашего телефона время
        pdfFileUri?.let { uri ->
            my_referencePDF.putFile(uri).addOnSuccessListener {
                my_referencePDF.downloadUrl.addOnSuccessListener { downloadUri ->
                    val pdfModel = PDFModel(
                        null,
                        fileName, downloadUri.toString(),
                        current_user!!.uid, 0
                    )
                    // Генерирует уникальный ключ для нового объекта pdfModel
                    val pushKey = databaseReferencePDF.push().key
                    if (pushKey != null) {
                        //Сажает значение по уникальному ключу
                        databaseReferencePDF.child(pushKey).setValue(pdfModel)
                            .addOnSuccessListener {
                                pdfFileUri = null //очиститься место для нового файла
                                binding.titlePdfFile.text = "pdf_selected"

                                showToast(requireActivity(), "PDF file uploaded successfully")
                                if (binding.progressBar.isShown)
                                    binding.progressBar.visibility(
                                    false
                                )
                            }.addOnFailureListener { err ->
                                showToast(


                                    requireActivity(),
                                    "Failed to Upload" + err.message.toString()
                                )
                                if (binding.progressBar.isShown)
                                    binding.progressBar.visibility(
                                    false
                                )
                            }
                    }
                }
            }.addOnProgressListener { uploadTask ->
                val uploadPercent =
                    (100 * uploadTask.bytesTransferred) / uploadTask.totalByteCount
                binding.progressBar.progress = uploadPercent.toInt()
                if (!binding.progressBar.isShown) binding.progressBar.visibility(true)
            }.addOnFailureListener { err ->
                showToast(
                    requireActivity(),
                    "Failed to Upload" + err.message.toString()
                )
                if (binding.progressBar.isShown)
                    binding.progressBar.visibility(false)
            }
        }
    }


    private fun upLoadWordFileToFirebase() {
        val fileNameWord = binding.titleWordFile.text.toString()
        val my_referenceWord =
            storageReferenceWord.child("${System.currentTimeMillis()}/$fileNameWord")
        wordFileUri?.let { uri ->
            my_referenceWord.putFile(uri).addOnSuccessListener {
                my_referenceWord.downloadUrl.addOnSuccessListener { downloadUriWord ->
                    val wordModel = WordModel(
                        null,
                        fileNameWord,
                        downloadUriWord.toString(),
                        current_user!!.uid,
                        0
                    )
                    // Generate a unique key for the new WordModel
                    val pushKey = databaseReferenceWord.push().key
                    if (pushKey != null) {
                        // Set the value at the unique key
                        databaseReferenceWord.child(pushKey).setValue(wordModel)
                            .addOnSuccessListener {
                                wordFileUri = null
                                binding.titleWordFile.text =
                                    resources.getString(R.string.word_selected)
                                showToast(requireActivity(), "WORD file uploaded successfully")
                                if (binding.progressBar.isShown)
                                    binding.progressBar.visibility(false)
                            }.addOnFailureListener { err ->
                                showToast(
                                    requireActivity(),
                                    "Failed to Upload WORD file" + err.message.toString()
                                ); if (binding.progressBar.isShown)
                                binding.progressBar.visibility(false)
                            }
                    } else {
                        showToast(
                            requireContext(),
                            "Failed to generate database key"
                        )
                        if (binding.progressBar.isShown)
                            binding.progressBar.visibility(false)
                    }
                }
            }.addOnProgressListener { uploadTask ->
                val uploadPercent =
                    (100 * uploadTask.bytesTransferred) / uploadTask.totalByteCount
                binding.progressBar.progress = uploadPercent.toInt()
                if (!binding.progressBar.isShown) binding.progressBar.visibility(true)

            }.addOnFailureListener { err ->
                showToast(
                    requireActivity(),
                    "Failed to Upload" + err.message.toString()
                )
                if (binding.progressBar.isShown)
                    binding.progressBar.visibility(false)
            }
        }
    }

    private fun handlePdfFileSelection(uri: Uri?) {
        if (uri != null) {
            val docName = DocumentFile.fromSingleUri(
                requireActivity(),
                uri
            )?.name
            binding.titlePdfFile.text = docName.toString()
        } else {
            binding.titlePdfFile.text = "Unknown PDF file"
        }
    }

    private fun handleWordFileSelection(uri: Uri?) {
        if (uri != null) {
            val docName = DocumentFile.fromSingleUri(
                requireActivity(),
                uri
            )?.name
            binding.titleWordFile.text = docName.toString()
        } else {
            binding.titleWordFile.text = "Unknown Word file"
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }


}