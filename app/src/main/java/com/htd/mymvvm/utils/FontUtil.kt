package com.htd.mymvvm.utils

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.Spannable
import android.text.style.RelativeSizeSpan
import android.text.style.ScaleXSpan

/**
 * FontUtil 是一个处理字符串字体颜色，大小，间距的工具类
 */
object FontUtil {

    /**
     * 给字体加上颜色
     *
     * @param colorCode  颜色
     * @param text  内容
     * @return
     */
    fun addColor(colorCode: String?, text: String?): CharSequence {
        if (text == null) {
            val spanString = SpannableString(" ")
            spanString.setSpan(
                ForegroundColorSpan(Color.parseColor(colorCode)), 0, " ".length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return spanString
        }
        val spanString = SpannableString(text)
        spanString.setSpan(
            ForegroundColorSpan(Color.parseColor(colorCode)), 0, text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spanString
    }

    /**
     * 给字体加上颜色
     *
     * @param color 颜色
     * @param text 内容
     * @return
     */
    fun addColor(color: Int, text: String): CharSequence {
        val spanString = SpannableString(text)
        spanString.setSpan(
            ForegroundColorSpan(color),
            0,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spanString
    }

    /**
     * 调整字体大小
     *
     * @param relativeSice 相对于原来字体大小的倍数
     * @param text 内容
     * @return
     */
    fun resize(relativeSice: Float, text: String): CharSequence {
        val spanString = SpannableString(text)
        spanString.setSpan(
            RelativeSizeSpan(relativeSice),
            0,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spanString
    }

    /**
     * 调整字体水平间距大小
     *
     * @param relativeXSice   相对于原来字体在水平方向上大小的倍数
     * @param text 内容
     * @return
     */
    fun scaleX(relativeXSice: Float, text: String): CharSequence {
        val spanString = SpannableString(text)
        spanString.setSpan(
            ScaleXSpan(relativeXSice),
            0,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spanString
    }
}