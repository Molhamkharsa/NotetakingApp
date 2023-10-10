package com.example.notetakingapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notetakingapp.adapter.NoteAdapter
import com.example.notetakingapp.databinding.FragmentHomeBinding
import com.example.notetakingapp.model.Note
import com.example.notetakingapp.viewmodel.NoteViewModel


class HomeFragment : Fragment(R.layout.fragment_home) , SearchView.OnQueryTextListener{
    //private var _binding : FragmentHomeBinding? = null
    //private val binding get() = _binding!!
    private lateinit var binding: FragmentHomeBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //_binding = FragmentHomeBinding.inflate(inflater,container,false)
        //return binding.root
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        noteViewModel = (activity as MainActivity).noteViewModel
        binding.btnAddNote.setOnClickListener {
            it.findNavController().navigate(
                R.id.action_homeFragment_to_newNoteFragment
            )
        }
        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        noteAdapter = NoteAdapter()
        binding.recycleView.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = noteAdapter
        }
        activity?.let {
            noteViewModel.getAllNotes().observe(
                viewLifecycleOwner
            ) { note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }

    }

    private fun updateUI(note: List<Note>?) {
        if (note != null) {
            if (note.isEmpty()){
                binding.cardView.visibility = View.GONE
                binding.recycleView.visibility = View.VISIBLE
            }else{
                binding.cardView.visibility = View.VISIBLE
                binding.recycleView.visibility = View.GONE
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.home_menu,menu)
        val menuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        //searchNote(query)
        return false
    }

    private fun searchNote(query: String?) {
        val searchQuery = "%$query"
        noteViewModel.searchNote(searchQuery).observe(
            this
        ) { list -> noteAdapter.differ.submitList(list) }

    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!= null){
            searchNote(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

    }


}