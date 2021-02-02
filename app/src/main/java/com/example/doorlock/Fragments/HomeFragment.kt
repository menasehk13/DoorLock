package com.example.doorlock.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.BatteryManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.example.doorlock.MainActivity
import com.example.doorlock.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.itsxtt.patternlock.PatternLockView
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
  lateinit var switchCompat: SwitchCompat;
  lateinit var doorstatus: TextView
  lateinit var batteryanddate:TextView
  lateinit var settingbutton:ImageButton
  lateinit var bottomshet:LinearLayout
  lateinit var patternLockView: PatternLockView
  lateinit var bottomSheetBehavior:BottomSheetBehavior<LinearLayout>
  lateinit var date:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        door_close()
        switchCompat=view.findViewById(R.id.switch_door_lock)
        bottomshet=view.findViewById(R.id.bottom_sheet)
        patternLockView=view.findViewById(R.id.pattern_lock)
        bottomSheetBehavior  = BottomSheetBehavior.from(bottomshet)
        bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.isDraggable=false
        bottomSheetBehavior.skipCollapsed=false
        switchCompat.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                bottomSheetBehavior.state=BottomSheetBehavior.STATE_EXPANDED
                switchCompat.isClickable=false
                door_open()

            } else {
                door_close()
            }
        }
    }

    private fun door_close() {
        val command = "c"
        try {
            ConnectBluetoothFragment.outputStream.write(command.toByteArray())
            Toast.makeText(requireContext(),"Door is Closed",Toast.LENGTH_LONG).show()
        }catch (e:IOException){
            e.printStackTrace()
            Log.e("ERROR", "door_close: ",e )
        }
    }

    private fun door_open() {
        patternLockView.setOnPatternListener(object : PatternLockView.OnPatternListener {
            override fun onComplete(ids: ArrayList<Int>): Boolean {
                val isCorrect = TextUtils.equals("012", getPatternString(ids))
                if (isCorrect) {
                    val command = "1"
                    switchCompat.isClickable = true
                    Toast.makeText(requireContext(), "Door is Open", Toast.LENGTH_LONG).show();
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    try {
                        //ConnectBluetoothFragment.outputStream.write(command.toByteArray())

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Log.e("ERROR", "onComplete: ",e )
                    }


                } else {
                    Toast.makeText(requireContext(), "Wrong Password", Toast.LENGTH_LONG).show();
                }

                return isCorrect
            }

            override fun onProgress(ids: ArrayList<Int>) {
                super.onProgress(ids)
            }

            override fun onStarted() {
                super.onStarted()
            }
        })
    }

    private fun getPatternString(ids: ArrayList<Int>):String {
     var result=""
        for (id in ids){
            result+=id.toString()
        }
        return result
    }

    companion object {

    }
}