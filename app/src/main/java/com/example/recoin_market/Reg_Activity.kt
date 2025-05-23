package com.example.recoin_market

import android.content.ContentResolver
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recoin_market.databinding.ActivityRegBinding
import com.example.recoin_market.models.UserModels
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.Serializable
import java.util.UUID

class Reg_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRegBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var new_user_model: UserModels
    private lateinit var contentResolver: ContentResolver // отдает contentresolver ложит в newImage
    private lateinit var photoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest> // сожание фото что я лично фоткал

    var newImage:ByteArray? = null // есть ли фотка ? цифровые без lateinit var

    private lateinit var storeUserPerson:StorageReference // склад данных
    private lateinit var databaseUserPersons:DatabaseReference // сама база данных основная база





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRegBinding.inflate(layoutInflater)
        setContentView(binding.root)


        storeUserPerson = FirebaseStorage.getInstance().reference.child("user_persons") // reference проходит по ячейкам child
        databaseUserPersons = FirebaseDatabase.getInstance().reference.child("user_persons")
        firebaseAuth = FirebaseAuth.getInstance()
        contentResolver = this.applicationContext.contentResolver // this.applicationContext дает админку ко всем фрагментом contentResolver


        photoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
                uri?.let {
                    try {
                        newImage = contentResolver.openInputStream(uri)?.readBytes().let {
                            UUID.randomUUID()
                            it
                        }
                        newImage?.let {
                            showToast(this,"Фото загружено")
                        }

                    }catch (e: Exception){
                        showToast(this, "Не удалось загрузить фото")
                        e.printStackTrace()
                    }
                }
            }

        binding.btnAddAvatar.setOnClickListener {v1 ->
            photoPickerLauncher?.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly) 
            )
        }

        binding.register.setOnClickListener{v2 ->
            new_user_model = UserModels(
                binding.registerNicnameEdit.text.toString(),
                binding.registerEmail.text.toString(),
                binding.confirmPasswordReg.text.toString(),
                binding.passwordRegister.text.toString(),
                "",0,0,0,""
            )

            if (new_user_model.email.isNotEmpty() && new_user_model.password.isNotEmpty()
                && new_user_model.confirmPassword.isNotEmpty()
                && new_user_model.nickname.isNotEmpty()) {

                // Создаем уникальное имя - место для хранения для нового юзера
                // в базе данных изображений - StorageFirebase
                 val fileName = new_user_model.nickname
                val avatarRef =
                    storeUserPerson.child("${System.currentTimeMillis()}/${fileName}.png") // $ используется чтобы связать их и использовать filename
                //загружаем аватар нового юзера в Firebase Storage

                val uploadTask:UploadTask = avatarRef.putBytes(newImage as ByteArray)
                uploadTask.addOnSuccessListener {
                    // Получаем avatar URL нового юзера
                    avatarRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val avatarUrl = downloadUri.toString()
                        //фиксируем avatar URL нового юзера
                        val userWithAvatarUrl = new_user_model.copy(avatar = avatarUrl)
                        if (new_user_model.password == new_user_model.confirmPassword) {
                            firebaseAuth.createUserWithEmailAndPassword(
                                new_user_model.email,
                                new_user_model.password
                            ).addOnCompleteListener { authResult ->
                                if (authResult.isSuccessful){
                                    //получаем UID нового юзера
                                    val userUid = authResult.result.user?.uid
                                    if (userUid != null) {
                                        //Сохраняем нового юзера в базе данных
                                        // в Realtime Database (вместе с URL-адресом его аватара)
                                        databaseUserPersons.child(userUid)
                                            .setValue(userWithAvatarUrl)
                                            .addOnSuccessListener {
                                                showToast(
                                                    this.applicationContext,
                                                    "Новый пользовательй зарегистрирован")
                                            }.addOnFailureListener{error ->
                                                showToast(this.applicationContext,
                                                "ошибка пользователя" + error.message)

                                            }
                                    }

                                    val bundle = Bundle()
                                    bundle.putSerializable(
                                        "cur_user",
                                        userWithAvatarUrl as Serializable)
                                    val intent = Intent (this, Login_Activity::class.java)
                                    startActivity(intent,bundle)

                                }else {
                                    Log.e("Error", authResult.exception.toString())
                                    showToast(this,"Пользователь не зарегистрирован"
                                    + authResult.exception.toString())
                                }

                            }
                        }else {
                            showToast(this,"пароль не совпадает")

                        }
                    }

                }.addOnProgressListener {uploadTask->
                    val uploadPercent = (100 * uploadTask.bytesTransferred) / uploadTask.totalByteCount
                    binding.progressBar.progress = uploadPercent.toInt()
                    if (!binding.progressBar.isShown)
                        binding.progressBar.visibility = View.VISIBLE
                }
            }else {
                showToast(this,"Заполните все поля")
            }

        }





    }
}