package com.nvrskapp.ournovorossiysk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nvrskapp.ournovorossiysk.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private  lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
                .apply {
                    navController =
                        Navigation.findNavController(this@MainActivity, R.id.nav_host)
                    bottomNav.setupWithNavController(navController)
                    val appBarConfiguration = AppBarConfiguration(
                        topLevelDestinationIds = setOf(
                            R.id.newsFragment,
                            R.id.appealsFragment
                        )
                    )
                    setupActionBarWithNavController(navController, appBarConfiguration)

                    navController.addOnDestinationChangedListener { controller, destination, arguments ->
                        if (destination.id == R.id.apealsFormFragment) {
                            bottom_nav.visibility = View.INVISIBLE
                        } else {
                            bottom_nav.visibility = View.VISIBLE
                        }
                    }
                }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()

    }
}