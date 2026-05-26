package com.notes.notesproxmlviews

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.card.MaterialCardView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import java.io.ByteArrayOutputStream
import java.io.InputStream

class NoteDetailsActivity : AppCompatActivity() {
    var titleEditText: EditText? = null
    var contentEditText: EditText? = null
    var saveNoteBtn: ImageButton? = null
    var pageTitleTextView: TextView? = null
    var title: String? = null
    var content: String? = null
    var docId: String? = null
    var isEditMode: Boolean = false
    var deleteNoteTextViewBtn: TextView? = null

    // Image-related variables
    var selectImageBtn: ImageButton? = null
    var removeImageBtn: ImageButton? = null
    var imageContainer: MaterialCardView? = null
    var noteImageView: ImageView? = null
    var imageBase64: String? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            processAndSetImage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        titleEditText = findViewById<EditText?>(R.id.notes_title_text)
        contentEditText = findViewById<EditText?>(R.id.notes_content_text)
        saveNoteBtn = findViewById<ImageButton?>(R.id.save_note_btn)
        pageTitleTextView = findViewById<TextView?>(R.id.page_title)
        deleteNoteTextViewBtn = findViewById<TextView?>(R.id.delete_note_text_view_btn)

        // Image views
        selectImageBtn = findViewById<ImageButton?>(R.id.select_image_btn)
        removeImageBtn = findViewById<ImageButton?>(R.id.remove_image_btn)
        imageContainer = findViewById<MaterialCardView?>(R.id.image_container)
        noteImageView = findViewById<ImageView?>(R.id.note_image_view)

        //receive data
        title = getIntent().getStringExtra("title")
        content = getIntent().getStringExtra("content")
        docId = getIntent().getStringExtra("docId")
        imageBase64 = getIntent().getStringExtra("image")

        if (docId != null && !docId!!.isEmpty()) {
            isEditMode = true
        }

        titleEditText!!.setText(title)
        contentEditText!!.setText(content)

        // Load existing image if available
        if (!imageBase64.isNullOrEmpty()) {
            try {
                val decodedString = Base64.decode(imageBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                noteImageView!!.setImageBitmap(bitmap)
                imageContainer!!.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (isEditMode) {
            pageTitleTextView!!.setText("Edit your note")
            deleteNoteTextViewBtn!!.setVisibility(View.VISIBLE)
        }

        saveNoteBtn!!.setOnClickListener(View.OnClickListener { v: View? -> saveNote() })
        deleteNoteTextViewBtn!!.setOnClickListener(View.OnClickListener { v: View? -> deleteNoteFromFirebase() })

        selectImageBtn!!.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        removeImageBtn!!.setOnClickListener {
            imageBase64 = null
            imageContainer!!.visibility = View.GONE
        }
    }

    fun saveNote() {
        val noteTitle = titleEditText!!.getText().toString()
        val noteContent = contentEditText!!.getText().toString()
        if (noteTitle.isEmpty()) {
            titleEditText!!.setError("Title is required")
            return
        }
        val note: Note = Note()
        note.setTitle(noteTitle)
        note.setContent(noteContent)
        note.setTimestamp(Timestamp.now())
        note.setImage(imageBase64)

        saveNoteToFirebase(note)
    }

    private fun processAndSetImage(uri: Uri) {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            var bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap != null) {
                // Resize image if it is too large
                bitmap = resizeBitmap(bitmap, 800) // Max 800px width/height

                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream) // Compress to 70% quality JPEG
                val byteArray = outputStream.toByteArray()
                imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT)

                noteImageView!!.setImageBitmap(bitmap)
                imageContainer!!.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Utility.showToast(this, "Failed to load image")
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    fun saveNoteToFirebase(note: Note) {
        val documentReference: DocumentReference?
        if (isEditMode) {
            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId!!)
        } else {
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document()
        }

        documentReference.set(note).addOnCompleteListener(object : OnCompleteListener<Void?> {
            override fun onComplete(task: Task<Void?>) {
                if (task.isSuccessful()) {
                    //note is added
                    Utility.showToast(this@NoteDetailsActivity, "Note added successfully")
                    finish()
                } else {
                    Utility.showToast(this@NoteDetailsActivity, "Failed while adding note")
                }
            }
        })
    }

    fun deleteNoteFromFirebase() {
        val documentReference: DocumentReference?
        documentReference = Utility.getCollectionReferenceForNotes().document(docId!!)
        documentReference.delete().addOnCompleteListener(object : OnCompleteListener<Void?> {
            override fun onComplete(task: Task<Void?>) {
                if (task.isSuccessful()) {
                    //note is deleted
                    Utility.showToast(this@NoteDetailsActivity, "Note deleted successfully")
                    finish()
                } else {
                    Utility.showToast(this@NoteDetailsActivity, "Failed while deleting note")
                }
            }
        })
    }
}