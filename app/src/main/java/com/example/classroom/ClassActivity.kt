package com.example.classroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.classroom.Model.ClassRoom
import com.example.classroom.Model.Supplier
import com.example.classroom.adapters.PostActivity
import com.example.classroom.adapters.PostAdapter
import com.example.classroom.models.Post
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_class.*
import kotlin.math.log

class ClassActivity : AppCompatActivity() {
    lateinit var room: ClassRoom
    lateinit var idRoom :String
    var isTeacher : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class)

        setEvent()
        val bundle: Bundle? = intent.extras
        bundle?.let {
            idRoom = bundle.get("id").toString()
            val user = FirebaseAuth.getInstance().currentUser
            val dbRoom = FirebaseDatabase.getInstance().getReference("rooms")
            dbRoom.orderByChild("id").equalTo(idRoom).addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var map = p0.children.first().value as Map<String,Any>
                    if(map["uidTeacher"].toString().contains(user?.uid.toString())) {
                        classHome.text = "Chức vụ: Giáo viên"
                        classUser.text = "Chức vụ: Giáo viên"
                        createQuiz.visibility = View.VISIBLE
                        createHW.visibility = View.VISIBLE
                        isTeacher = true
                    } else {
                        classHome.text = "Chức vụ: Học sinh"
                        classUser.text = "Chức vụ:  Học sinh"
                        createHW.visibility = View.GONE
                        isTeacher = false
                    }
                    nameClass.text = "Lớp: "+map["nameClass"].toString()
                    nameUser.text = user?.displayName
                    nameHome.text = user?.displayName
                    Glide.with(this@ClassActivity).load(user?.photoUrl).into(btMennu)
                    Glide.with(this@ClassActivity).load(user?.photoUrl).into(avatarUser)
                    Glide.with(this@ClassActivity).load(user?.photoUrl).into(avaShare)
                    getPost()
                }
            })
        }
        btnGoBack.setOnClickListener {
            val intent = Intent(this, SubjectActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
    }
    fun getPost() {
        val dbRoom = FirebaseDatabase.getInstance().getReference("posts")
        dbRoom.orderByChild("idRoom").equalTo(idRoom).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()) {
                    Supplier.listPost.clear()
                    for (h in p0.children) {
                        val post = h.getValue(Post::class.java)
                        Supplier.listPost.add(post!!)
                    }
                    setupAdapter(Supplier.listPost)
                }
            }
        })
    }
    private fun setupAdapter(arr:ArrayList<Post>) {
        val layoutManager1 = LinearLayoutManager(this)
        layoutManager1.orientation = LinearLayoutManager.VERTICAL
        recycle.layoutManager = layoutManager1
        val adapter1 = PostAdapter(this, arr)
        adapter1.notifyDataSetChanged()
        recycle.adapter = adapter1
    }
    fun setEvent() {
        val bottom: Animation = AnimationUtils.loadAnimation(this, R.anim.frombottom)
        val top: Animation = AnimationUtils.loadAnimation(this, R.anim.fromtop)
        btMennu.setOnClickListener {
            content.animate().translationX(0.toFloat())
            mennu.animate().translationX(0.toFloat())

            avatarUser.startAnimation(top)
            nameUser.startAnimation(top)
            classUser.startAnimation(top)

            lop.startAnimation(bottom)
            exit.startAnimation(bottom)
            quiz.startAnimation(bottom)
            createQuiz.startAnimation(bottom)
            createHW.startAnimation(bottom)
            Homework.startAnimation(bottom)
        }
        createHW.setOnClickListener {
            val intent = Intent(this, CreateHWActivity::class.java)
            intent.putExtra("id",idRoom)
            startActivity(intent)
            overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
        Homework.setOnClickListener {
            val intent = Intent(this, HomeWorks::class.java)
            intent.putExtra("id",idRoom)
            intent.putExtra("uid",isTeacher)
            startActivity(intent)
            overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
        lop.setOnClickListener {
            content.animate().translationX(-800.toFloat())
            mennu.animate().translationX(-1000.toFloat())
        }
        content.setOnClickListener {
            content.animate().translationX(-800.toFloat())
            mennu.animate().translationX(-1000.toFloat())
        }
        share.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("id",idRoom)
            startActivity(intent)
            overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
        createQuiz.setOnClickListener {
            val intent = Intent(this, Quiz_CreateActivity::class.java)
            intent.putExtra("id",idRoom)
            startActivity(intent)
            overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
        quiz.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("id",idRoom)
            startActivity(intent)
            overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
        exit.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnCompleteListener{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fleft,R.anim.fhelper)
            }.addOnFailureListener{

            }
        }

    }
}
