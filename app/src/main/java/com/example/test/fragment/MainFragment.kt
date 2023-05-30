package com.example.test

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.adapter.MainAdapter
import com.example.test.adapter.TopAdapter
import com.example.test.databinding.FragmentMainBinding
import com.example.test.dialog.InputDialog
import com.example.test.dialog.MenuDialog
import com.example.test.fragment.SearchFragment
import com.example.test.fragment.SettingsFragment
import com.example.test.fragment.StatisticsFragment
import com.example.test.model.CustomDate
import com.example.test.model.DateModel
import com.example.test.model.NoteModel
import com.example.test.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalTime
import java.util.Calendar

class MainFragment : Fragment() {
    private val viewModel: MyViewModel by activityViewModels()
    private val mainAdapter = MainAdapter()  // for central recycler view
    private val adapter = TopAdapter() //for horizontal recycler view
    lateinit var dates : ArrayList<DateModel>
    lateinit var binding: FragmentMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getDbData()

    }



//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewModel.noteList.observe(viewLifecycleOwner , Observer {
//            viewModel.unselectAll()
//
//        })
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        mainAdapter.setOnCheckboxClick() { pos: Int ->
            viewModel.changeIsChecked(pos)
        }
        mainAdapter.setOnLongClick() { pos: Int ->
            viewModel.changeIsSelected(pos)
        }
        mainAdapter.setOnClick(){ pos: Int ->
            showCustomizeMenu(pos)
        }

        adapter.onDateClickListener(){ pos:Int ->
            for(i in 0 until dates.size){
                dates[i].isClicked =false
            }
            dates[pos].isClicked = true
//        Toast.makeText(requireContext() , "${viewModel.getIndexOf(dates[pos].day )}", Toast.LENGTH_SHORT).show()
            binding.mainRecyclerView.scrollToPosition(viewModel.getIndexOf(dates[pos].day))
            adapter.setList(dates)
        }

        binding =  FragmentMainBinding.inflate(inflater , container, false)
        dates = getCurrentDatesList()
        adapter.setList(dates)

        //To ensure the RecyclerView is ready before scrolling

        binding.topRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Scroll to the desired position
                binding.topRecyclerView.scrollToPosition((dates.size / 2)+3)

                // Remove the listener to avoid multiple calls
                binding.topRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        binding.topRecyclerView.adapter = adapter
        binding.topRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val now = LocalTime.now()
        if (now.hour>= 6 && now.hour<12 ){
            binding.greeting.setText(R.string.good_morning)
        } else if( now.hour>= 12 && now.hour< 18 ) {
            binding.greeting.setText(R.string.good_afternoon)
        } else if(now.hour>= 18 && now.hour< 23)
        {
            binding.greeting.setText(R.string.good_evening)
        }else{
            binding.greeting.setText(R.string.good_night)
        }
        binding.saveBtn.setOnClickListener(){
            showInputDialog()
        }
        binding.menu.setOnClickListener(){
            showMenu()
        }
        binding.search.setOnClickListener(){
            val fragment = SearchFragment()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_cont , fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.deleteBtn.setOnClickListener(){
            showOnDeleteDialog()

        }

        viewModel.noteList.observe(viewLifecycleOwner , Observer {

                var list = viewModel.getValue()!!
                mainAdapter.setList(list)
                mainAdapter.setSelected(viewModel.getNumSelected())
                if(viewModel.getNumSelected()>0){
                    binding.deleteBtn.visibility = View.VISIBLE
                }else{
                    binding.deleteBtn.visibility = View.GONE
                }
        })


        binding.mainRecyclerView.adapter = mainAdapter
        binding.mainRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }

    fun showOnDeleteDialog(){
        val listener = DialogInterface.OnClickListener { _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> viewModel.deleteSelected()
                DialogInterface.BUTTON_NEGATIVE -> Unit
            }


        }
        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setMessage("Do you really want to delete?")
            .setPositiveButton("Yes", listener)
            .setNegativeButton("No", listener)
            .create()

        dialog.show()

    }

    fun showCustomizeMenu(pos: Int){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_note_layout)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.show()

        var task = dialog.findViewById<EditText>(R.id.task_et)
        task.setText(viewModel.getTask(pos))
        var description = dialog.findViewById<EditText>(R.id.description_et)

        description.setText("${viewModel.getDescription(pos)}")





        val save_btn = dialog.findViewById<Button>(R.id.save)
        save_btn.setOnClickListener(){
            var note = viewModel.getValue()?.get(pos)
            note?.description = description.text.toString()
            note?.task = task.text.toString()
            if (note != null) {
                viewModel.updateNote( note)
            }
            dialog?.dismiss()
        }

    }



    private fun showMenu() {
        val dialog: MenuDialog? = context?.let { MenuDialog(it) }
        val hobby_tasks = dialog?.findViewById<TextView>(R.id.hobby_tasks)
        val personal_tasks = dialog?.findViewById<TextView>(R.id.personal_tasks)
        val lifestyle_tasks = dialog?.findViewById<TextView>(R.id.lifeStyle_tasks)
        val work_tasks = dialog?.findViewById<TextView>(R.id.work_tasks)
        val nolist_tasks = dialog?.findViewById<TextView>(R.id.nolist_tasks)

        personal_tasks?.text = "${viewModel.getNumOfStatusOccurence("Personal").toString()} Tasks"
        hobby_tasks?.text = "${viewModel.getNumOfStatusOccurence("Hobby").toString()} Tasks"
        lifestyle_tasks?.text = "${viewModel.getNumOfStatusOccurence("Lifestyle").toString()} Tasks"
        work_tasks?.text = "${viewModel.getNumOfStatusOccurence("Work").toString()} Tasks"
        nolist_tasks?.text = "${viewModel.getNumOfStatusOccurence("No list").toString()} Tasks"


        dialog?.show()

        val statistics = dialog?.findViewById<LinearLayout>(R.id.statistics)
        statistics?.setOnClickListener() {
            val fragment = StatisticsFragment()
            parentFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_cont, fragment)
                .commit()

            dialog?.dismiss()
        }

        val settings = dialog?.findViewById<LinearLayout>(R.id.settings)
        settings?.setOnClickListener(){
            val fragment = SettingsFragment.newInstance()
            parentFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_cont, fragment)
                .commit()
            dialog?.dismiss()
        }
    }

    private fun showInputDialog(){
        val dialog: InputDialog? = context?.let { InputDialog(it) }
        dialog?.show()

        val editText = dialog?.findViewById<EditText>(R.id.task_et)
        editText?.requestFocus()
        val saveBtn =dialog?.findViewById<Button>(R.id.save)
        val choose_status_btn = dialog?.findViewById<Button>(R.id.choose_status)
        val choose_date_btn = dialog?.findViewById<Button>(R.id.choose_date)
        val date = Calendar.getInstance()
        val day = date.get(Calendar.DAY_OF_MONTH)
        val month = date.get(Calendar.MONTH)
        val year = date.get(Calendar.YEAR)
        var customDate = CustomDate(day, month, year)

        saveBtn?.setOnClickListener(){
            editText?.clearFocus()
            val status = choose_status_btn?.text.toString()
            val task = editText?.text.toString()
            date.set(customDate.year ,customDate.month, customDate.day)
            viewModel.addNoteToList(NoteModel(customDate, matchDayOfWeekFull(date.get(Calendar.DAY_OF_WEEK)), task , status))
            dialog?.dismiss()
        }

        choose_status_btn?.setCompoundDrawablesRelativeWithIntrinsicBounds( ContextCompat.getDrawable(requireContext(), R.drawable.status_nolist),null,null,null)
        var pos = 0
        choose_status_btn?.setOnClickListener(){

            val allStatus = listOf<String>("Hobby" , "Lifestyle" ,"Personal" , "Work", "No list")
            val allIcon = listOf<Drawable?>(
                ContextCompat.getDrawable(requireContext(), R.drawable.status_hobby_red),
                ContextCompat.getDrawable(requireContext(), R.drawable.status_lifestyle_green),
                ContextCompat.getDrawable(requireContext(), R.drawable.status_personal_blue),
                ContextCompat.getDrawable(requireContext(), R.drawable.status_work_orange),
                ContextCompat.getDrawable(requireContext(), R.drawable.status_nolist))

            choose_status_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(allIcon[pos%5] ,null,null,null)
            choose_status_btn.text = allStatus[pos%5]
            pos++
        }

        choose_date_btn?.setOnClickListener(){
            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                customDate = CustomDate(mDay , mMonth , mYear)
                val calendar = Calendar.getInstance()
                date.set(mYear ,mMonth, mDay)
                choose_date_btn.text = "${mDay} ${matchDayOfWeekFull(date.get(Calendar.DAY_OF_WEEK))}"
            },year , month , day )
            dpd.show()
        }




