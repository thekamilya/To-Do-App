package com.example.test.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.TopListItemBinding
import com.example.test.model.DateModel




class TopAdapter():RecyclerView.Adapter<TopAdapter.NoteViewHolder>() {

    lateinit var binding: TopListItemBinding
    var dates = ArrayList<DateModel>()
    var listener: ((Int) -> Unit)? = null // function
    class NoteViewHolder(private val binding: TopListItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(date: DateModel){

            binding.dayTxt.text = date.day.toString()
            binding.weekDayTxt.text = date.weekDay
            if(date.isClicked){

                binding.dayTxt.setTextColor(Color.parseColor("#00A86B"))
                binding.weekDayTxt.setTextColor(Color.parseColor("#00A86B"))
                binding.view.visibility = View.VISIBLE

            }else{
                binding.dayTxt.setTextColor(Color.BLACK)
                binding.weekDayTxt.setTextColor(Color.BLACK)
                binding.view.visibility = View.GONE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        binding = TopListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.itemView.setOnClickListener(){
            listener!!(position)
        }
        return holder.bind(dates[position])
    }

    override fun getItemCount(): Int {
       return dates.size
    }

    fun setList(newList: ArrayList<DateModel>){
        dates = newList
        notifyDataSetChanged()
    }

    fun onDateClickListener (callback: ((Int)->Unit)?) {
        listener = callback
    }
}