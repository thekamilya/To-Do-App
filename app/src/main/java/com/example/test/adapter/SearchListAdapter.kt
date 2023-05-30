package com.example.test.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.databinding.MainListItemBinding
import com.example.test.model.NoteModel

class SearchListAdapter: RecyclerView.Adapter<SearchListAdapter.NoteViewHolder>(){
    lateinit var binding: MainListItemBinding
    private var noteList = ArrayList<NoteModel>()

    class NoteViewHolder(private val binding: MainListItemBinding): RecyclerView.ViewHolder(binding.root) {


        fun bind(note: NoteModel){

            binding.dateOuterConstraint.visibility = View.GONE
            binding.descriptionTxt.text= note.task
            binding.noteStatusTxt.text = note.status

            when( note.status){
                "No List" -> binding.status.setBackgroundResource(R.drawable.status_nolist)
                "Lifestyle" -> binding.status.setBackgroundResource(R.drawable.status_lifestyle_green)
                "Work" -> binding.status.setBackgroundResource(R.drawable.status_work_orange)
                "Personal" -> binding.status.setBackgroundResource(R.drawable.status_personal_blue)
                "Hobby" -> binding.status.setBackgroundResource(R.drawable.status_hobby_red)
                else -> binding.status.setBackgroundResource(R.drawable.status_nolist)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        binding = MainListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val checkBox: ImageView = holder.itemView.findViewById(R.id.checkbox)

        val currentNote = noteList[position]
        holder.bind(currentNote)


    }

    fun setList(newList: ArrayList<NoteModel>){
        noteList = newList
        notifyDataSetChanged() }

}