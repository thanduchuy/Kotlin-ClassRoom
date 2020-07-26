package com.example.classroom

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.classroom.models.Message
import com.example.classroom.models.Package
import com.example.classroom.models.PointUser
import com.example.classroom.models.Question
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_quiz__detail.*
import java.util.*
import kotlin.collections.ArrayList


class Quiz_DetailActivity : AppCompatActivity() {
    var presCounter = 0
    var beginChar = 65
    var maxPresCounter = 0
    var point = 0
    var question : ArrayList<Question> = ArrayList<Question>()
    private var keys = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz__detail)
        keys.add("A")
        keys.add("B")
        keys.add("C")
        keys.add("D")

        for (item: String in keys) {
            addView(findViewById(R.id.layoutParent),item,findViewById(R.id.editText))
        }
        getQuestion()
        setFont()
        setEvent()
    }
    fun setFont() {
        var typeface:Typeface = Typeface.createFromAsset(assets,"fonts/FredokaOneRegular.ttf")
        textA.typeface= typeface
        textB.typeface= typeface
        textC.typeface= typeface
        textD.typeface= typeface
    }
    fun addView(viewParent:LinearLayout,text:String,edittext:EditText) {
        var linerlayout: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linerlayout.rightMargin = 50
        val textview:TextView = TextView(this)
        textview.layoutParams = linerlayout
        textview.background = this.resources.getDrawable(R.drawable.bgpink)
        textview.setTextColor(this.resources.getColor(R.color.violet))
        textview.gravity = Gravity.CENTER
        textview.text = text
        textview.isClickable = true
        textview.isFocusable = true
        textview.textSize = 32.toFloat()

        var typeface:Typeface = Typeface.createFromAsset(assets,"fonts/FredokaOneRegular.ttf")

        textQuestion.typeface = typeface
        textTitle.typeface = typeface
        editText.typeface = typeface
        btn_continue.typeface = typeface

        textview.setOnClickListener {
                    edittext.text.clear()
                    edittext.text = Editable.Factory.getInstance().newEditable(text)

        }

        viewParent.addView(textview)

    }
    fun setEvent() {
        btn_continue.setOnClickListener {
            val res = editText.text.toString()
            if(res==resultQuestion(question[presCounter],presCounter)){
                    point++
            }
            resetForm()
            if(presCounter<maxPresCounter-1) {
                presCounter++
            } else {
                val user = FirebaseAuth.getInstance().currentUser
                Package.clickPackage.students.add(PointUser(user?.displayName.toString(),user?.uid.toString(),user?.photoUrl.toString(),point))
                val dbQues = FirebaseDatabase.getInstance().getReference("quizs")
                dbQues.child(Package.clickPackage.idQuiz).setValue(Package.clickPackage)

                val intent = Intent( this, QuizActivity::class.java)
                intent.putExtra("id",Package.clickPackage.idRoom)
                startActivity(intent)
                overridePendingTransition(R.anim.fleft, R.anim.fhelper)

            }
            setDataForm(question[presCounter])
        }
    }
    fun getQuestion() {
        val dbQuestion = FirebaseDatabase.getInstance().getReference("questions")
        dbQuestion?.child(Package.clickPackage.idQuiz)?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // handle error
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot!!.exists()) {
                    for (h in dataSnapshot.children) {
                        val quiz = h.getValue(Question::class.java)
                         question.add(quiz!!)
                    }
                    maxPresCounter = question.size
                    setDataForm(question[presCounter])
                }
            }
        })
    }
    fun setDataForm( item:Question) {
           textQuestion.text = item.titleQuiz
           textA.text = item.answer[0].title
           textB.text = item.answer[1].title
           textC.text = item.answer[2].title
           textD.text = item.answer[3].title
    }
    fun resetForm() {
        editText.text.clear()
    }
    fun resultQuestion(item:Question,index:Int):String {
        var result = ""
        if(presCounter<maxPresCounter) {
            for (i in 0 until question[index].answer.size)
            {
                if(question[index].answer[i].status) {
                    result+= (beginChar+i).toChar()
                }
            }
        }
        return result
    }
}
