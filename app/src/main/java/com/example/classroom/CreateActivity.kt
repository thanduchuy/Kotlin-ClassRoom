package com.example.classroom

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.classroom.Model.ClassRoom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_create.*

class CreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        setFont()

        setAnimation()

        setEvent()
    }

    fun setFont() {
        val mlight: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratLight.ttf")
        val mmedium: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratMedium.ttf")
        val abri: Typeface = Typeface.createFromAsset(assets, "fonts/AbrilFat.ttf")

        titleCreate.typeface = abri
        subtitleCreate.typeface = mlight
        btnCreate.typeface = mmedium
        titleCreate2.typeface = abri
        btnStarted.typeface = mlight

        nameClass.typeface = mlight
        roomClass.typeface = mlight
        titleClass.typeface = mlight
        subClass.typeface = mlight
    }

    fun setAnimation() {
        titleCreate.alpha = 0.toFloat()
        titleCreate.translationY = 200.toFloat()

        subtitleCreate.alpha = 0.toFloat()
        subtitleCreate.translationY = 200.toFloat()

        btnCreate.alpha = 0.toFloat()
        btnCreate.translationY = 200.toFloat()


        titleCreate.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
            .setStartDelay(300).start()
        subtitleCreate.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
            .setStartDelay(600).start()
        btnCreate.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
            .setStartDelay(900).start()
    }

    fun setEvent() {
        titleCreate2.alpha = 0.toFloat()

        nameClass.alpha = 0.toFloat()
        nameClass.translationY = 200.toFloat()

        titleClass.alpha = 0.toFloat()
        titleClass.translationY = 200.toFloat()

        subClass.alpha = 0.toFloat()
        subClass.translationY = 200.toFloat()

        roomClass.alpha = 0.toFloat()
        roomClass.translationY = 200.toFloat()

        btnStarted.alpha = 0.toFloat()
        btnStarted.translationY = 200.toFloat()

        btnCreate.setOnClickListener {
            titleCreate2.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
                .setStartDelay(300).start()

            nameClass.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
                .setStartDelay(600).start()
            roomClass.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
                .setStartDelay(800).start()
            titleClass.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
                .setStartDelay(1000).start()
            subClass.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
                .setStartDelay(1200).start()

            btnStarted.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
                .setStartDelay(1400).start()

            titleCreate.animate().alpha(0.toFloat()).translationY(-200.toFloat()).setDuration(800)
                .start()
            subtitleCreate.animate().alpha(0.toFloat()).translationY(-200.toFloat())
                .setDuration(800).start()
            btnCreate.animate().alpha(0.toFloat()).translationY(-200.toFloat()).setDuration(800)
                .start()
        }

        btnStarted.setOnClickListener {
            saveClassRoom()
        }
    }
    fun saveClassRoom() {
        val name = nameClass.text.toString().trim()
        val sub = subClass.text.toString().trim()
        val title = titleClass.text.toString().trim()
        val room = roomClass.text.toString().trim()
        val user = FirebaseAuth.getInstance().currentUser
        if(name.isEmpty()) {
            nameClass.error = "Vui lòng nhập tên lớp"
            return
        }
        if(sub.isEmpty()) {
            subClass.error = "Vui lòng nhập mô tả"
            return
        }
        if(title.isEmpty()) {
            titleClass.error = "Vui lòng nhập chủ đề"
            return
        }
        if(room.isEmpty()) {
            roomClass.error = "Vui lòng nhập phòng học"
            return
        }

        val temp = ArrayList<String>()
        temp.add(user!!.uid.toString())
        val ref = FirebaseDatabase.getInstance().getReference("rooms")
        val RoomID = ref.push().key.toString()
        val Room = ClassRoom(RoomID,name,sub,title,room,getRandomString(5),temp,user!!.uid)
        ref.child(RoomID).setValue(Room).addOnCompleteListener {
            val intent = Intent(this, SubjectActivity::class.java)
            intent.putExtra("alert", "show")
            startActivity(intent)
            overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
    }
    fun getRandomString(length: Int) : String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}
