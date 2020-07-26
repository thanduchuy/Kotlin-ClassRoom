package com.example.classroom.models

import java.util.*

class Exercise (var name:String, var content:String, val dateEnd:Date, val idRoom:String, val idPost:String, val uid:String) {
    constructor():this("","",Date(),"","","") {

    }
}