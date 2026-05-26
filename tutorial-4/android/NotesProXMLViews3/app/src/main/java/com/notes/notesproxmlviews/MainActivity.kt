package com.notes.notesproxmlviews

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
    var addNoteBtn: FloatingActionButton? = null
    var recyclerView: RecyclerView? = null
    var menuBtn: ImageButton? = null
    var noteAdapter: NoteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addNoteBtn = findViewById<FloatingActionButton?>(R.id.add_note_btn)
        recyclerView = findViewById<RecyclerView?>(R.id.recyler_view)
        menuBtn = findViewById<ImageButton?>(R.id.menu_btn)

        addNoteBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@MainActivity,
                    NoteDetailsActivity::class.java
                )
            )
        })
        menuBtn!!.setOnClickListener(View.OnClickListener { v: View? -> showMenu() })
        setupRecyclerView()
    }

    fun showMenu() {
        val popupMenu = PopupMenu(this@MainActivity, menuBtn)
        popupMenu.getMenu().add("Logout")
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem): Boolean {
                if (menuItem.getTitle() === "Logout") {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                    return true
                }
                return false
            }
        })
    }

    fun setupRecyclerView() {
        val query = Utility.getCollectionReferenceForNotes()
            .orderBy("timestamp", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query, Note::class.java).build()
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        noteAdapter = NoteAdapter(options, this)
        recyclerView!!.setAdapter(noteAdapter)
    }

    override fun onStart() {
        super.onStart()
        noteAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        noteAdapter?.stopListening()
    }

    override fun onResume() {
        super.onResume()
        noteAdapter?.notifyDataSetChanged()
    }
}