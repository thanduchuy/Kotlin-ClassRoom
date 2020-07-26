package com.example.classroom.Model

import com.example.classroom.R
import com.example.classroom.models.Post

class ClassRoom (val id: String,val nameClass : String, val subClass: String, val titleClass: String, val roomClass: String, val codeClass:String,val listStudent:ArrayList<String>,val uidTeacher:String) {
    constructor():this("","","","","","",ArrayList<String>(),"") {

    }
}
object Supplier {
    var images = arrayListOf<Int>(
        R.drawable.internet,
        R.drawable.unicorn,
        R.drawable.maths,
        R.drawable.worldwideweb,
        R.drawable.sju,
        R.drawable.superpower
    )
    var roomTemp = ClassRoom()
    var listRoom = arrayListOf<ClassRoom>()
    var listPost = arrayListOf<Post>()
    var isTeacher = false
}