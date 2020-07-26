package com.example.classroom.models

class ItemQuestion(val title:String,val status:Boolean) {
    constructor():this("",false) {

    }
}
class Question(val titleQuiz:String,val answer: ArrayList<ItemQuestion>) {
    constructor():this("",ArrayList<ItemQuestion>()) {

    }
}