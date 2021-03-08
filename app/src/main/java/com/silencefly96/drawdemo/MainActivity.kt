package com.silencefly96.drawdemo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.silencefly96.drawdemo.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //清除画板
        binding.clear.setOnClickListener {
            binding.drawView.clear()
        }

        //导出图像
        binding.output.setOnClickListener{
            //动态申请外部权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                outputImage()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }

        //前进一步
        binding.forward.setOnClickListener {
            binding.drawView.forward()
        }

        //回退一步
        binding.back.setOnClickListener {
            binding.drawView.back()
        }

        //设置笔触粗细
        binding.setWidth.setOnClickListener {
            binding.drawView.mPaintWidth = binding.width.text.toString().toFloat()
        }

        //设置笔触颜色
        binding.setStroke.setOnClickListener {
            binding.drawView.mPaintColor = Color.parseColor(binding.strokeColor.text.toString())
        }

        //设置背景颜色
        binding.setCanvasColor.setOnClickListener {
            binding.drawView.mBackgroundColor = Color.parseColor(binding.canvasColor.text.toString())
        }
    }

    private fun outputImage() = try {
        @Suppress("DEPRECATION")
        val path = Environment.getExternalStorageDirectory().absolutePath + "/" + binding.path.text.toString().trim()
        //设置是否清除边缘空白
        binding.drawView.isClearBlank = true
        binding.drawView.output(path)
        Toast.makeText(this, "保存成功$path", Toast.LENGTH_SHORT).show()
    }catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(this, "保存失败" + e.message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    outputImage()
                } else {
                    Toast.makeText(this, "请通过授权", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}