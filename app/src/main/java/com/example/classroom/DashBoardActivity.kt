package com.example.classroom

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classroom.adapters.DashBoardAdapter
import com.example.classroom.models.Package
import com.example.classroom.models.PointUser
import kotlinx.android.synthetic.main.activity_dash_board.*

class DashBoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)


        setupAdapter(removeNull(Package.clickPackage.students))
        setFontFamily()
    }
    fun setFontFamily() {
        val mlight: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratLight.ttf")
        val mregular: Typeface = Typeface.createFromAsset(assets, "fonts/MontserratRegular.ttf")

        titleDash.typeface = mregular
        countDash.typeface = mlight

        countDash.text = "Số người đã làm : "+removeNull(Package.clickPackage.students).size
    }
    private fun setupAdapter(arr:ArrayList<PointUser>) {
        val layoutManager1 = LinearLayoutManager(this)
        layoutManager1.orientation = LinearLayoutManager.VERTICAL
        recycle.layoutManager = layoutManager1
        val adapter1 = DashBoardAdapter(this, arr)
        adapter1.notifyDataSetChanged()

        recycle.adapter = adapter1
    }
    private  fun removeNull(user:ArrayList<PointUser>):ArrayList<PointUser> {
        var temp = ArrayList<PointUser>()
        for (i in 0 until user.size)
        {
            if(user[i].nameUser!="") {
                temp.add(user[i])
            }
        }
        return temp
    }
}
