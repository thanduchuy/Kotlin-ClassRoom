package com.example.classroom.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classroom.ClassActivity
import com.example.classroom.Model.ClassRoom
import com.example.classroom.Model.Supplier
import com.example.classroom.R
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*


class ListAdapter(val context: Context, var rooms: ArrayList<ClassRoom>) :
    RecyclerView.Adapter<ListAdapter.myViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(com.example.classroom.R.layout.list_item, parent, false)
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
            val intent = Intent( activity, ClassActivity::class.java)
            intent.putExtra("id",hobby.id)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
        holder.itemView.alpha = 0.toFloat()
        holder.itemView.translationY = 200.toFloat()

        holder.itemView.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
            .setStartDelay((position*100).toLong()).start()
    }

    inner class myViewHolder(myView: View) : RecyclerView.ViewHolder(myView) {
        var currentHobby: ClassRoom? = null
        var currentPosition: Int = 0

        init {
        }

        fun setData(rooms: ClassRoom?, position: Int) {
            itemView.ivItemTitle.text = rooms!!.nameClass
            itemView.ivItemCode.text = "Mã lớp: "+rooms!!.codeClass
            var random = Random()
            itemView.image.setImageResource(Supplier.images[random.nextInt(4 - 0) +  0])
            this.currentHobby = rooms
            this.currentPosition = position

        }
    }
}