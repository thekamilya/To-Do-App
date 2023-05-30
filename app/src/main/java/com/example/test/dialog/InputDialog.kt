package com.example.test.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.example.test.R


class InputDialog(context: Context) : Dialog(context) {
    init {
        this.setContentView(R.layout.bottom_input_layout)
        this.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT )
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.attributes?.windowAnimations = R.style.DialogAnimation
        this.window?.setGravity(Gravity.BOTTOM)
    }
}