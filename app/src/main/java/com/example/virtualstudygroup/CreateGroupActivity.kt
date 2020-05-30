package com.example.virtualstudygroup

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_create.*
import java.util.*

class CreateGroupActivity : AppCompatActivity() {

    private var groupName: String? = null
    private var courseName: String? = null
    private var currNumber: Int = 1
    private var totalNumber: Int? = null
    private var groupImage: Uri? = null
    private var examSquad: Boolean = false
    private var homeworkHelp: Boolean = false
    private var labMates: Boolean = false
    private var noteExchange: Boolean = false
    private var projectPartners: Boolean = false
    private var groupDescription: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        btnExamSquad.setOnClickListener {
            examSquad = if (examSquad) {
                btnExamSquad.setBackgroundColor(resources.getColor(R.color.beige))
                !examSquad
            } else {
                btnExamSquad.setBackgroundColor(Color.GREEN)
                !examSquad
            }
        }

        btnHomeworkHelp.setOnClickListener {
            homeworkHelp = if (homeworkHelp) {
                btnHomeworkHelp.setBackgroundColor(resources.getColor(R.color.beige))
                !homeworkHelp
            } else {
                btnHomeworkHelp.setBackgroundColor(Color.GREEN)
                !homeworkHelp
            }
        }

        btnLabMates.setOnClickListener {
            labMates = if (labMates) {
                btnLabMates.setBackgroundColor(resources.getColor(R.color.beige))
                !labMates
            } else {
                btnLabMates.setBackgroundColor(Color.GREEN)
                !labMates
            }
        }

        btnProjectPartners.setOnClickListener {
            projectPartners = if (projectPartners) {
                btnProjectPartners.setBackgroundColor(resources.getColor(R.color.beige))
                !projectPartners
            } else {
                btnProjectPartners.setBackgroundColor(Color.GREEN)
                !projectPartners
            }
        }

        btnNoteExchange.setOnClickListener {
            noteExchange = if (noteExchange) {
                btnNoteExchange.setBackgroundColor(resources.getColor(R.color.beige))
                !noteExchange
            } else {
                btnNoteExchange.setBackgroundColor(Color.GREEN)
                !noteExchange
            }
        }

        btnGroupImgUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        btnFinish.setOnClickListener {
            groupName = etGroupName.text.toString()
            courseName = etCourseName.text.toString()
            totalNumber = etGroupSize.text.toString().toInt()
            groupDescription = etGroupDescription.text.toString()
            groupImage?.let { photo ->
                uploadPhoto(photo)
            } ?: run {
                val defaultPhotoName = "default_image.png"
                val ref = FirebaseStorage.getInstance().getReference("/images/group_image/$defaultPhotoName")
                ref.downloadUrl.addOnSuccessListener {
                    groupImage = it
                }
            }
        }
    }

    private fun uploadPhoto(photoUri: Uri) {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/group_image/$filename")
        ref.putFile(photoUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    Log.i(RegisterActivity.TAG, "Photo uploaded, uri = $it")
                    groupImage = it
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.i(RegisterActivity.TAG, "photo selected")

            groupImage = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, groupImage)

            btnGroupImgUpload.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "Failed to select photo", Toast.LENGTH_SHORT).show()
        }
    }

}