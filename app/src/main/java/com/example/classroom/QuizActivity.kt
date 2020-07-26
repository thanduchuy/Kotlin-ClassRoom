package com.example.classroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classroom.Model.ClassRoom
import com.example.classroom.Model.Supplier
import com.example.classroom.adapters.QuizAdapter
import com.example.classroom.models.Package
import com.example.classroom.models.PointUser
import com.example.classroom.models.Quiz
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {
    lateinit var idRoom:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        setAnimation()

        setEvent()
        val bundle: Bundle? = intent.extras
        bundle?.let {
            idRoom = bundle.get("id").toString()
        }

        getDataQuiz()
        Supplier.isTeacher = uidTeacher(idRoom)

    }
    fun setAnimation() {
    }
    fun getDataQuiz() {
        val ref = FirebaseDatabase.getInstance().getReference("quizs")
        ref.orderByChild("idRoom").equalTo(idRoom).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()) {
                    var temp = arrayListOf<Quiz>()
                    for (h in p0.children) {
                        val quiz = h.getValue(Quiz::class.java)
                        temp.add(quiz!!)
                    }
                    setupAdapter(temp)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    fun setEvent() {
        btn_continue.alpha = 1.toFloat()
        btn_cancel.alpha = 1.toFloat()

        btn_continue.setOnClickListener {
            if(isDone(Package.clickPackage.students)) {
                Alerter.create(this)
                    .setText("Bài tập này bạn đã làm rồi")
                    .setBackgroundColorRes(R.color.error) // or setBackgroundColorInt(Color.CYAN)
                    .show()
            } else {
                val intent = Intent(this, Quiz_DetailActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fleft, R.anim.fhelper)
            }
        }
        btn_cancel.setOnClickListener {
            val intent = Intent( this, ClassActivity::class.java)
            intent.putExtra("id",idRoom)
            startActivity(intent)
            overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
    }
    private fun setupAdapter(arr:ArrayList<Quiz>) {
        val layoutManager1 = LinearLayoutManager(this)
        layoutManager1.orientation = LinearLayoutManager.VERTICAL
        recycle.layoutManager = layoutManager1
        val adapter1 = QuizAdapter(this, arr)
        adapter1.notifyDataSetChanged()

        recycle.adapter = adapter1
    }
    private fun isDone(arr:ArrayList<PointUser>):Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        for (h in arr) {
            if(h.uid==user?.uid.toString()) {
                return true
            }
        }
        return false
    }
    private fun uidTeacher(text:String):Boolean {
        lateinit var room: ClassRoom
        val user = FirebaseAuth.getInstance().currentUser
        for(item in Supplier.listRoom) {
            if(text==item.id) {
                room = item
            }
        }
        return room.uidTeacher == user?.uid.toString()
    }
}