// Obtain the InputMethodManager
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

// Show the keyboard for the EditText field
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }


    private fun matchDayOfWeek(num:Int) :String{
        return when(num){
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY-> "Wed"
            Calendar.THURSDAY-> "Thu"
            Calendar.FRIDAY -> "Fri"
            Calendar.SATURDAY -> "Sat"
            Calendar.SUNDAY -> "Sun"
            else -> "Null"
        }
    }
    private fun matchDayOfWeekFull(num:Int) :String{
        return when(num){
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY-> "Wednesday"
            Calendar.THURSDAY-> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            Calendar.SUNDAY -> "Sunday"
            else -> "Null"
        }
    }

    private fun getCurrentDatesList() : ArrayList<DateModel>{
        var dates = ArrayList<DateModel>()
        val date = Calendar.getInstance()
        val today = DateModel(date.get(Calendar.DAY_OF_MONTH), matchDayOfWeek(date.get(Calendar.DAY_OF_WEEK)))
        date.add(Calendar.DAY_OF_YEAR, -15)
        for(i in 1 until 30){

            date.add(Calendar.DAY_OF_YEAR, 1)
            val day = date.get(Calendar.DAY_OF_MONTH)
            val dayOfWeek = matchDayOfWeek(date.get(Calendar.DAY_OF_WEEK))
            val dateModel = DateModel(day,dayOfWeek )
            if (dateModel == today){
                dateModel.isClicked = true
            }
            dates.add(dateModel)
        }
        return dates
    }





    companion object {

        fun newInstance(): MainFragment{
            return MainFragment()
        }
    }

}
