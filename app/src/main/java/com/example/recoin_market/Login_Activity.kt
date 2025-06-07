package com.example.recoin_market

import android.content.Intent
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recoin_market.databinding.ActivityLoginBinding
import com.example.recoin_market.databinding.ActivityRegBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Login_Activity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser // currentUser это пользователь который находится в firebase зарегна он или нет

        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java)) // inten закрывает одно закрыть и окрыть
            finish() }
        binding.registerBtn.setOnClickListener(View.OnClickListener{ v1 : View? ->
            val intent = Intent(applicationContext, Reg_Activity::class.java)
            startActivity(intent)
            finish()

        })

        binding.logginBtn.setOnClickListener({v2: View ->
            binding.progressBar.visibility(true)

            mAuth.signInWithEmailAndPassword(
                binding.emailLogin.text.toString(),
                binding.passwordLogin.text.toString() )
                .addOnCompleteListener(object :OnCompleteListener<AuthResult>{
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful) {
                    binding.progressBar.visibility(false)
                            showToast(applicationContext, "Log in is successful")
                            var intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else {
                            showToast(applicationContext, "Authentication failed!")
                            binding.progressBar.visibility(false)
                        }

                    }
                })



        })








    }
}