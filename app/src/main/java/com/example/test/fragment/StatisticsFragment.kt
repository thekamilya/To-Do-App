package com.example.test.fragment

import android.content.res.ColorStateList
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.R
import com.example.test.adapter.StatisticsListAdapter
import com.example.test.databinding.FragmentStatisticsBinding
import com.example.test.model.CustomDate
import com.example.test.model.StatModel
import com.example.test.viewmodel.MyViewModel

class StatisticsFragment : Fragment() {
    private val viewModel: MyViewModel by activityViewModels()
    lateinit var binding: FragmentStatisticsBinding
    val statAdapter = StatisticsListAdapter()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentStatisticsBinding.inflate(inflater , container, false)

        binding.statisticsRv.adapter = statAdapter

        val sinceDate = getSinceDate(30)
        val statList = setList(sinceDate)
        statAdapter.setList(statList)
        binding.numAll.text = "/${viewModel.getNumOfAllNotes(sinceDate)}"
        binding.numCompleted.text = viewModel.getNumOfAllCompletedNotes(sinceDate).toString()
        binding.rate.text = viewModel.getRate(sinceDate).toString()
        setProgress(sinceDate)



        binding.statisticsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.backBtn.setOnClickListener(){
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.statisticsRv.setHasFixedSize(true)



        binding.weeklyStatistics.setOnClickListener(){

            val sinceDate = getSinceDate(7)
            val statList = setList(sinceDate)
            statAdapter.setList(statList)
            binding.numAll.text = "/${viewModel.getNumOfAllNotes(sinceDate)}"
            binding.numCompleted.text = viewModel.getNumOfAllCompletedNotes(sinceDate).toString()
            binding.rate.text = viewModel.getRate(sinceDate).toString()
            setProgress(sinceDate)

            changeButtonColor(binding.weeklyStatistics)


    }
        binding.monthlyStatistics.setOnClickListener(){
            val sinceDate = getSinceDate(30)
            val statList = setList(sinceDate)
            statAdapter.setList(statList)

            binding.numAll.text = "/${viewModel.getNumOfAllNotes(sinceDate)}"
            binding.numCompleted.text = viewModel.getNumOfAllCompletedNotes(sinceDate).toString()
            binding.rate.text = viewModel.getRate(sinceDate).toString()
            setProgress(sinceDate)

            changeButtonColor(binding.monthlyStatistics)
        }

        binding.yearlyStatistics.setOnClickListener(){
            val sinceDate = getSinceDate(365)
            val statList = setList(sinceDate)
            statAdapter.setList(statList)

            binding.numAll.text = "/${viewModel.getNumOfAllNotes(sinceDate)}"
            binding.numCompleted.text = viewModel.getNumOfAllCompletedNotes(sinceDate).toString()
            binding.rate.text = viewModel.getRate(sinceDate).toString()
            setProgress(sinceDate)



            changeButtonColor(binding.yearlyStatistics)

        }

        binding.allStatistics.setOnClickListener(){
            val sinceDate = getSinceDate()
            val statList = setList(sinceDate)
            statAdapter.setList(statList)
            statAdapter.setList(statList)

            binding.numAll.text = "/${viewModel.getNumOfAllNotes(sinceDate)}"
            binding.numCompleted.text = viewModel.getNumOfAllCompletedNotes(sinceDate).toString()
            binding.rate.text = viewModel.getRate().toString()
            setProgress(sinceDate)

            changeButtonColor(binding.allStatistics)
        }


        return binding.root


    }

    private fun setProgress(sinceDate: CustomDate){
        val allStatus = listOf<String>("Hobby" , "Lifestyle" ,"Personal" , "Work", "No list")
        var view: ProgressBar = binding.hobbyProgressBar
        for (i in 0 until allStatus.size){
            when(allStatus[i]) {
                "Hobby" -> view = binding.hobbyProgressBar
                "Lifestyle" -> view = binding.lifestyleProgressBar
                "Personal" -> view = binding.personalProgressBar
                "Work" -> view= binding.workProgressBar
                "No list" -> view = binding.nolistProgressBar
            }
            view.max = viewModel.getNumOfStatusOccurence(allStatus[i] , sinceDate)
            view.progress = viewModel.getNumOfCompletedStatusOccurence(allStatus[i] , sinceDate)
            val layoutParams = view.layoutParams as ViewGroup.LayoutParams
            val dpValue = (370 * viewModel.getNumOfStatusOccurence(allStatus[i] , sinceDate)/(viewModel.getNumOfBiggestStatusOccurence()) )
            val density = resources.displayMetrics.density
            val pxValue = (dpValue * density).toInt()
            layoutParams.width = pxValue
            view.layoutParams = layoutParams
        }
    }
    private fun changeButtonColor(view : TextView){

        ViewCompat.setBackgroundTintList(binding.weeklyStatistics,
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey)))
        ViewCompat.setBackgroundTintList(binding.monthlyStatistics,
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey)))
        ViewCompat.setBackgroundTintList(binding.yearlyStatistics,
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey)))
        ViewCompat.setBackgroundTintList(binding.allStatistics,
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey)))
        ViewCompat.setBackgroundTintList(view,
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)))

    }

    private fun getSinceDate(num: Int = 10000): CustomDate {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -num)
        return CustomDate(
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR)
        )
    }

    private fun setList(sinceDate: CustomDate) : ArrayList<StatModel>{
        val statList = ArrayList<StatModel>()

        statList.add(StatModel("Hobby", viewModel.getNumOfCompletedStatusOccurence("Hobby" ,
            sinceDate) , viewModel.getNumOfStatusOccurence("Hobby" , sinceDate) , viewModel.getStatusRate("Hobby" , sinceDate) ))
        statList.add(StatModel("Personal", viewModel.getNumOfCompletedStatusOccurence("Personal", sinceDate) ,
            viewModel.getNumOfStatusOccurence("Personal", sinceDate) , viewModel.getStatusRate("Personal", sinceDate)))
        statList.add(StatModel("Lifestyle",viewModel.getNumOfCompletedStatusOccurence("Lifestyle", sinceDate) ,
            viewModel.getNumOfStatusOccurence("Lifestyle", sinceDate), viewModel.getStatusRate("Lifestyle", sinceDate)))
        statList.add(StatModel("Work", viewModel.getNumOfCompletedStatusOccurence("Work", sinceDate) ,
            viewModel.getNumOfStatusOccurence("Work", sinceDate) , viewModel.getStatusRate("Work", sinceDate)))
        statList.add(StatModel("No list", viewModel.getNumOfCompletedStatusOccurence("No list", sinceDate) ,
            viewModel.getNumOfStatusOccurence("No list", sinceDate) , viewModel.getStatusRate("No list", sinceDate)))
        return statList
    }

    companion object {

        @JvmStatic
        fun newInstance(): StatisticsFragment{
//            Main().apply {
//                arguments = Bundle().apply {
////                    putString(ARG_PARAM1, param1)
////                    putString(ARG_PARAM2, param2)
//                }
            val fragment = StatisticsFragment()
            return fragment
        }
    }
}