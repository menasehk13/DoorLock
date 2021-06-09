package com.example.doorlock

import android.content.Context
import android.content.SharedPreferences
import java.util.prefs.Preferences

public class PrefClass{
  lateinit var context:Context

    var DEFAULT : String="0000"
    var SAVE_ITEM :String="Save_File"
    var Key_File:String="Key_Patteren"
    var sharedPref:SharedPreferences=context.getSharedPreferences(SAVE_ITEM,Context.MODE_PRIVATE)
    public fun Save_Patteren(newPatteren :String){
        val editor :SharedPreferences.Editor=sharedPref.edit()
        editor.putString(Key_File,newPatteren)
        editor.apply()
    }

    public fun getPattern():String{
        val key:String=sharedPref.getString(Key_File,DEFAULT).toString()
        return key;
    }
}