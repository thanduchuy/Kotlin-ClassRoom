package com.example.classroom.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.classroom.ChatActivity
import com.example.classroom.R
import com.example.classroom.models.Message
import com.example.classroom.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.editpost.view.*
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter(val context: Context, var posts: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.myViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(com.example.classroom.R.layout.item_post, parent, false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val hobby = posts[position]
        val traX: Float = 400.toFloat()
        val user = FirebaseAuth.getInstance().currentUser
        holder.setData(hobby, position)
        holder.itemView.goChat.setOnClickListener {
            val activity = context as Activity
            val intent = Intent( activity, ChatActivity::class.java)
            intent.putExtra("post",posts[position].idPost+"*"+posts[position].titlePost+"*"+posts[position].idRoom)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
        if(posts[position].uid==user?.uid.toString()) {
            holder.itemView.btnEdit.visibility = View.VISIBLE
        }

        holder.itemView.alpha = 0.toFloat()
        holder.itemView.translationX = traX
        holder.itemView.animate().translationX(0.toFloat()).alpha(1.toFloat()).setDuration(800)
            .setStartDelay((position*200).toLong()).start()
    }

    inner class myViewHolder(myView: View) : RecyclerView.ViewHolder(myView) {
        private var currentHobby: Post? = null
        var currentPosition: Int = 0

        init {
            itemView.btnEdit.setOnClickListener {
                updateData()
            }
        }

        fun setData(post: Post?, position: Int) {
            this.currentHobby = post
            this.currentPosition = position

            itemView.titlePost.text = post?.titlePost.toString()
            itemView.contentPost.text = post?.contentPost.toString()
            itemView.datePost.text = post?.datePost.toString()

        }
        fun updateData() {
            val mDialogView = LayoutInflater.from(context)
                .inflate(com.example.classroom.R.layout.editpost, null)
            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.dialogNameEt.text =
                Editable.Factory.getInstance().newEditable(currentHobby!!.titlePost)
            mDialogView.dialogContentEt.text =
                Editable.Factory.getInstance().newEditable(currentHobby!!.contentPost)
            mDialogView.dialogUpdateBtn.setOnClickListener {
                val title = mDialogView.dialogNameEt.text.toString()
                val content = mDialogView.dialogContentEt.text.toString()
                val db = FirebaseDatabase.getInstance().getReference("posts")
                currentHobby!!.titlePost = title
                currentHobby!!.contentPost = content
                db.child(currentHobby!!.idPost).setValue(currentHobby)
                mAlertDialog.dismiss()
            }
            mDialogView.dialogRemoveBtn.setOnClickListener {
                val db = FirebaseDatabase.getInstance().getReference("posts")
                db.child(currentHobby!!.idPost).removeValue()
                mAlertDialog.dismiss()
            }
            mDialogView.dialogCancelBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }
}