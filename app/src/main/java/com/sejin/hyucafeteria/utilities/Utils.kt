package com.sejin.hyucafeteria.utilities

import android.content.Context
import android.util.TypedValue

fun dp(context: Context, int: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        int.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}