package com.example.test.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.database.dbManager
import com.example.test.model.CustomDate
import com.example.test.model.NoteModel

class MyViewModel : ViewModel() {
    // MutableLiveData list variable to hold a list of NoteModel objects
//    lateinit var db: DatabaseReference
    var noteList: MutableLiveData<List<NoteModel>?> = MutableLiveData()
//    var dbauth = FirebaseAuth.getInstance()
    var dbManager = dbManager()

    fun setDbName(name:String){
        dbManager.setDbName(name)
//        var res = name
//        if(name.contains("@")){
//           res =name.replace("@","a")
//        }
//        if(name.contains(".")){
//            res = res.replace(".","a")
//        }
//        db = FirebaseDatabase.getInstance().getReference("${res}")
    }
    fun addUser(email: String, password: String , callback: (String) -> Unit){
        dbManager.addUser(email,password,callback)
//        dbauth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if(task.isSuccessful) {
//                    callback("Successfully signed in")
//                } else {
//                    callback(task.exception?.message.toString())
//                }
//            }

    }

    fun logIn (email: String, password: String , callback: (Boolean) -> Unit){

        dbManager.logIn(email , password, callback)
//        dbauth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if(task.isSuccessful) {
//                    callback(true)
//                } else {
//                    callback(false)
//                }
//            }

    }




    private fun sortListDescend(list :List<NoteModel>) : List<NoteModel>{
        val sortedList = list.sortedWith(compareByDescending<NoteModel> { it.date })
        return sortedList
    }

    // A function to add a new NoteModel object to the noteList
    fun addNoteToList(note: NoteModel) {

        dbManager.addNoteToList(note)
        // Get the current noteList value, or an empty list if it's null
        val currentList = noteList.value ?: emptyList()

        // Create a new list with the current notes and the new note added
        val updatedList = currentList + note

        // Update the noteList value with the updated list
        noteList.value = sortListDescend(updatedList)

        // add new note to database

    }

    fun getDbData (){
        var notes = ArrayList<NoteModel>()
        dbManager.getDbData(){ notes ->
            noteList.value = sortListDescend(notes)
        }
//        db.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()){
//                    for (noteSnapshot in snapshot.children){
//                        val note = noteSnapshot.getValue(NoteModel::class.java)
//                        if (note!= null){
//                            note?.isSelected = false
//                            note?.id = noteSnapshot.key
//                            notes.add(note)
//                        }
//                    }
//                }
//                noteList.value = sortListDescend(notes)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//        })

    }

    fun updateList(updatedList: List<NoteModel>?) {
        noteList.value = updatedList
    }

    fun getValue(): List<NoteModel>? {
        return noteList.value
    }

    fun getIndexOf(day: Int ) : Int{
        for (i in 0 until (noteList.value?.size ?:0 )){
            if (noteList.value?.get(i)?.date?.day ?: 0 == day  ){
                return i
            }
        }
        return -1
    }

    fun changeIsChecked (position: Int){
        var currentList = noteList.value ?: emptyList()
        currentList[position].isChecked = !(currentList[position].isChecked)
        var updatedNote = currentList[position]
        dbManager.updateVal(updatedNote)
        noteList.value = currentList

    }

    fun changeIsSelected(position: Int){
        var currentList = noteList.value ?: emptyList()
        currentList[position].isSelected = !(currentList[position].isSelected)
        var updatedNote = currentList[position]
        dbManager.updateVal(updatedNote)
        noteList.value = currentList

    }

    fun getNumOfStatusOccurence(status : String , sinceDate: CustomDate = CustomDate(1 , 1, 1599)) : Int{
        var num =0
        for (i in 0 until (noteList.value?.size ?: 0)){
            if(noteList.value?.get(i)?.status ?:""  == status && noteList.value?.get(i)?.date!! >= sinceDate){
                num ++
            }
        }
        return num
    }


    fun getNumOfCompletedStatusOccurence(status : String , sinceDate: CustomDate = CustomDate(1 , 1, 1599)):Int {
            var num =0
            for (i in 0 until (noteList.value?.size ?: 0)){
                if(noteList.value?.get(i)?.status ?:""  == status && noteList.value?.get(i)?.isChecked == true && noteList.value?.get(i)?.date!! >= sinceDate){
                    num ++
                }
            }
            return num

    }

    fun getStatusRate(status: String , sinceDate: CustomDate = CustomDate(1 , 1, 1599)): Int{
        var result = 0
        val completed = getNumOfCompletedStatusOccurence(status, sinceDate)
        val all = getNumOfStatusOccurence(status , sinceDate)

        if (all != 0)
        {
            result = (completed * 100) / all
        }
        return result
    }

    fun getNumOfAllNotes(sinceDate: CustomDate= CustomDate(1, 1 , 1599)): Int{
        var num = 0
        for (i in 0 until (noteList.value?.size ?: 0)){
            if(noteList.value?.get(i)?.date!! >= sinceDate){
                num ++
            }
        }
        return num
    }

    fun getNumOfAllCompletedNotes(sinceDate: CustomDate= CustomDate(1, 1 , 1599)): Int{
        var num = 0
        for (i in 0 until (noteList.value?.size ?: 0)){
            if(noteList.value?.get(i)?.isChecked == true && noteList.value?.get(i)?.date!! >= sinceDate){
                num ++
            }
        }
        return num

    }

    fun getRate( sinceDate: CustomDate= CustomDate(1, 1 , 1599)) : Int{
        var result = 0
        val completed = getNumOfAllCompletedNotes(sinceDate)
        val all = getNumOfAllNotes(sinceDate)

        if (all != 0)
        {
            result = (completed * 100) / all
        }
        return result
    }

    fun getLastFour(): List<NoteModel>? {
        val list = noteList.value
        val new = list?.subList(list.size - 4, list.size)
        return new

    }

    fun getNumSelected(): Int{
        var numSelected = 0
        for (item in noteList.value!!){
            if(item.isSelected == true){
                numSelected ++
            }
        }
        return numSelected
    }


    fun deleteSelected(){
        for (item in noteList.value!!){
            if(item.isSelected == true){
                dbManager.deleteVal(item)
            }
        }
        getDbData()
    }

    fun getTask(pos: Int): String{
        return noteList.value?.get(pos)?.task ?: ""
    }

    fun getDescription(pos: Int): String{
        return noteList.value?.get(pos)?.description ?: ""
    }

    fun updateNote (note : NoteModel){

        dbManager.updateVal(note)
        getDbData()
    }

    fun getNumOfBiggestStatusOccurence(): Int{
        var result = 1
        val allStatus = listOf<String>("Hobby" , "Lifestyle" ,"Personal" , "Work", "No list")

        for (i in allStatus.indices){
            if (result < getNumOfStatusOccurence(allStatus[i])){
                result = getNumOfStatusOccurence(allStatus[i])
            }
        }

        return result
    }




















}