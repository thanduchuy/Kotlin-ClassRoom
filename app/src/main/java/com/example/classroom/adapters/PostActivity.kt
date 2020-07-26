package com.example.classroom.adapters

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.classroom.ClassActivity
import com.example.classroom.R
import com.example.classroom.SubjectActivity
import com.example.classroom.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_post.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class PostActivity : AppCompatActivity() {
    lateinit var idRoom :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        setFontFamily()
        setAnimation()
        setEvent()
        val bundle: Bundle? = intent.extras
        bundle?.let {
            idRoom = bundle.get("id").toString()
        }
    }
    fun setFontFamily() {
        val logox: Typeface = Typeface.createFromAsset(assets, "fonts/Fredoka.ttf")
        val mmedium: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratMedium.ttf")
        val mregular: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratRegular.ttf")

        textView.typeface = logox
        titlePost.typeface = mmedium
        contentPost.typeface = mregular
    }
    fun setAnimation() {
        val traX: Float = 400.toFloat()


        textView.translationX = traX
        titlePost.translationX = traX
        contentPost.translationX = traX
        btnPost.alpha = 0.toFloat()
        btnPost.translationY = 200.toFloat()
        btnCancle.alpha = 0.toFloat()
        btnCancle.translationY = 200.toFloat()

        textView.alpha = 0.toFloat()
        titlePost.alpha = 0.toFloat()
        contentPost.alpha = 0.toFloat()

        textView.animate().translationX(0.toFloat()).alpha(1.toFloat()).setDuration(800)
            .setStartDelay(500).start()
        titlePost.animate().translationX(0.toFloat()).alpha(1.toFloat()).setDuration(800)
            .setStartDelay(700).start()
        contentPost.animate().translationX(0.toFloat()).alpha(1.toFloat()).setDuration(800)
            .setStartDelay(1000).start()
        btnPost.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
            .setStartDelay(1500).start()
        btnCancle.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
            .setStartDelay(1800).start()
    }
    fun setEvent() {
        btnPost.setOnClickListener {
            val dbPost = FirebaseDatabase.getInstance().getReference("posts")
            val dateFormatter: DateFormat = SimpleDateFormat("dd/MM/yyy")
            val user = FirebaseAuth.getInstance().currentUser
            dateFormatter.setLenient(false)
            val today = Date()

            val s: String = dateFormatter.format(today)
            val title = titlePost.text.toString()
            val content = contentPost.text.toString()

            val PostID = dbPost.push().key.toString()
            val post = Post(title,content,s,idRoom,PostID,user?.uid.toString())
            dbPost.child(PostID).setValue(post).addOnCompleteListener {
                val intent = Intent(this, ClassActivity::class.java)
                intent.putExtra("id", idRoom)
                startActivity(intent)
                overridePendingTransition(R.anim.fleft, R.anim.fhelper)
            }
        }
        btnCancle.setOnClickListener {
            val intent = Intent( this, ClassActivity::class.java)
            intent.putExtra("id",idRoom)
            startActivity(intent)
            overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
    }
}
