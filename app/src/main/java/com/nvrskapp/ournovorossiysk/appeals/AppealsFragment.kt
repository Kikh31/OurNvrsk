package com.nvrskapp.ournovorossiysk.appeals

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nvrskapp.ournovorossiysk.Problem
import com.nvrskapp.ournovorossiysk.R
import com.nvrskapp.ournovorossiysk.databinding.FragmentAppealsBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.apeal_info_window.*
import kotlinx.android.synthetic.main.apeal_info_window.view.*
import kotlinx.android.synthetic.main.fragment_appeals.*
import java.io.IOException


class AppealsFragment : Fragment() {

    private lateinit var googleMap: GoogleMap
    private lateinit var firebase: FirebaseDatabase
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
            databaseReference = Firebase.database.getReference("problems")
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

                                    p0?.addMarker(
                                        MarkerOptions()
                                            .position(pin)
                                            .title(problem?.title)
                                            .snippet(problem?.description)
                                    )?.tag = problem?.image + ";" + problem?.address
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
                p0?.setOnInfoWindowClickListener {
                    val tagString = it.tag.toString().split(';')
                    val view = LayoutInflater.from(context)
                        .inflate(R.layout.apeal_info_window, null, false)
                    MaterialAlertDialogBuilder(context!!).apply {
                        setView(view)
                        view.title.setText(it.title)
                        view.apealTextView.setText(it.snippet + "\n")
                        view.address.setText(tagString[1])
                        Picasso.get().load(tagString[0]).into(view.apeal_image)
                    }.show()
                }
            }
        })
        floating_action_button.setOnClickListener{
            findNavController().navigate(R.id.action_appealsFragment_to_apealsFormFragment)
        }
    }
}
