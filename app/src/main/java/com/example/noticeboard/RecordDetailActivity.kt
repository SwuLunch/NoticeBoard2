package com.example.noticeboard

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.blogspot.atifsoftwares.circularimageview.CircularImageView
import java.util.*

class RecordDetailActivity : AppCompatActivity() {

    private var actionBar:ActionBar? = null
    private var dbHelper:MyDbHelper?= null
    private var recordId:String?= null
    lateinit var titleTv : TextView
    lateinit var addedDateTv : TextView

    lateinit var profileIv: CircularImageView
    lateinit var contentttTv : TextView
    lateinit var timeTv : TextView
    lateinit var addressTv : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_detail)

        titleTv = findViewById<TextView>(R.id.titleTv)
        addedDateTv = findViewById<TextView>(R.id.addedDateTv)

        profileIv = findViewById<CircularImageView>(R.id.profileIv)
        contentttTv = findViewById<TextView>(R.id.contenttTv)
        timeTv = findViewById<TextView>(R.id.timeTv)
        addressTv = findViewById<TextView>(R.id.addressTv)

        actionBar = supportActionBar
        actionBar !!.title = "게시물 정보"
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        dbHelper = MyDbHelper(this)

        var intent = intent
        recordId = intent.getStringExtra("RECORD_ID")

        showRecordDetails()

    }

    private fun showRecordDetails() {
        val selectQuery = "SELECT * FROM " + Constants.TABLE_NAME+ " WHERE " + Constants.C_ID + " =\""+recordId+"\""

        val db = dbHelper!!.writableDatabase
        val cursor = db.rawQuery(selectQuery,null)

        if(cursor.moveToFirst()){
            do{
                val id = ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID))
                val name = ""+cursor.getString(cursor.getColumnIndex(Constants.C_NAME))
                val address = ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDRESS))
                val image = ""+cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE))
                val time = ""+cursor.getString(cursor.getColumnIndex(Constants.C_TIME))
                val contentt = ""+cursor.getString(cursor.getColumnIndex(Constants.C_CONTENT))
                val addedTimeStamp = ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIME_STAMP))
                val updatedTimeStamp = ""+cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIME_STAMP))

                val calendar1 = Calendar.getInstance(Locale.getDefault())
                calendar1.timeInMillis = addedTimeStamp.toLong()
                val timeAdded = android.text.format.DateFormat.format("yyyy/MM/dd hh:mm:aa", calendar1)

                val calendar2 = Calendar.getInstance(Locale.getDefault())
                calendar1.timeInMillis = updatedTimeStamp.toLong()
                val timeUpdated = android.text.format.DateFormat.format("yyyy/MM/dd hh:mm:aa", calendar2)

                //set data
                titleTv.text = name
                addedDateTv.text = timeAdded

                contentttTv.text = contentt
                addressTv.text = address
                timeTv.text = time

                if(image == "null"){
                    profileIv.setImageResource(R.drawable.ic_person_black)
                }else
                    profileIv.setImageURI(Uri.parse(image))

            }while (cursor.moveToNext())
        }
        db.close()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}