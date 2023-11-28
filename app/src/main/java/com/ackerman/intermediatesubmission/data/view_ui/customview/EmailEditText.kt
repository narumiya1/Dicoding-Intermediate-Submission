package com.ackerman.intermediatesubmission.data.view_ui.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.ackerman.intermediatesubmission.R

class EmailEditText : AppCompatEditText {

    constructor(context: Context):super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = " "
    }

    private fun init(){
        maxLines = 1
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setSelection(text!!.length)

                if (p0 != null) {
                    error = if (p0.isEmpty()) {
                        null
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(p0).matches()) {
                        resources.getString(R.string.email_not_valid)
                    } else {
                        null
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

    }
}