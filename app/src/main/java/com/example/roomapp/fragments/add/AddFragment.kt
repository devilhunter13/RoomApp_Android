package com.example.roomapp.fragments.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.roomapp.R
import com.example.roomapp.model.User
import com.example.roomapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*


class AddFragment : Fragment() {

    val imageRequestCode=10
    var tempURL="empty"

    private lateinit var mUserViewModel: UserViewModel
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK && requestCode==imageRequestCode){

            addImage.setImageURI(data?.data)
            tempURL=data?.data.toString()
            requireContext().contentResolver.takePersistableUriPermission(data?.data!!,Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.add_btn.setOnClickListener {
            insertDataToDatabase()
        }
        view.btn_add_img.setOnClickListener {
            image_layout.visibility=View.VISIBLE

            val intent=Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type="image/*"

            startActivityForResult(intent, imageRequestCode)
            Toast.makeText(requireContext(), "Работает", Toast.LENGTH_SHORT).show()
        }
        view.btn_img_del.setOnClickListener {
            image_layout.visibility=View.GONE
            Toast.makeText(requireContext(), "О бозе! и это работает!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun insertDataToDatabase() {
        val firstName = addFirstName_et.text.toString()
        val lastName = addLastName_et.text.toString()
        val about = addAbout.text.toString()
       // val url=addImage.toString()

        if(inputCheck(firstName, lastName, about)){
            // Create User Object

            val user = User(0, firstName, lastName, about, tempURL)
            // Add Data to Database
            mUserViewModel.addUser(user)

            Toast.makeText(requireContext(), "Данные добавлены", Toast.LENGTH_LONG).show()
            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)

        }else{
            Toast.makeText(requireContext(), "Пожалуйста заполните все поля", Toast.LENGTH_LONG).show()
        }
    }
    private fun inputCheck(firstName: String, lastName: String, about: String): Boolean{
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && TextUtils.isEmpty(
            about
        ) )
    }
}