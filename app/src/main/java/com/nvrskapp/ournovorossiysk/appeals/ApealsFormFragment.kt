package com.nvrskapp.ournovorossiysk.appeals

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.nvrskapp.ournovorossiysk.Problem
import com.nvrskapp.ournovorossiysk.R
import com.nvrskapp.ournovorossiysk.databinding.FragmentApealsFormBinding
import kotlinx.android.synthetic.main.fragment_apeals_form.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


class ApealsFormFragment : Fragment() {
    private lateinit var firebase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageBitmap: Bitmap
    val REQUEST_IMAGE_CAPTURE = 1

    fun rotateImage(source: Bitmap, angle: Float) : Bitmap? {
        val bitmap: Bitmap? = null
        val matrix: Matrix = Matrix()
        matrix.postRotate(angle)
        try {
            val bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
            matrix, true)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }
        return bitmap
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val binding = DataBindingUtil.inflate<FragmentApealsFormBinding>(
            inflater,
            R.layout.fragment_apeals_form,
            container,
            false
        ).apply {
            databaseReference = Firebase.database.getReference("problems")
            storageReference = Firebase.storage.getReference("images")
            val img = storageReference.child("photo" + (UUID.randomUUID()).toString() + ".jpg")

            problemImageButton.setOnClickListener {
                var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
                catch (e: Exception) { }
            }

            problemButton.setOnClickListener {
                if (titleEditText.text.isNullOrBlank()) {
                    titleInputLayout.error = "Поле не должно быть пустым"
                    return@setOnClickListener
                } else {
                    titleInputLayout.error = ""
                }
                if (addresEditText.text.isNullOrBlank()) {
                    addresInputLayout.error = "Поле не должно быть пустым"
                    return@setOnClickListener
                } else {
                    addresInputLayout.error = ""
                }
                if (hmNumberEditTExt.text.isNullOrBlank()) {
                    hmNumberInputLayout.error = "Поле не должно быть пустым"
                    return@setOnClickListener
                } else {
                    hmNumberInputLayout.error = ""
                }
                if (descriptionEditText.text.isNullOrBlank()) {
                    descriptionInputLayout.error = "Поле не должно быть пустым"
                    return@setOnClickListener
                } else {
                    descriptionInputLayout.error = ""
                }

                problem_info_body.visibility = View.INVISIBLE
                indicator.visibility = View.VISIBLE
                val outputStream: ByteArrayOutputStream = ByteArrayOutputStream()

                var correctedBitmap: Bitmap? = null

                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val inputStream: InputStream = ByteArrayInputStream(outputStream.toByteArray())

                val exifInterface: ExifInterface = ExifInterface(inputStream)
                val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90){
                    correctedBitmap = rotateImage(imageBitmap, 90f)
                }
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180){
                    correctedBitmap = rotateImage(imageBitmap, 180f)
                }

                val data = outputStream.toByteArray()
                val uploadTask = img.putBytes(data)
                uploadTask.addOnCompleteListener{
                    val task: Task<Uri> = it.getResult()!!.storage.downloadUrl.addOnSuccessListener {
                        val taskRes: String = it.toString()
                        val title: String = titleEditText.text.toString()
                        val address: String = "Новороссийск, " + addresEditText.text.toString() + ", " + hmNumberEditTExt.text.toString()
                        val description: String = descriptionEditText.text.toString()

                        val problem: Problem = Problem(title, address, taskRes, description)
                        databaseReference.push().setValue(problem)


                        findNavController().navigate(R.id.action_apealsFormFragment_to_appealsFragment)
                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        indicator.visibility = View.INVISIBLE
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras: Bundle? = data!!.getExtras()
            imageBitmap = extras!!.get("data") as Bitmap
            photoStatus.text = "Фото добавленно"
        }
    }
}