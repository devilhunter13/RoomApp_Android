package com.example.roomapp.fragments.list
import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import coil.load
import com.example.roomapp.R
import com.example.roomapp.fragments.update.UpdateFragment
import com.example.roomapp.model.User
import kotlinx.android.synthetic.main.custom_row.view.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class ListAdapter:RecyclerView.Adapter<ListAdapter.MyViewHolder>(){

    var userList= emptyList<User>()



    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row,parent,false))
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=userList[position]
        holder.itemView.id_txt.text=(1+position).toString()
        //holder.itemView.imageView.load(currentItem.profilePhoto)
        holder.itemView.firstName_txt.text=currentItem.firstName
        holder.itemView.lastName_txt.text=currentItem.lastName
        if(currentItem.url!="empty")
        holder.itemView.avatars_img.setImageURI( Uri.parse(currentItem.url))
        holder.itemView.rowLayout.setOnClickListener{
            val action=ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }


    override fun getItemCount(): Int {
        return userList.size
    }
    fun setData(user:List<User>){
        this.userList=user


        notifyDataSetChanged()

    }

}

