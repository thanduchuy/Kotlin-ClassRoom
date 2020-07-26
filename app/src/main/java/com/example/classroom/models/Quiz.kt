package com.example.classroom.models

class Quiz (var nameQuiz:String, var idQuiz:String, val datePost:String,val idRoom:String,val students:ArrayList<PointUser>) {
    constructor():this("","","","",ArrayList<PointUser>()) {

    }
}
class PointUser(var nameUser:String,var uid:String,var avatar:String,var point:Int) {
    constructor():this("","","",0) {

    }
}
object Package {
    var clickPackage:Quiz = Quiz()
}