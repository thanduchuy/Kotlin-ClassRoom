package com.example.classroom.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classroom.models.Message
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatAdapter(val context: Context, var chats: ArrayList<Message>) :
    RecyclerView.Adapter<ChatAdapter.myViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(com.example.classroom.R.layout.item_chat, parent, false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val hobby = chats[position]
        val traX: Float = 400.toFloat()
        holder.setData(hobby, position)
        holder.itemView.alpha = 0.toFloat()
        holder.itemView.translationX = traX
        holder.itemView.animate().translationX(0.toFloat()).alpha(1.toFloat()).setDuration(800)
            .setStartDelay((position*50).toLong()).start()
    }

    inner class myViewHolder(myView: View) : RecyclerView.ViewHolder(myView) {
        var currentHobby: Message? = null
        var currentPosition: Int = 0

        init {
        }

        fun setData(msg: Message?, position: Int) {
            this.currentHobby = msg
            this.currentPosition = position

            val user = FirebaseAuth.getInstance().currentUser
            if(user?.uid.toString()==msg?.uid.toString()) {
                itemView.msgLeft.setVisibility(View.GONE)
                itemView.msgRight.text = msg?.message.toString()
            }else {
                itemView.msgRight.setVisibility(View.GONE)
                itemView.bgEnemy.visibility=View.VISIBLE
                itemView.nameEnemy.text = user?.displayName
                itemView.msgLeft.text = msg?.message.toString()
            }
        }
    }

}
