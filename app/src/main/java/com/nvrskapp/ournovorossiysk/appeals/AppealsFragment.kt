package com.nvrskapp.ournovorossiysk.appeals

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nvrskapp.ournovorossiysk.Problem
import com.nvrskapp.ournovorossiysk.R
import com.nvrskapp.ournovorossiysk.databinding.FragmentAppealsBinding
import kotlinx.android.synthetic.main.fragment_appeals.*
import java.io.IOException


class AppealsFragment : Fragment() {

    private lateinit var googleMap: GoogleMap
    private lateinit var firebase: Firebase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val binding = DataBindingUtil.inflate<FragmentAppealsBinding>(
            inflater,
            R.layout.fragment_appeals,
            container,
            false
        ).apply {
            val firebase = Firebase.database
            databaseReference = firebase.getReference("problems")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(p0: GoogleMap?) {
                //TODO("Not yet implemented")
                val nvrs: LatLng = LatLng(44.715224, 37.762172)
                p0?.moveCamera(CameraUpdateFactory.newLatLngZoom(nvrs, 9f))

                databaseReference?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p1: DataSnapshot) {
                        for (s in p1.children) {
                            var problem = s.getValue(Problem::class.java)

                            var geocoder = Geocoder(context)
                            try {
                                var coords: List<Address> =
                                    geocoder.getFromLocationName(problem?.address, 1)
                                if (coords.size > 0) {
                                    var pin: LatLng =
                                        LatLng(coords.get(0).latitude, coords.get(0).longitude)
                                    p0?.addMarker(MarkerOptions().position(pin).title(problem?.description))

                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            }
        })
        floating_action_button.setOnClickListener{
            findNavController().navigate(R.id.action_appealsFragment_to_apealsFormFragment)
        }
    }
}
