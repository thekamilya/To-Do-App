package com.example.test.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.databinding.MainListItemBinding
import com.example.test.model.NoteModel

class MainAdapter() : RecyclerView.Adapter<MainAdapter.NoteViewHolder>(){
    lateinit var binding: MainListItemBinding
    private var noteList = listOf<NoteModel>()
    private var listener: ((Int) -> Unit)? = null;
    private var listener2: ((Int) -> Unit)? = null;
    private var listener3: ((Int) -> Unit)? = null;
    var numSelected = 0



    class NoteViewHolder(private val binding: MainListItemBinding): RecyclerView.ViewHolder(binding.root) {


        fun bind(note:NoteModel , showDate: Boolean){

            if(!showDate){
                binding.dateOuterConstraint.visibility = View.GONE
            } else {
                binding.dateOuterConstraint.visibility = View.VISIBLE
                binding.dayTxt.text = note.date.day.toString()
                binding.weekDayTxt.text = note.weekDay
            }
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
            if(note.isChecked == true){
                binding.checkbox.setImageResource(R.drawable.icons8_done)

            } else{
                binding.checkbox.setImageResource(0)
            }
            if (note.isSelected == true){
                binding.contentOuterConstraint.setBackgroundResource(R.drawable.half_transparent_grey_background)
            }else{

                binding.contentOuterConstraint.setBackgroundColor(Color.parseColor("#80FFFFFF"))
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

        checkBox.setOnClickListener(){
           listener!!(position)
        }
        val currentNote = noteList[position]
        val showDate = position == 0 || currentNote.date != noteList[position - 1].date



        val note = holder.itemView.findViewById<ConstraintLayout>(R.id.content_outer_constraint)
        note.setOnClickListener(){
            if(numSelected> 0){
                listener2!!(position)
            }else{
                listener3!!(position)
            }
        }

        note.setOnLongClickListener(){
            listener2!!(position)

            true
        }


        holder.bind(currentNote, showDate)
    }

    fun setList(newList: List<NoteModel>){
        noteList = newList
        notifyDataSetChanged() }

    fun setOnCheckboxClick(callback: (Int) -> Unit) {
        listener = callback
    }

    fun setOnLongClick(callback: (Int) -> Unit) {
        listener2 = callback
    }

    fun setSelected(num :Int){
        numSelected =num
        notifyDataSetChanged()
    }

    fun setOnClick(callback: (Int) -> Unit){
        listener3 = callback
    }

}