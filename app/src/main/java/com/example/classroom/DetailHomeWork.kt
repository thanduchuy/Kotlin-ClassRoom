package com.example.classroom

import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.classroom.models.Exercise
import com.example.classroom.models.Homework
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_detail_home_work.*
import java.util.*


class DetailHomeWork : AppCompatActivity() {
    var transiton = false
    private  var loadingDialog : Dialog? = null
    lateinit var mStorage:StorageReference
    var idPost = ""
    var idRoom = ""
    var isExist = false
    lateinit  var selectedFile : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_home_work)
        val bundle: Bundle? = intent.extras
        bundle?.let {
            idPost  = bundle.getString("idPost").toString()
            idRoom = bundle.getString("idRoom").toString()
        }
        getDetailHW()
        mStorage = FirebaseStorage.getInstance().getReference("Uploads-"+idPost)
        btnGoBack.setOnClickListener {
            finish()
        }
        buttonFile.setOnClickListener {

            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }
        buttonUpload.setOnClickListener {
            showLoading()
            upload()
        }
        setAnimation()
        checkStatus()
    }
    private fun getDetailHW() {
        showLoading()
        val dbRoom = FirebaseDatabase.getInstance().getReference("exercises")
        dbRoom.orderByChild("idPost").equalTo(idPost).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()) {
                    for (h in p0.children) {
                        val post = h.getValue(Exercise::class.java)
                        textView4.text = post!!.name.toString()
                        textView5.text = post!!.content.toString()
                    }
                }
            }
        })
        hideLoading()
    }
    private fun upload() {
        var mReference = mStorage.child(selectedFile.lastPathSegment!!)
        try {
            mReference.putFile(selectedFile).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                mReference.downloadUrl.addOnCompleteListener () {taskSnapshot ->
                    var url = taskSnapshot.result
                    if (isExist) {
                        updateExercire(url.toString())
                    } else {
                        saveExercire(url.toString())
                    }
                }

            }
        }catch(e:Exception) {
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }
    }
    private fun checkStatus() {
        val user = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().getReference("homeworks/"+idRoom+"/"+idPost+"/collect")
        ref.orderByChild("uid").equalTo(user!!.uid)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    imageView4.setImageResource(R.drawable.emoji)
                    comment.text = "Đã nộp bài tập"
                    isExist = true
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("error", databaseError.getMessage()) //Don't ignore errors!
            }
        }
        ref.addListenerForSingleValueEvent(valueEventListener)
    }
    private fun saveExercire(url:String) {
        val user = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().getReference("homeworks/"+idRoom+"/"+idPost+"/collect")
        val hwId = ref.push().key.toString()
        val hw = Homework(url, Date(),idRoom,idPost,user!!.uid,user?.displayName.toString(),user?.photoUrl.toString())
        ref.child(hwId).setValue(hw).addOnCompleteListener {
            nameFile.text = ""
            buttonUpload.setBackgroundResource(R.drawable.btnremove)
            buttonUpload.isClickable = false
            checkStatus()
            hideLoading()
        }
    }
    private fun updateExercire(url:String) {
        val user = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().getReference("homeworks/"+idRoom+"/"+idPost+"/collect")
        ref.orderByChild("uid").equalTo(user!!.uid)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val id = ds.key
                    val hw = Homework(url, Date(),idRoom,idPost,user!!.uid,user?.displayName.toString(),user?.photoUrl.toString())
                    val rf = FirebaseDatabase.getInstance().getReference("homeworks/"+idRoom+"/"+idPost+"/collect")
                    rf.child(id!!).setValue(hw).addOnCompleteListener {
                        nameFile.text = ""
                        buttonUpload.setBackgroundResource(R.drawable.btnremove)
                        buttonUpload.isClickable = false
                        checkStatus()
                        hideLoading()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("tag", databaseError.getMessage()) //Don't ignore errors!
            }
        }
        ref.addListenerForSingleValueEvent(valueEventListener)
    }
    private fun hideLoading() {
        loadingDialog?.let { if(it.isShowing) it.cancel() }
    }
    private fun showLoading() {
        hideLoading()
        loadingDialog = CommonUtils.showLoadingDialog(this)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            selectedFile = data!!.data!!
            if (selectedFile!= null ) {
                nameFile.text = getFileName(selectedFile)
                buttonUpload.setBackgroundResource(R.drawable.btncode)
                buttonUpload.isClickable = true
            }
        }
    }
    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.getScheme().equals("content")) {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.getPath()
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!!.plus(1))
            }
        }
        return result
    }
    fun setAnimation() {
        menuHomework.setOnClickListener {
            if(transiton) {
                menuHomework.animate().translationY(650.toFloat())
                image.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp)
            } else {
                menuHomework.animate().translationY(0.toFloat())
                image.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp)
            }
            transiton = !transiton
        }
        contentHomework.setOnClickListener {
            menuHomework.animate().translationY(650.toFloat())
            image.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp)
        }
    }
}
