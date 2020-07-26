package com.example.classroom

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.classroom.Model.ClassRoom
import com.example.classroom.Model.Supplier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        setFont()

        setEvent()
    }
    fun setFont() {
        val mlight: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratLight.ttf")
        val mmedium: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratMedium.ttf")

        titleJoin.typeface=mmedium
        subtitleJoin.typeface = mlight
        inputJoin.typeface=mlight
    }
    fun setEvent() {
        buttonJoin.alpha=0.toFloat()
        val dissape: Animation = AnimationUtils.loadAnimation(this, R.anim.dissape)
        inputJoin.setOnClickListener {

            titleJoin.animate().translationY(30.toFloat()).setDuration(800)
                .setStartDelay(600).start()
            subtitleJoin.animate().translationY(30.toFloat()).setDuration(800)
                .setStartDelay(600).start()
            inputJoin.animate().translationY(30.toFloat()).setDuration(800)
                .setStartDelay(800).start()
            buttonJoin.animate().translationY(30.toFloat()).alpha(1.toFloat()).setDuration(800)
                .setStartDelay(1000).start()
            imageView.startAnimation(dissape)
            imageView.visibility = View.GONE
        }
        buttonJoin.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val text = inputJoin.text.toString().trim()
            lateinit var room:ClassRoom
            for(item in Supplier.listRoom) {
                if(text==item.codeClass) {
                    room = item
                }
            }
            if(room!=null) {
                val dbRoom = FirebaseDatabase.getInstance().getReference("rooms")
                room.listStudent.add(user!!.uid)
                dbRoom.child(room.id).setValue(room)
                val intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("join","join")
                startActivity(intent)
                overridePendingTransition(R.anim.fleft,R.anim.fhelper)
            } else {
                val intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("join","fail")
                startActivity(intent)
                overridePendingTransition(R.anim.fleft,R.anim.fhelper)
            }
        }
    }
}
