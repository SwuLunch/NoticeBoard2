package com.example.noticeboard

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.service.voice.VoiceInteractionSession
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.circularimageview.CircularImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.util.jar.Manifest

class AddUpdateRecordActivity : AppCompatActivity() {

    //permission constants
    private val CAMERA_REQUEST_CODE = 100;
    private val STORAGE_REQUEST_CODE = 100;
    private val IMAGE_PICK_CAMERA_CODE = 102;
    private val IMAGE_PICK_GALLERY_CODE = 102;
    private lateinit var cameraPermissions:Array<String>
    private lateinit var storagePermission:Array<String>

    private var actionBar:ActionBar? = null;

    lateinit var dbHelper:MyDbHelper

    lateinit var profileIv : CircularImageView
    lateinit var saveBtn: FloatingActionButton
    lateinit var nameEt : EditText
    lateinit var whereEt : EditText
    lateinit var timeEt : EditText
    lateinit var contentEt : EditText

    //variables that will contain data to save in database
    private var imageUri: Uri? = null
    private var name:String? = ""
    private var where:String? = ""
    private var time:String? = ""
    private var contextt: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_record)

        profileIv = findViewById<CircularImageView>(R.id.profileIv)
        saveBtn = findViewById<FloatingActionButton>(R.id.saveBtn)
        nameEt = findViewById<EditText>(R.id.nameEt)
        whereEt = findViewById<EditText>(R.id.whereEt)
        timeEt = findViewById<EditText>(R.id.timeEt)
        contentEt = findViewById<EditText>(R.id.contentEt)

        //init db helper class
        dbHelper = MyDbHelper(this)

        actionBar = supportActionBar
        actionBar!!.title = "게시물 작성하기"
        //back button in actionbar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)

        //init permission arrays
        cameraPermissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        //click imageView to pick image
        profileIv.setOnClickListener{
            imagePickDialog();
        }
        saveBtn.setOnClickListener{
            inputData()
            var intent = Intent(this, Board_Main::class.java)
            startActivity(intent)
        }
    }

    private fun inputData() {
        //get data
        name = ""+nameEt.text.toString().trim()
        where = ""+whereEt.text.toString().trim()
        time = "" + timeEt.text.toString().trim()
        contextt = "" + contentEt.text.toString().trim()

        //save data to db
        val timestamp = System.currentTimeMillis()
        val id = dbHelper.insertRecord(
            ""+name,
            ""+imageUri,
            ""+time,
            ""+where,
            ""+contextt,
            ""+timestamp,
            ""+timestamp
        )
        Toast.makeText(this, "Record Added against ID $id", Toast.LENGTH_SHORT).show()
    }

    private fun imagePickDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")
        builder.setItems(options){dialog, which ->
            if(which ==0){
                //camera pick
                if(!checkCameraPermission()){
                    requestCameraPermission()
                }else{
                    pickFromCamera()
                }

            }else{
                //gallery pick
                if(!checkStoragePermission()){
                   requestStoragePermission()
                }else{
                    pickFromGallery()
                }
            }
        }
        //show dialog
        builder.show()
    }

    private fun pickFromGallery() {
        //pick image from gallery using Intent
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"  //only image pick
        startActivityForResult(
            galleryIntent,
            IMAGE_PICK_GALLERY_CODE
        )
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE)
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun pickFromCamera() {
        //pick image from camera using Intent
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Image Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image Desription")

        //put image uri
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //intent to open camera
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(
            cameraIntent,
            IMAGE_PICK_CAMERA_CODE
        )
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE)
    }

    private fun checkCameraPermission(): Boolean {
        val results = ContextCompat.checkSelfPermission(this,
        android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val results1 = ContextCompat.checkSelfPermission(this,
        android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        return results && results1
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isNotEmpty()){
                    //if allowed returns true otherwise false
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if(cameraAccepted && storageAccepted)
                        pickFromCamera()
                    else
                        Toast.makeText(this,"Camera and Storage permission are required", Toast.LENGTH_SHORT).show()
                }

            }
            STORAGE_REQUEST_CODE -> {
                if(grantResults.isNotEmpty()){
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if(storageAccepted)
                        pickFromGallery()
                }else{
                    Toast.makeText(this,"Storage permission is required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //image picked from camera or gallery will be received here
        if(resultCode == Activity.RESULT_OK){
            //image is picked
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                CropImage.activity(data!!.data).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                val result = CropImage.getActivityResult(data)
                if(resultCode == Activity.RESULT_OK){
                    val resultUri = result.uri
                    imageUri = resultUri
                    //set image
                    profileIv.setImageURI(resultUri)
                }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    //error
                    val error = result.error
                    Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}