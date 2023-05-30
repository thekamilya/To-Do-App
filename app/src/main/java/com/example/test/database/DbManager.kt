package com.example.test.database

import com.example.test.model.NoteModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class dbManager {

    lateinit var db: DatabaseReference
    var dbauth = FirebaseAuth.getInstance()

    fun setDbName(name:String){
        var res = name
        if(name.contains("@")){
           res =name.replace("@","a")
        }
        if(name.contains(".")){
            res = res.replace(".","a")
        }
        db = FirebaseDatabase.getInstance().getReference("${res}")
    }

    fun addUser(email: String, password: String , callback: (String) -> Unit){

        dbauth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    callback("Successfully signed in")
                } else {
                    callback(task.exception?.message.toString())
                }
            }

    }
    fun logIn (email: String, password: String , callback: (Boolean) -> Unit){

        dbauth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

    }
    fun addNoteToList(note: NoteModel) {

        note.id = db.push().key
        db.child(note.id!!).setValue(note)

    }
    private fun sortListDescend(list :List<NoteModel>) : List<NoteModel>{
        val sortedList = list.sortedWith(compareByDescending<NoteModel> { it.date })
        return sortedList
    }
    fun getDbData (callback: (ArrayList<NoteModel>) -> Unit){
        var notes = ArrayList<NoteModel>()
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (noteSnapshot in snapshot.children){
                        val note = noteSnapshot.getValue(NoteModel::class.java)
                        if (note!= null){
                            note?.isSelected = false
                            note?.id = noteSnapshot.key
                            notes.add(note)
                        }
                    }
                }
                callback(notes)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    fun updateVal(updatedNote: NoteModel){
        db.child(updatedNote.id!!).setValue(updatedNote)
    }

    fun deleteVal(note: NoteModel){
        note.id?.let { db.child(it).setValue(null) }
    }

}