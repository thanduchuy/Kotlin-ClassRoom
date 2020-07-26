package com.example.classroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.classroom.models.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_hw.*
import java.util.*

class CreateHWActivity : AppCompatActivity() {
    var idRoom : String =  ""
    var dateEnd : Date = Date()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_hw)
        val today = Calendar.getInstance()
        datePicker1.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            dateEnd = Date(year,month+1,day)
        }
        btnGoBack.setOnClickListener {
            onBackPressed()
        }
        val bundle: Bundle? = intent.extras
        bundle?.let {
            idRoom = bundle.getString("id").toString()
        }
        btnCreate.setOnClickListener {
            saveExercire()
        }
    }
    fun saveExercire() {
        var name = nameHW.text.toString()
        var content = contentHW.text.toString()
        if (name.isEmpty() || content.isEmpty()) {
            return
        } else {
            val exer = FirebaseDatabase.getInstance().getReference("exercises")
            val user = FirebaseAuth.getInstance().currentUser
            val exerID = exer.push().key.toString()
            val exercise = Exercise(name,content,dateEnd,idRoom,exerID,user?.uid.toString())
            exer.child(exerID).setValue(exercise).addOnCompleteListener {
                val intent = Intent(this, ClassActivity::class.java)
                intent.putExtra("id", idRoom)
                startActivity(intent)
                overridePendingTransition(R.anim.fleft, R.anim.fhelper)
            }
        }
    }
}
