package com.example.test.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.adapter.SearchListAdapter
import com.example.test.databinding.FragmentSearchBinding
import com.example.test.model.NoteModel
import com.example.test.viewmodel.MyViewModel

class SearchFragment : Fragment() {
    private val viewModel: MyViewModel by activityViewModels()
    private val adapter = SearchListAdapter()
    lateinit var binding: FragmentSearchBinding
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater , container , false)
        binding.searchRecyclerView.adapter = adapter
        binding.searchRecyclerView.layoutManager =  LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.backBtn.setOnClickListener(){
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        var search =binding.searchView
        search.queryHint = " Search task"
        search.setIconifiedByDefault(false)
        search.setIconified(false)
        search.setQuery("", false)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        return binding.root
    }

    fun filterList(query :String?){
        var filteredList = ArrayList<NoteModel>()
        if (query!= null){
            var list = viewModel.noteList.value
            for(item in list!!){
                if (item.task.contains(query) || item.task.contains(query)){
                    filteredList.add(item)
                }
            }
        }

        if (filteredList != null){
            adapter.setList(filteredList)
        }
    }
    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}