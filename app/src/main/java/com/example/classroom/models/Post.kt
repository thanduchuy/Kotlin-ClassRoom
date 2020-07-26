package com.example.classroom.models

class Post (var titlePost:String, var contentPost:String, val datePost:String, val idRoom:String, val idPost:String, val uid:String) {
    constructor():this("","","","","","") {

    }
}