package com.entertainment.medkit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.entertainment.medkit.data.Userdata
import com.entertainment.medkit.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.register.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        val name = binding.name.text.toString()
        val email = binding.name.text.toString()
        val adress = binding.adress.text.toString()
        val password = binding.password.text.toString()
        val phone = binding.phone.text.toString()
        if(name.isNotEmpty() && email.isNotEmpty() && adress.isNotEmpty() && password.isNotEmpty() && phone.isNotEmpty()){
            database = FirebaseDatabase.getInstance().getReference("Users")
            val user = Userdata(name,phone,email,password,adress)
            database.child(name).setValue(user).addOnSuccessListener {
                binding.name.text!!.clear()
                binding.email.text!!.clear()
                binding.adress.text!!.clear()
                binding.phone.text!!.clear()
                binding.password.text!!.clear()
            }.addOnFailureListener {
                Toast.makeText(this,"Something went wrong kindly check your internet connection",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Filling all fields is mendatory",Toast.LENGTH_SHORT).show()
        }
    }
}