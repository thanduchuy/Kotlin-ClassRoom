package com.example.classroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classroom.adapters.HomeWorkAdapter
import com.example.classroom.models.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home_works.*

class HomeWorks : AppCompatActivity() {
    var list = ArrayList<Exercise>()
    var idRoom = ""
    var isTeacher = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_works)
        val bundle: Bundle? = intent.extras
        bundle?.let {
            idRoom = bundle.getString("id").toString()
            isTeacher = bundle.getBoolean("uid")
        }
        getDataExercise()
        btnGoBack.setOnClickListener {
            finish()
        }
    }
    fun getDataExercise() {
        val ref = FirebaseDatabase.getInstance().getReference("exercises")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()) {
                    list.clear()
                    for (h in p0.children) {
                        val room = h.getValue(Exercise::class.java)
                        if(room!!.idRoom.contains(idRoom)) {
                            list.add(room!!)
                        }
                    }
                    setupAdapter(list)
                }
            }

            override fun onCancelled(p0: DatabaseError){
            }
        })
    }
    private fun setupAdapter(arr:ArrayList<Exercise>) {
        val layoutManager1 = LinearLayoutManager(this)
        layoutManager1.orientation = LinearLayoutManager.VERTICAL
        RCHW.layoutManager = layoutManager1
        val adapter1 = HomeWorkAdapter(this, arr,isTeacher)
        adapter1.notifyDataSetChanged()

        RCHW.adapter = adapter1
    }
}
