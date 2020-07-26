package com.example.classroom.adapters

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.classroom.models.Homework
import kotlinx.android.synthetic.main.item_rank.view.*
import java.text.SimpleDateFormat

class RankAdapter(val context: Context, var rooms: ArrayList<Homework>) :
    RecyclerView.Adapter<RankAdapter.myViewHolder>() {
    var myDowloadid : Long = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(com.example.classroom.R.layout.item_rank, parent, false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val hobby = rooms[position]
        holder.setData(hobby, position)

        holder.itemView.alpha = 0.toFloat()
        holder.itemView.translationY = 200.toFloat()

        holder.itemView.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
            .setStartDelay((position*100).toLong()).start()
        holder.itemView.dowload.setOnClickListener {
            var request:DownloadManager.Request = DownloadManager.Request(Uri.parse(hobby.file))
                .setTitle("exercire of "+hobby.name)
                .setDescription("exercire of "+hobby.name)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setAllowedOverMetered(true)
            var dm : DownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            myDowloadid = dm.enqueue(request)
        }
        var br = object :BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var id :Long? = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1)
                if(id == myDowloadid) {
                    Toast.makeText(context,"Dowload complete!!!",Toast.LENGTH_LONG).show()
                }
            }
        }
        context.registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    inner class myViewHolder(myView: View) : RecyclerView.ViewHolder(myView) {
        var currentHobby: Homework? = null
        var currentPosition: Int = 0

        init {
        }

        fun setData(rooms: Homework?, position: Int) {
            val pattern = "dd/MM"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val date: String = simpleDateFormat.format(rooms!!.date)
            itemView.datePost.text = "Hết hạn vào :"+date
            itemView.namePost.text = rooms!!.name
            Glide.with(context).load(rooms!!.avatar).into(itemView.avatar)
            this.currentHobby = rooms
            this.currentPosition = position

        }
    }
}