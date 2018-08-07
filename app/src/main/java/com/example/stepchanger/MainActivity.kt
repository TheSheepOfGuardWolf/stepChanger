package com.example.stepchanger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = applicationContext;
        var step = Step(context,"/data/data/com.tencent.mm/MicroMsg/");

        //读取步数，隐藏光标
        number.setText(step.stepCount.toString())
        number.isCursorVisible = false

        //点击done关闭软键盘
        number.imeOptions = EditorInfo.IME_ACTION_DONE

        //监听button
        btnDisplay.setOnClickListener {
            var setNum = number.text.toString()
            var steps:Long = 0

            //判断输入是否合法
            if(isDigit(setNum)){
                steps = setNum.toLong();
                step.setStep(steps)
                Toast.makeText(this, setNum, Toast.LENGTH_SHORT).show();
                number.isCursorVisible = false
            }else{
                Toast.makeText(this, "请输入数字", Toast.LENGTH_SHORT).show();
            }
        }

        number.setOnClickListener {
            number.isCursorVisible = true
        }

    }

    fun isDigit(strNum: String): Boolean {
        return strNum.matches("[0-9]{1,}".toRegex())
    }
}
