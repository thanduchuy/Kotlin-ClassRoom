package com.example.classroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classroom.adapters.RankAdapter
import com.example.classroom.models.Homework
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_rank.*
import kotlinx.android.synthetic.main.item_post.*

class RankActivity : AppCompatActivity() {
    var idRoom = ""
    var idPost = ""
    var list = ArrayList<Homework>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)
        val bundle: Bundle? = intent.extras
        bundle?.let {
            idRoom = bundle.getString("idRoom").toString()
            idPost = bundle.getString("idPost").toString()
        }
        getDataHomeWork()
    }
    fun getDataHomeWork() {
        val ref = FirebaseDatabase.getInstance().getReference("homeworks/"+idRoom+"/"+idPost+"/collect")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()) {
                    list.clear()
                    for (h in p0.children) {
                        val room = h.getValue(Homework::class.java)
                        list.add(room!!)
                    }
                    setupAdapter(list)
                }
            }

            override fun onCancelled(p0: DatabaseError){
            }
        })
    }
    private fun setupAdapter(arr:ArrayList<Homework>) {
        val layoutManager1 = LinearLayoutManager(this)
        layoutManager1.orientation = LinearLayoutManager.VERTICAL
        rankRCW.layoutManager = layoutManager1
        val adapter1 = RankAdapter(this, arr)
        adapter1.notifyDataSetChanged()

        rankRCW.adapter = adapter1
    }
}
