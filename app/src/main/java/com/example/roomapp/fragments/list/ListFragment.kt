package com.example.roomapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.system.Os.remove
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomapp.MainActivity
import com.example.roomapp.R
import com.example.roomapp.fragments.update.UpdateFragment

import com.example.roomapp.model.User
import com.example.roomapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.custom_row.*
import kotlinx.android.synthetic.main.custom_row.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private  lateinit var mUserViewModel: UserViewModel
    private val adapter=ListAdapter()
    private lateinit var Listtt:List<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)


        val recyclerView=view.recyclerview
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(requireContext())


        mUserViewModel=ViewModelProvider(this).get(UserViewModel::class.java)
        mUserViewModel.readData.observe(viewLifecycleOwner, Observer { user->
            adapter.setData(user)
        })
        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        setHasOptionsMenu(true)


        val value = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showDialog(viewHolder)
            }
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
        val swap=itemSwipe()
        swap.attachToRecyclerView(recyclerview)

        initSearchView()
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.menu_delete) {
            deleteAllUsers()

        }
        return super.onOptionsItemSelected(item)
    }
    private fun deleteAllUsers(){
        val builder= AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_->
            mUserViewModel.deleteAllUsers()
            Toast.makeText(
                requireContext(),
                "Successfully removed everything",
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

        }

        builder.setNegativeButton("No"){_,_->}
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure want to delete everything?")
        builder.create().show()
    }

    private fun itemSwipe():ItemTouchHelper{
        return ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showDialog(viewHolder)
            }
        } )
    }
   private fun showDialog(viewHolder: RecyclerView.ViewHolder) {
        val builder=AlertDialog.Builder(activity)
        builder.setTitle("Удалить запись")
        builder.setMessage("Удалить ${viewHolder.itemView.firstName_txt.text} ${viewHolder.itemView.lastName_txt.text}?")
        builder.setPositiveButton("Да"){dialog,which->
            val pos=viewHolder.adapterPosition
           remove(pos)
            adapter.notifyItemChanged(pos)
        }
        builder.setNegativeButton("Нет"){dialog,which->
            val pos=viewHolder.adapterPosition
            adapter.notifyItemChanged(pos)

        }
        builder.show()
    }

fun initSearchView(){
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(p0: String?): Boolean {
            TODO("Not yet implemented")
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            Log.d("MyLog", "New Text: $p0")
            if (p0!=null){
                searchDatabase(p0)
            }

            return true
        }

    })
}

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        mUserViewModel.searchDatabase(searchQuery).observe(this, { list ->
            list.let {
               adapter.setData(it)
            }
        })
    }
    fun remove(pos: Int) {
       val item = adapter.userList[pos]
       val itemname=item.firstName.toString()
        Log.d("MMM","$itemname")
        (adapter.userList as MutableList).remove(item)
        adapter.notifyItemChanged(pos)
        mUserViewModel.deleteUser(item)

    }
    }
