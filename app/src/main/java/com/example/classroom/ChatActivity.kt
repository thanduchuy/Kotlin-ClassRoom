package com.example.classroom

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classroom.adapters.ChatAdapter
import com.example.classroom.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chat.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {
    lateinit var yourArray: List<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setEvent()
        val bundle: Bundle? = intent.extras
        bundle?.let {
            val text = bundle.getString("post").toString()
            yourArray = text.split("*")
            toolbar_title.text = yourArray[1].toUpperCase()
        }

        getALlMessage()
    }
    fun getALlMessage() {
        val dbChat = FirebaseDatabase.getInstance().getReference("chats")
        dbChat?.child(yourArray[0])?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // handle error
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    val chats = arrayListOf<Message>()
                    for (productSnapshot in dataSnapshot.children) {
                        val chat = productSnapshot.getValue(Message::class.java)
                        chats.add(chat!!)
                    }
                    setupAdapter(chats)
                }
            }
        })
    }
    private fun setupAdapter(arr:ArrayList<Message>) {
        val layoutManager1 = LinearLayoutManager(this)
        layoutManager1.orientation = LinearLayoutManager.VERTICAL
        recycle.layoutManager = layoutManager1
        val adapter1 = ChatAdapter(this, arr)
        adapter1.notifyDataSetChanged()

        recycle.adapter = adapter1
    }
    fun setEvent() {
        btnsent.setOnClickListener {
            val text = message.text.toString()
            val user = FirebaseAuth.getInstance().currentUser
            val dateFormatter: DateFormat = SimpleDateFormat("dd/MM/yyy")
            dateFormatter.setLenient(false)
            val today = Date()

            val s: String = dateFormatter.format(today)
            val dbChat = FirebaseDatabase.getInstance().getReference("chats")
            dbChat?.child(yourArray[0])?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    // handle error
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()) {
                        val chats = arrayListOf<Message>()
                        for (productSnapshot in dataSnapshot.children) {
                            val chat = productSnapshot.getValue(Message::class.java)
                            chats.add(chat!!)
                        }
                        chats.add(Message(s,text,user?.uid.toString(),yourArray[0]))
                        dbChat.child(yourArray[0]).setValue(chats)
                    } else {
                        val mess=ArrayList<Message>()
                        mess.add(Message(s,text,user?.uid.toString(),yourArray[0]))
                        dbChat.child(yourArray[0]).setValue(mess)
                    }
                }
            })
            message.text.clear()
        }
    }
}
