package com.example.classroom.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classroom.DetailHomeWork
import com.example.classroom.R
import com.example.classroom.RankActivity
import com.example.classroom.models.Exercise
import kotlinx.android.synthetic.main.item_homework.view.*
import java.text.SimpleDateFormat

class HomeWorkAdapter(val context: Context, var rooms: ArrayList<Exercise>,var isTeacher: Boolean) :
    RecyclerView.Adapter<HomeWorkAdapter.myViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(com.example.classroom.R.layout.item_homework, parent, false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val hobby = rooms[position]
        holder.setData(hobby, position)

        holder.itemView.setOnClickListener {
            val activity = context as Activity
            val intent = Intent( activity, DetailHomeWork::class.java)
            intent.putExtra("idPost",hobby.idPost)
            intent.putExtra("idRoom",hobby.idRoom)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
        holder.itemView.alpha = 0.toFloat()
        holder.itemView.translationY = 200.toFloat()

        holder.itemView.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
            .setStartDelay((position*100).toLong()).start()
        holder.itemView.goRank.setOnClickListener {
            val activity = context as Activity
            val intent = Intent( activity, RankActivity::class.java)
            intent.putExtra("idPost",hobby.idPost)
            intent.putExtra("idRoom",hobby.idRoom)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
    }

    inner class myViewHolder(myView: View) : RecyclerView.ViewHolder(myView) {
        var currentHobby: Exercise? = null
        var currentPosition: Int = 0

        init {
        }

        fun setData(rooms: Exercise?, position: Int) {
            val pattern = "dd/MM"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val date: String = simpleDateFormat.format(rooms!!.dateEnd)
            itemView.textView2.text = rooms!!.name
            itemView.textView3.text = "Hết hạn vào :"+date
            this.currentHobby = rooms
            this.currentPosition = position
            itemView.goRank.visibility = View.VISIBLE
        }
    }
}