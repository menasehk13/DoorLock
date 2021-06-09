package com.example.doorlock.Fragments

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.doorlock.R
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

lateinit var bluetoothAdapter:BluetoothAdapter
lateinit var socket: BluetoothSocket


lateinit var handler:Handler
class ConnectBluetoothFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_connect_bluetooth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled){
            val intent=Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent,0)
        }
        val bluetoothDevice=bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS)
        val uuid=bluetoothDevice.uuids[0].uuid
        var sock: BluetoothSocket? = null
        try {
            sock=bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid)
        }catch (e:IOException){
            e.printStackTrace()
        }
        socket=sock!!

        Timer().schedule(object :TimerTask(){
            override fun run() {
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                bluetoothAdapter.cancelDiscovery()
                        try {
                            socket.connect()
                            findNavController().navigate(R.id.action_connectBluetoothFragment_to_homeFragment)
                            Log.e("Status", "Connected ")
                        } catch (socketError: IOException) {
                            socketError.printStackTrace()
                            Log.e("Status", "Error ", socketError)
                            try {
                                socket.close()
                                Log.e("Status", "Closed")
                            } catch (ErrorSocket: IOException) {
                                ErrorSocket.printStackTrace()
                                Log.e("Status", "Error ", ErrorSocket)
                            }
                        }

            }
        },2000)
        try {
            inputStream= socket.inputStream

            outputStream=socket.outputStream
        }catch(e:IOException){
            e.printStackTrace()
        }
    }
  companion object{
      lateinit var inputStream:InputStream
      lateinit var outputStream: OutputStream
      const val DEVICE_ADDRESS:String="70:26:05:B2:A7:B9"// the doorlock bluetooth address
  }
}