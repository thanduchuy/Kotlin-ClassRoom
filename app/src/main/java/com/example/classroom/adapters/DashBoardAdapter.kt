package com.example.classroom.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.classroom.models.PointUser
import kotlinx.android.synthetic.main.item_dashboard.view.*

class DashBoardAdapter(val context: Context, var users: ArrayList<PointUser>) :
    RecyclerView.Adapter<DashBoardAdapter.myViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(com.example.classroom.R.layout.item_dashboard, parent, false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val hobby = users[position]
        holder.setData(hobby!!, position)
        holder.itemView.alpha = 0.toFloat()
        holder.itemView.translationY = 200.toFloat()

        holder.itemView.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
            .setStartDelay((position*100).toLong()).start()
    }

    inner class myViewHolder(myView: View) : RecyclerView.ViewHolder(myView) {
        var currentHobby: PointUser? = null
        var currentPosition: Int = 0

        init {
        }

        fun setData(dash: PointUser?, position: Int) {
            itemView.nameUser.text = dash?.nameUser.toString()
            itemView.pointUser.text = dash?.point.toString()

            Glide.with(context).load(dash?.avatar.toString()).into(itemView.avatarUser)
        }
    }
}