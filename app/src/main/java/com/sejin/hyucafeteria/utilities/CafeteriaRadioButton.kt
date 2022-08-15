package com.sejin.hyucafeteria.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.widget.RadioButton
import com.sejin.hyucafeteria.R
import com.sejin.hyucafeteria.data.CafeteriaIdName

@SuppressLint("ViewConstructor")
class CafeteriaRadioButton(context: Context, val idName: CafeteriaIdName) :
    androidx.appcompat.widget.AppCompatRadioButton(context) {


    //    val idName: CafeteriaIdName
    init {
        this.text = idName.name.compressCafeteriaName()
        this.gravity = Gravity.CENTER
        this.textAlignment = TEXT_ALIGNMENT_CENTER
        this.buttonDrawable = null
        this.setTextColor(resources.getColor(R.color.white))
        this.setBackgroundResource(R.drawable.selector_cafeteria_radio_background)
        this.width
        this.width = dp(90)
        this.height = dp(45)
    }

    private fun dp(int: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            int.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}