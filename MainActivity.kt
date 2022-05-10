package com.example.a5kings

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.a5kings.databinding.ActivityMainBinding
import com.example.a5kings.databinding.NameBinding
import com.example.a5kings.databinding.ScoreBinding
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var p1Scores: List<EditText>
    private lateinit var p2Scores: List<EditText>
    private val xList = mutableListOf<Int>()
    private val yList = mutableListOf<Int>()
    private val lastX = mutableListOf<Int>()
    private val lastY = mutableListOf<Int>()
    private var player1 = ""
    private var player2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        p1Scores = listOf(
            binding.number1,
            binding.number2,
            binding.number3,
            binding.number4,
            binding.number5,
            binding.number6,
            binding.number7,
            binding.number8,
            binding.number9
        )
        p2Scores = listOf(
            binding.num1,
            binding.num2,
            binding.num3,
            binding.num4,
            binding.num5,
            binding.num6,
            binding.num7,
            binding.num8,
            binding.num9
        )

        val sum = View.OnClickListener {

            for (n in p1Scores) {
                xList.add(n.text.toString().toIntOrNull() ?: 0)
            }
            for (n in p2Scores) {
                yList.add(n.text.toString().toIntOrNull() ?: 0)
            }
            save()
            binding.textView.text = xList.sum().toString()
            binding.textView2.text = yList.sum().toString()
        }
        binding.sumButton.setOnClickListener(sum)

        val clear = View.OnClickListener {
            val reset = AlertDialog.Builder(binding.root.context)
            val clear = DialogInterface.OnClickListener { _, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    xList.clear()
                    yList.clear()
                    p1Scores.forEach { it.setText("") }
                    p2Scores.forEach { it.setText("") }
                    binding.editTextTextPersonName3.setText("")
                    binding.editTextTextPersonName4.setText("")
                    binding.textView.text = ""
                    binding.textView2.text = ""
                }
            }
            reset
                .setTitle(R.string.app_name)
                .setMessage(R.string.clear)
                .setIcon(R.mipmap.logo_foreground)
                .setPositiveButton(android.R.string.ok, clear)
                .setNegativeButton(android.R.string.cancel, clear)
                .show()
        }
        binding.clearButton.setOnClickListener(clear)

        val last = View.OnClickListener {
            readLast()
            val alert = AlertDialog.Builder(binding.root.context)
            val view = DialogInterface.OnClickListener { _, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    var i = 0
                    for (n in p1Scores) {
                        n.setText(lastX[i].toString())
                        i += 1
                    }
                    i = 0
                    for (n in p2Scores) {
                        n.setText(lastY[i].toString())
                        i += 1
                    }
                }
            }
            alert
                .setTitle(R.string.app_name)
                .setMessage(R.string.view)
                .setIcon(R.mipmap.logo_foreground)
                .setPositiveButton(android.R.string.ok, view)
                .setNegativeButton(android.R.string.cancel, view)
                .show()
        }
        binding.lastButton.setOnClickListener(last)

        val start = View.OnClickListener {

            val alert = AlertDialog.Builder(binding.root.context)
            val view = DialogInterface.OnClickListener { _, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    clear()
                    setPlayers()
                }
            }
            alert
                .setTitle(R.string.app_name)
                .setMessage(R.string.view)
                .setIcon(R.mipmap.logo_foreground)
                .setPositiveButton(android.R.string.ok, view)
                .setNegativeButton(android.R.string.cancel, view)
                .show()
        }
        binding.startButton.setOnClickListener(start)
    }

    override fun onStart() {
        super.onStart()
        val video = binding.videoView
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.smoke )
        video.setVideoURI(videoUri)
        video.start()
        video.setOnCompletionListener {
            video.start()
        }
    }

    private fun clear(){
        xList.clear()
        yList.clear()
        p1Scores.forEach { it.setText("") }
        p2Scores.forEach { it.setText("") }
        binding.editTextTextPersonName3.setText("")
        binding.editTextTextPersonName4.setText("")
        binding.textView.text = ""
        binding.textView2.text = ""
    }

    private fun setPlayers() {
        val players = listOf(binding.editTextTextPersonName3, binding.editTextTextPersonName4)
        val player1Alert = AlertDialog.Builder(binding.root.context)
        val p1dialog = NameBinding.inflate(layoutInflater)
        val p1Listener = DialogInterface.OnClickListener { _, _ ->
            val p1 = p1dialog.name.text.toString()
            players[0].setText(p1)
            player1 = p1
            val player2Alert = AlertDialog.Builder(binding.root.context)
            val p2dialog = NameBinding.inflate(layoutInflater)
            val p2Listener = DialogInterface.OnClickListener { _, _ ->
                val p2 = p2dialog.name.text.toString()
                players[1].setText(p2)
                player2 = p2
            }
            player2Alert
                .setTitle(R.string.app_name)
                .setView(p2dialog.root)
                .setMessage(resources.getString(R.string.player, 2))
                .setIcon(R.mipmap.logo_foreground)
                .setPositiveButton(android.R.string.ok, p2Listener)
                .setNegativeButton(android.R.string.cancel, p2Listener)
                .show()
        }
        player1Alert
            .setTitle(R.string.app_name)
            .setView(p1dialog.root)
            .setMessage(resources.getString(R.string.player, 1))
            .setIcon(R.mipmap.logo_foreground)
            .setPositiveButton(android.R.string.ok, p1Listener)
            .setNegativeButton(android.R.string.cancel, p1Listener)
            .show()
    }

    private fun save() {
        openFileOutput("x.txt", MODE_PRIVATE).bufferedWriter().use {
            it.write("${binding.editTextTextPersonName3.text}\n")
            for (x in xList) {
                it.appendLine("$x")
            }
        }

        openFileOutput("y.txt", MODE_PRIVATE).bufferedWriter().use {
            it.write("${binding.editTextTextPersonName4.text}\n")
            for (y in yList) {
                it.appendLine("$y")
            }
        }
    }

    private fun readLast() {
        try {
            openFileInput("x.txt").bufferedReader().useLines {
                var i = 0
                for (line in it) {
                    if (i == 0) {
                        binding.editTextTextPersonName3.setText(line)
                    } else {
                        lastX.add(line.toInt())
                    }
                    i += 1
                }
            }
            try {
                openFileInput("y.txt").bufferedReader().useLines {
                    var i = 0
                    for (line in it) {
                        if (i == 0) {
                            binding.editTextTextPersonName4.setText(line)
                        } else {
                            lastY.add(line.toInt())
                        }
                        i += 1
                    }
                }
            } catch (e: FileNotFoundException) {
                binding.editTextTextPersonName4.setText("missing file")
            }
        } catch (e: FileNotFoundException) {
            binding.editTextTextPersonName3.setText("missing file")
        }
    }
}