package com.example.classroom

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {
    lateinit var providers : List<AuthUI.IdpConfig>
    val MY_REQUEST_CODE:Int = 7117
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )

        setEvent()
        showSignInOptions()
    }


    fun setEvent() {
        btn_sign_out.setOnClickListener {
            AuthUI.getInstance().signOut(this@LoginActivity).addOnCompleteListener{
                btn_sign_out.isEnabled = false
                showSignInOptions()
            }
                .addOnFailureListener{
                        e->Toast.makeText(this@LoginActivity,e.message,Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                val intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("login","ok")
                startActivity(intent)
                overridePendingTransition(R.anim.fleft, R.anim.fhelper)
                Toast.makeText(this,""+user!!.email,Toast.LENGTH_SHORT).show()
                btn_sign_out.isEnabled = true
            } else {
                Toast.makeText(this,""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder().
                setAvailableProviders(providers).
                setTheme(R.style.myTheme).build(),MY_REQUEST_CODE)
    }
}
