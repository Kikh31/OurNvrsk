package com.nvrskapp.ournovorossiysk.appeals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nvrskapp.ournovorossiysk.Problem
import com.nvrskapp.ournovorossiysk.R
import com.nvrskapp.ournovorossiysk.databinding.FragmentApealsFormBinding

class ApealsFormFragment : Fragment() {
    private lateinit var firebase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val binding = DataBindingUtil.inflate<FragmentApealsFormBinding>(
            inflater,
            R.layout.fragment_apeals_form,
            container,
            false
        ).apply {
            val firebase = Firebase.database
            databaseReference = firebase.getReference("problems")
            problemButton.setOnClickListener {
                val title: String = titleEditText.text.toString()
                val address: String = "Новороссийск, " + addresEditText.text.toString() + ", " + hmNumberEditTExt.text.toString()
                val description: String = descriptionEditText.text.toString()

                val problem: Problem = Problem(title, address, "", description)
                databaseReference.push().setValue(problem)

                findNavController().navigate(R.id.action_apealsFormFragment_to_appealsFragment)
            }
        }
        return binding.root
    }
}