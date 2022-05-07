package com.example.parstagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.parstagram.fragments.ComposeFragment
import com.example.parstagram.fragments.FeedFragment
import com.example.parstagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File

/**
 * Let user create a post by taking a photo with their camera
 */

class MainActivity : AppCompatActivity() {

    //lateinit var swipeContainer: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            item ->
            var fragmentToShow: Fragment? = null

            when(item.itemId){
                R.id.action_home -> {
                    fragmentToShow = FeedFragment()
                    //Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                }
                R.id.action_compose -> {
                    fragmentToShow = ComposeFragment()
                    //Toast.makeText(this, "Compose", Toast.LENGTH_SHORT).show()
                }
                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                    //Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                }
            }

            if(fragmentToShow != null){
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }

            //Returning true signifies that we have handled this user interaction on the item
            true
        }

        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home
        //queryPosts()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}