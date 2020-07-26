package com.example.classroom.models

import java.util.*

class Homework (var file:String, val date:Date, val idRoom:String, val idPost:String, val uid:String,var name:String,var avatar:String ) {
    constructor():this("",Date(),"","","","","") {
    }
}