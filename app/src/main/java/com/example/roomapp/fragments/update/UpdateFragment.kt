package com.example.roomapp.fragments.update

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.roomapp.R
import com.example.roomapp.model.User
import com.example.roomapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*

class UpdateFragment : Fragment() {
    private  val args: UpdateFragmentArgs by navArgs<UpdateFragmentArgs>()
    private lateinit var mUserViewModel:UserViewModel
    val imageRequestCode2=20
    var tempURL2="empty"

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK && requestCode==imageRequestCode2){

            update_addImage.setImageURI(data?.data)
            tempURL2=data?.data.toString()
            requireContext().contentResolver.takePersistableUriPermission(data?.data!!,Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_update, container, false)
        mUserViewModel=ViewModelProvider(this).get(UserViewModel::class.java)

        view.UpdateFirstName_et.setText(args.currentUser.firstName)
        view.UpdateLastName_et.setText(args.currentUser.lastName)
        view.UpdateAbout_et.setText(args.currentUser.about)
        if(args.currentUser.url!="empty") {
            view.update_addImage.setImageURI(Uri.parse(args.currentUser.url))
            tempURL2 = args.currentUser.url
        }
        else {
                view.update_image_layout.visibility=View.GONE
        }

        view.update_btn.setOnClickListener{
            updateItem()

        }
        view.update_btn_add_img.setOnClickListener {
            update_image_layout.visibility=View.VISIBLE

            val intent=Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type="image/*"
            startActivityForResult(intent,imageRequestCode2)
            Toast.makeText(requireContext(), "Работает", Toast.LENGTH_SHORT).show()
        }
        view.update_btn_img_del.setOnClickListener {
            update_image_layout.visibility=View.GONE
            tempURL2="empty"
            Toast.makeText(requireContext(), "О бозе! и это работает!", Toast.LENGTH_SHORT).show()
        }
        //добавить кнопку удалить
        setHasOptionsMenu(true)
        return  view
    }

    private  fun updateItem(){
        val firstName=UpdateFirstName_et.text.toString()
        val lastName=UpdateLastName_et.text.toString()
        val about=UpdateAbout_et.text.toString()
       // val url=addImage.toString()
        if(inputCheck(firstName, lastName, about)){
            val updatedUser= User(args.currentUser.id,firstName,lastName, about,tempURL2)

            mUserViewModel.updateUser(updatedUser)

            Toast.makeText(requireContext(), "Обновление данных успешно", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        else{
            Toast.makeText(requireContext(), "Обновление завершилось ошибкой", Toast.LENGTH_SHORT).show()
        }
    }
    private fun inputCheck(firstName: String, lastName: String, about:String): Boolean{
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && TextUtils.isEmpty(about))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.delete_menu,menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.menu_delete){
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

  fun deleteUser(){
        val builder=AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Да"){_,_->
        mUserViewModel.deleteUser(args.currentUser)
        Toast.makeText(
            requireContext(),
            "Успешно удалено: ${args.currentUser.firstName}",
            Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        builder.setNegativeButton("Нет"){_,_->}
        builder.setTitle("Удалить ${args.currentUser.firstName}?")
        builder.setMessage("Вы действительно хотите удалить ${args.currentUser.firstName}?")
        builder.create().show()
    }

}

