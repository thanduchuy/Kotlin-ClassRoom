package com.example.classroom

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.classroom.models.ItemQuestion
import com.example.classroom.models.PointUser
import com.example.classroom.models.Question
import com.example.classroom.models.Quiz
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_quiz__create.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Quiz_CreateActivity : AppCompatActivity() {
    lateinit var idRoom:String
    lateinit var quiz:Quiz
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz__create)

        setAnimation()

        val bundle: Bundle? = intent.extras
        bundle?.let {
            idRoom = bundle.get("id").toString()
        }
    }

    fun setAnimation() {
        val logox: Typeface = Typeface.createFromAsset(assets, "fonts/Fredoka.ttf")
        titleQuiz.typeface = logox

        quizoption.alpha = 0.toFloat()
        quizoption.translationY = 400.toFloat()

        titleQuiz.alpha = 0.toFloat()
        titleQuiz.translationY = 100.toFloat()

        nameQuestion.alpha = 0.toFloat()
        nameQuestion.translationY = 100.toFloat()

        questionA.alpha = 0.toFloat()
        questionA.translationY = 400.toFloat()

        questionB.alpha = 0.toFloat()
        questionB.translationY = 400.toFloat()

        questionC.alpha = 0.toFloat()
        questionC.translationY = 400.toFloat()

        questionD.alpha = 0.toFloat()
        questionD.translationY = 400.toFloat()

        btnplus.setOnClickListener {
            addQuiz()
            setTransi()
        }
        btnadd.setOnClickListener {
            addQuestion()
        }
        btnok.setOnClickListener {
            val intent = Intent( this, ClassActivity::class.java)
            intent.putExtra("id",quiz.idRoom)
            startActivity(intent)
            overridePendingTransition(R.anim.fleft, R.anim.fhelper)
        }
    }
    fun addQuiz() {
        val dateFormatter: DateFormat = SimpleDateFormat("dd/MM/yyy")
        dateFormatter.setLenient(false)
        val today = Date()

        val s: String = dateFormatter.format(today)
        val ref = FirebaseDatabase.getInstance().getReference("quizs")
        val quizID = ref.push().key.toString()
        val name = nameQuiz.text.toString()
        val temp =  ArrayList<PointUser>()
        temp.add(PointUser())
        quiz =  Quiz(name,quizID,s,idRoom,temp)
        ref.child(quizID).setValue(quiz)
    }
    fun resetForm() {
        nameQuestion.text.clear()
        nameA.text.clear()
         switchA.isChecked = false
        nameB.text.clear()
        switchB.isChecked = false
        nameC.text.clear()
        switchC.isChecked = false
        nameD.text.clear()
        switchD.isChecked = false
    }
    fun addQuestion() {
        val dbQues = FirebaseDatabase.getInstance().getReference("questions")
        val id = dbQues.push().key.toString()
        val title = nameQuestion.text.toString()
        val namea=nameA.text.toString()
        val choisea = switchA.isChecked
        val nameb=nameB.text.toString()
        val choiseb = switchB.isChecked
        val namec=nameC.text.toString()
        val choisec = switchC.isChecked
        val named=nameD.text.toString()
        val choised = switchD.isChecked
        val temp =ArrayList<ItemQuestion>()
        temp.add(ItemQuestion(namea,choisea))
        temp.add(ItemQuestion(nameb,choiseb))
        temp.add(ItemQuestion(namec,choisec))
        temp.add(ItemQuestion(named,choised))
        if (choisea||choiseb||choisec||choised) {
            dbQues?.child(quiz.idQuiz)?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    // handle error
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()) {
                        val questions = ArrayList<Question>()
                        for (productSnapshot in dataSnapshot.children) {
                            val question = productSnapshot.getValue(Question::class.java)
                            questions.add(question!!)
                        }
                        questions.add(Question(title,temp))
                        dbQues.child(quiz.idQuiz).setValue(questions)
                    } else {
                        val questions = ArrayList<Question>()
                        questions.add(Question(title,temp))
                        dbQues.child(quiz.idQuiz).setValue(questions)
                    }
                }
            })
            Alerter.create(this)
                .setText("Thêm câu hỏi thành công")
                .setBackgroundColorRes(R.color.colorPrimaryDark) // or setBackgroundColorInt(Color.CYAN)
                .show()
            resetForm()
        } else {
            Alerter.create(this)
                .setText("Phải có ít nhất một đáp án đúng")
                .setBackgroundColorRes(R.color.error) // or setBackgroundColorInt(Color.CYAN)
                .show()
        }
    }
    fun setTransi() {
        quizoption.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(600).setStartDelay(300).start()
        btnplus.animate().alpha(0.toFloat()).translationY(400.toFloat()).setDuration(600).start()
        ictask.animate().alpha(0.toFloat()).translationY(400.toFloat()).setDuration(600).start()
        titlexx.animate().alpha(0.toFloat()).translationY(400.toFloat()).setDuration(600).start()
        subtitlexx.animate().alpha(0.toFloat()).translationY(400.toFloat()).setDuration(600).start()
        nameQuiz.animate().alpha(0.toFloat()).translationY(400.toFloat()).setDuration(600).start()
        nameQuiz.visibility = View.GONE
        titleQuiz.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(600).setStartDelay(300).start()
        nameQuestion.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(600).setStartDelay(300).start()

        questionA.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(600).setStartDelay(500).start()
        questionB.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(600).setStartDelay(700).start()
        questionC.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(600).setStartDelay(900).start()
        questionD.animate().alpha(1.toFloat()).translationY(0.toFloat()).setDuration(600).setStartDelay(1100).start()
    }
}
