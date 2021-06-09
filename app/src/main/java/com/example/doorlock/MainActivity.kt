package com.example.doorlock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.doorlock.R.id.nav_controler
import com.example.doorlock.R.id.settingFragment

class MainActivity : AppCompatActivity() {
    var id:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController:NavController=Navigation.findNavController(this,R.id.nav_controler)
      navController.addOnDestinationChangedListener { controller, destination, arguments ->
          when(destination.id){
              R.id.action_homeFragment_to_settingFragment->{
                  supportActionBar?.show()
              }
          }

      }
    }
}