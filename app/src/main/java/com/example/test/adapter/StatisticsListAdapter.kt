package com.example.test.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.databinding.MainListItemBinding
import com.example.test.databinding.StatisticsListItemBinding
import com.example.test.model.NoteModel
import com.example.test.model.StatModel

class StatisticsListAdapter : RecyclerView.Adapter<StatisticsListAdapter.StatisticsViewHolder>(){
    lateinit var binding: StatisticsListItemBinding
    private var statList = listOf<StatModel>()

    class StatisticsViewHolder(private val binding: StatisticsListItemBinding): RecyclerView.ViewHolder(binding.root) {


        fun bind(stat: StatModel) {

            binding.statusTxt.text = stat.status
            binding.completed.text = stat.completed.toString()
            binding.all.text = "/${stat.all}"
            binding.rateStatus.text = stat.rate.toString()

            when( stat.status){
                "No List" -> binding.statusImg.setBackgroundResource(R.drawable.status_nolist)
                "Lifestyle" -> binding.statusImg.setBackgroundResource(R.drawable.status_lifestyle_green)
                "Work" -> binding.statusImg.setBackgroundResource(R.drawable.status_work_orange)
                "Personal" -> binding.statusImg.setBackgroundResource(R.drawable.status_personal_blue)
                "Hobby" -> binding.statusImg.setBackgroundResource(R.drawable.status_hobby_red)
                else -> binding.statusImg.setBackgroundResource(R.drawable.status_nolist)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        binding = StatisticsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatisticsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        holder.bind(statList[position])
    }


    override fun getItemCount(): Int {
        return statList.size
    }

    fun setList(newList: List<StatModel>){
        statList = newList
        notifyDataSetChanged() }


}