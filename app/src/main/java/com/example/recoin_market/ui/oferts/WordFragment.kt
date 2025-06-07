package com.example.recoin_market.ui.oferts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.recoin_market.R
import com.example.recoin_market.databinding.FragmentWordBinding
import com.example.recoin_market.models.UserModels
import com.example.recoin_market.models.WordModel
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


class WordFragment : Fragment() {


    private var _binding: FragmentWordBinding? = null
    private val binding get() = _binding!!
    private lateinit var wordModel: WordModel
    private lateinit var title_doc: String
    private var total_outPage_counter: String? = null
    private lateinit var id_user_w: String
    var mAuth: FirebaseAuth? = null
    var current_user: FirebaseUser? = null
    val databaseReferenceWord = FirebaseDatabase.getInstance().reference.child("words")
    val databaseReferenceUsers: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("user_persons")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        current_user = mAuth!!.currentUser
        arguments?.let {
            wordModel =
                it.getSerializable("key_wordUrl") as WordModel
            //Retrieve the Firebase key from the arguments and set it to WordModel
            val firebaseKey = it.getString("firebase_key_word")
            wordModel.id = firebaseKey
            id_user_w = wordModel.idUser
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val wordDownloadUrl = wordModel.downloadWordUrl
        title_doc = wordModel.fileWordTitle
        binding.titleOfDocOnWordPage.text = title_doc
        downloadModel(wordDownloadUrl)
        setUpOnBackPressed()

        return root
    }

    fun downloadModel(wordUrl: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(wordUrl).build()
                val responce = okHttpClient.newCall(request).execute()
                //Проверяем есть ли Word-документ в базе данных
                if (responce.isSuccessful) {
                    responce.body?.let { responseBody ->
                        val file = File(
                            requireContext().filesDir,
                            "${wordModel.fileWordTitle}.docx" //Формируем имя файла для Word-документа
                        )//Создание файла в памяти телефона
                        responseBody.byteStream().use { inputStream ->
                            file.outputStream().use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                        // Ворд документ загружен успешно, теперь его можно открыть в главном потоке -Dispatchers.Main:                    withContext(Dispatchers.Main) {
                        loadWordFromFile(file)


                    } ?: withContext(Dispatchers.Main) {
                        showToast(
                            requireContext(),
                            "Failed to download Word document: Empty response body"
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast(
                            requireContext(),
                            "No Response from server," + "   Failed to download Word document: ${responce.code}"
                        )
                    }

                }
            } catch (e: Exception) {


                withContext(Dispatchers.Main) {
                    Log.e("Word_Fragment", "Download error: ${e.message}")
                    showToast(requireContext(), "Download error: ${e.message}")
                }
            }
        }
    }


    private fun loadWordFromFile(file: File) {    // Создает объект FileProvider для предоставления URI файла (адреса документа)
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider", //  пакетный идентификатор - знать где находится ваш файл в тел
            file
        )
        // Формирует интент для открытия  MIME-type object файла  для выбора приложения для открытия
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(
            uri,
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        )    //Проверяем есть ли разрешение от системы телефона для открытия файла
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        // Проверяем есть ли приложения, способные обработать этот Intent
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            showToast(requireContext(), "No app found to open Word documents")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.giveScoresBtn.setOnClickListener { v ->
            total_outPage_counter = binding.likesCounter.text.toString()
            changeScore(wordModel)
        }
    }

    private fun changeScore(wordModel: WordModel) {
        //Существует ли документ:
        if (wordModel.id != null) {
            //Чтобы пользователь не мог ставить очки  самому себе:
            if (wordModel.idUser != current_user!!.uid) {
                // Если пользователь ни разу не получал очки за текст:
                if (wordModel.score == 0) {
                    val updatesScoreWORD =
                        hashMapOf<String, Any>(
                            "score" to Integer.parseInt(
                                total_outPage_counter
                            )
                        )
                    //Обновляем базу данных с новыми оценками за тексты
                    databaseReferenceWord.child(wordModel.id.toString())
                        .updateChildren(updatesScoreWORD).addOnSuccessListener {
                            showToast(
                                requireContext(),
                                "Score uploaded successfully"
                            )
                            binding.textAboutScore.text = total_outPage_counter.toString()
                        }.addOnFailureListener { error ->
                            showToast(
                                requireContext(),
                                "Failed to update score: ${error.message}"
                            )
                            binding.textAboutScore.text = total_outPage_counter.toString()
                        }
                } else { //Если пользователь уже получал очки за текст:
                    val updatesScoreWORDAgain = hashMapOf<String, Any>(
                        "score" to wordModel.score + Integer.parseInt(total_outPage_counter)
                    )
                    //Обновляем базу данных с новыми оценками за тексты
                    databaseReferenceWord.child(wordModel.id.toString())
                        .updateChildren(updatesScoreWORDAgain)
                }

//Проверяем есть ли у пользователя монеты в базе данных
                databaseReferenceUsers.child(id_user_w).get().addOnSuccessListener {

                    val userModelCoins = it.getValue(UserModels::class.java)!!.coins
                    if (userModelCoins == 0) {
                        val updatesCoins =
                            hashMapOf<String, Any>(
                                "coins" to Integer.parseInt(
                                    total_outPage_counter
                                )
                            )
                        databaseReferenceUsers.child(id_user_w).updateChildren(updatesCoins)
                    } else {
                        val updatesCoinsAgain = hashMapOf<String, Any>(
                            "coins" to (userModelCoins + Integer.parseInt(
                                total_outPage_counter
                            ))
                        ) //Обновляем базу данных с новыми оценками- монетами  за тексты
                        databaseReferenceUsers.child(id_user_w)
                            .updateChildren(updatesCoinsAgain)
                    }
                }
            } else {
                showToast(requireContext(), "You can't give scores to your own documents")
            }
        } else {
            showToast(
                requireContext(),
                "Failed to get this Word-document, please, try again later"
            )
        }
    }

    fun setUpOnBackPressed(){
        requireActivity().onBackPressedDispatcher
            .addCallback(object: OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    if (isEnabled){


                        requireActivity().findNavController(R.id.cabinetFragment)

                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

