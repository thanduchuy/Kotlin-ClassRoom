package com.example.classroom.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.classroom.ClassActivity
import com.example.classroom.DashBoardActivity
import com.example.classroom.Model.Supplier
import com.example.classroom.R
import com.example.classroom.models.Package
import com.example.classroom.models.PointUser
import com.example.classroom.models.Quiz
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_quiz.view.*


class QuizAdapter(val context: Context, var quizs: ArrayList<Quiz>) :
    RecyclerView.Adapter<QuizAdapter.myViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(com.example.classroom.R.layout.item_quiz, parent, false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return quizs.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val hobby = quizs[position]
        holder.setData(hobby, position)

        holder.itemView.alpha = 0.toFloat()
        holder.itemView.translationY = 200.toFloat()
        holder.itemView.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(800)
            .setStartDelay((position*100).toLong()).start()
        holder.itemView.dashboard.setOnClickListener {
            val activity = context as Activity
            Package.clickPackage = quizs[position]!!
            val intent = Intent( context, DashBoardActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
    }

    inner class myViewHolder(myView: View) : RecyclerView.ViewHolder(myView) {
        var currentHobby: Quiz? = null
        var currentPosition: Int = 0
        var active:Boolean = false
        init {
            itemView.setOnClickListener {
                val myObservable = getObservable()


                val myObserver = getObserver()

                myObservable
                    .subscribe(myObserver)
            }
        }

        fun setData(quiz: Quiz?, position: Int) {
            itemView.ivItemTitle.text = quiz!!.nameQuiz.toUpperCase()
            itemView.ivItemCode.text = ""+quiz!!.datePost
            this.currentHobby = quiz
            this.currentPosition = position
            if(!isDone(quiz.students)) {
                itemView.uidDone.visibility = View.GONE
            }
            if(!Supplier.isTeacher) {
                itemView.dashboard.visibility = View.GONE
            }
            startRStream()
        }
        private fun startRStream() {


            val myObservable = getObservable()


            val myObserver = getObserver()

            myObservable
                .subscribe(myObserver)
        }
        private fun getObserver(): Observer<Boolean> {
            return object : Observer<Boolean> {
                override fun onSubscribe(d: Disposable) {
                }
                override fun onNext(s: Boolean) {
                    if(s) {
                        itemView.quiz_item.background = context.getDrawable(R.drawable.bg_item_selected)
                        itemView.ivItemTitle.setTextColor(Color.RED)
                        Package.clickPackage = currentHobby!!
                    } else {
                        itemView.quiz_item.background = context.getDrawable(R.drawable.bgitem)
                        itemView.ivItemTitle.setTextColor(Color.BLACK)
                    }
                }


                override fun onError(e: Throwable) {
                    Log.e("show", "onError: " + e.message)
                }
                override fun onComplete() {
                    Log.d("show", "onComplete")
                }
            }
        }
        private fun getObservable(): Observable<Boolean> {
            active = !active
            return Observable.just(!active)
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
    }
}