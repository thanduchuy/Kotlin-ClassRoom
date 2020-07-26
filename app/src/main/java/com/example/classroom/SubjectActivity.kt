package com.example.classroom

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classroom.Model.ClassRoom
import com.example.classroom.Model.Supplier
import com.example.classroom.adapters.ListAdapter
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_subject.*

class SubjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)

        setFont()

        setAnimation()

        setEvent()

        getParameters()

        getDataRooms()
        btnGoBack.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnCompleteListener{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fleft,R.anim.fhelper)
            }.addOnFailureListener{
                        e->Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
                }
        }

    }
    fun getDataRooms() {
        val user = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().getReference("rooms")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()) {
                    Supplier.listRoom.clear()
                    var temp = arrayListOf<ClassRoom>()
                    for (h in p0.children) {
                        val room = h.getValue(ClassRoom::class.java)
                        if(room!!.listStudent.contains(user!!.uid.toString())) {
                            temp.add(room!!)
                        }
                        Supplier.listRoom.add(room!!)
                    }
                    setupAdapter(temp)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    private fun setupAdapter(arr:ArrayList<ClassRoom>) {
        val layoutManager1 = LinearLayoutManager(this)
        layoutManager1.orientation = LinearLayoutManager.VERTICAL
        recycle.layoutManager = layoutManager1
        val adapter1 = ListAdapter(this, arr)
        adapter1.notifyDataSetChanged()

        recycle.adapter = adapter1
    }
    fun setEvent() {
        ivIlls.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
        btnBuy.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
    }
    fun getParameters() {
        val bundle: Bundle? = intent.extras
        bundle?.let {
            val text = bundle.getString("alert")
            val result = bundle.getString("login")
            val join  = bundle.getString("join")
            if(text=="show") {
                Alerter.create(this)
                    .setTitle("Tạo lớp học thành công")
                    .setText("Hãy xây dựng lớp học của bạn...")
                    .setBackgroundColorRes(R.color.colorPrimaryDark) // or setBackgroundColorInt(Color.CYAN)
                    .show()
            }
            if(join =="join") {
                Alerter.create(this)
                    .setText("Bạn đã tham gia lớp học thành công...")
                    .setBackgroundColorRes(R.color.colorPrimaryDark) // or setBackgroundColorInt(Color.CYAN)
                    .show()
            }
            if(join == "fail") {
                Alerter.create(this)
                    .setText("Mã lớp học không đúng")
                    .setBackgroundColorRes(R.color.error) // or setBackgroundColorInt(Color.CYAN)
                    .show()
            }
            if(result == "ok") {
                Alerter.create(this)
                    .setText("Bạn đã đăng nhập thành công...")
                    .setBackgroundColorRes(R.color.colorPrimaryDark) // or setBackgroundColorInt(Color.CYAN)
                    .show()
            }
        }
    }
    fun setAnimation() {
        val ivIcon: Animation = AnimationUtils.loadAnimation(this, R.anim.btgicon)
        btnBuy.translationY = 400.toFloat()
        btnBuy.alpha = 0.toFloat()
        ivIlls.startAnimation(ivIcon)
        btnBuy.animate().translationY(0.toFloat()).alpha(1.toFloat()).setDuration(800)
            .setStartDelay(500).start()
    }

    fun setFont() {
        val logox: Typeface = Typeface.createFromAsset(assets, "fonts/Fredoka.ttf")
        val mlight: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratLight.ttf")
        val mmedium: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratMedium.ttf")
        val mregular: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratRegular.ttf")

        btnBuy.typeface = mmedium
        textView.typeface = mlight
    }
}
