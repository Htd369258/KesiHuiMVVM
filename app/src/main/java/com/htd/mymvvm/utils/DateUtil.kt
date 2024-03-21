package com.htd.mymvvm.utils

import android.text.format.Time
import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * DateUtil 是一个关于时间，日期转换和判断的类
 */
object DateUtil {
    const val PT_YMDHM = "yyyy-MM-dd HH:mm"

    /**
     * 获取当前时间
     *
     * @return 毫秒
     */
    fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 获取当前时间
     *
     * @return 标准时间字符串
     */
    fun currentTime(): String {
        return getDate("yyyy-MM-dd HH:mm:ss", currentTimeMillis())
    }

    fun currentTime1(): String {
        return getDate("yyyy-MM-dd HH:mm", currentTimeMillis())
    }

    /**
     * 获取当前日期
     *
     * @return 标准日期字符串
     */
    fun currentDate(): String {
        return getDate("yyyy-MM-dd", currentTimeMillis())
    }

    /**
     * 判断两个日期是否是同一天
     *
     * @param time1 日期一，毫秒表示
     * @param time2 日期二，毫秒表示
     * @return boolean
     */
    fun isSameDay(time1: Long, time2: Long): Boolean {
        val time = Time()
        time.set(time1)
        val thenYear = time.year
        val thenMonth = time.month
        val thenMonthDay = time.monthDay
        time.set(time2)
        return thenYear == time.year && thenMonth == time.month && thenMonthDay == time.monthDay
    }

    /**
     * 判断两个日期恰好相隔n天（n 为正整数）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return boolean
     */
    fun isAfterDays(startTime: Long, endTime: Long, days: Int): Boolean {
        val time = Time()
        val deadTime = getNextDay(startTime, days)
        time.set(deadTime)
        val thenYear = time.year
        val thenMonth = time.month
        val thenMonthDay = time.monthDay
        time.set(endTime)
        return thenYear == time.year && thenMonth == time.month && thenMonthDay == time.monthDay
    }

    /**
     * 获取从某个日期开始计算未来日期的时间戳
     *
     * @param startTime 开始日期
     * @param index     index表示今天后的第几天，0表示今天
     * @return boolean
     */
    fun getNextDay(startTime: Long, index: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = startTime
        calendar.add(Calendar.DAY_OF_MONTH, index)
        return calendar.time.time
    }

    /**
     * 比较大小
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    fun compare_date(DATE1: String?, DATE2: String?): Int {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        try {
            val dt1 = df.parse(DATE1)
            val dt2 = df.parse(DATE2)
            return if (dt1.time > dt2.time) {
                println("dt1 在dt2前")
                1
            } else if (dt1.time < dt2.time) {
                println("dt1在dt2后")
                -1
            } else {
                0
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return 0
    }

    /**
     * 获取从某个日期开始计算未来日期的时间戳
     *
     * @param index index表示今天后的第几天，0表示今天
     * @return boolean
     */
    fun getNextDay(today: String?, index: Int): String {
        val sj = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        try {
            val d = sj.parse(today)
            calendar.time = d
        } catch (e: Exception) {
        }
        calendar.add(Calendar.DATE, index)
        return sj.format(calendar.time)
    }

    //将时间转换为时间戳
    fun dateToStamp(s: String?): Long { //设置时间格式，将该时间格式的时间转换为时间戳
        var time: Long = 0
        try {
            val simpleDateFormat =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = simpleDateFormat.parse(s)
            time = date.time
        } catch (e: Exception) {
        }
        return time
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14  16:09:00"）
     *
     * @param time
     * @return
     */
    fun timedate(time: String): String {
        val sdr = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val i = time.toInt()
        return sdr.format(Date(i * 1000L))
    }

    fun timedate(time: Long?): String {
        val sdr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdr.format(Date(time!!))
    }

    fun time(date: Date?): String {
        val sdr = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return sdr.format(date)
    }

    fun timeMonthAndDay(time: String): String {
        val sdr = SimpleDateFormat("MM-dd")
        val lcc = java.lang.Long.valueOf(time)
        val i = time.toInt()
        return sdr.format(Date(i * 1000L))
    }

    fun timeMonthAndDayHourMintue(time: Date?): String {
        val sdr = SimpleDateFormat("MM-dd HH:mm")
        sdr.timeZone = TimeZone.getTimeZone("Asia/Singapore")
        return sdr.format(time)
    }

    fun timHourMintue(time: Date?): String {
        val sdr = SimpleDateFormat("HH:mm")
        return sdr.format(time)
    }

    fun timeYearMonthAndDay(time: Date?): String {
        val sdr = SimpleDateFormat("yyyy-MM-dd")
        sdr.timeZone = TimeZone.getTimeZone("Asia/Singapore")
        return sdr.format(time)
    }

    fun timeMonth(time: String?): String {
        val sdr = SimpleDateFormat("MM")
        val lcc = java.lang.Long.valueOf(time)
        //        int i = Integer.parseInt(time);
        return sdr.format(Date(lcc))
    }

    fun timeMonth(time: Date?): String {
        val sdr = SimpleDateFormat("MM")
        sdr.timeZone = TimeZone.getTimeZone("Asia/Singapore")
        return sdr.format(time)
    }

    fun timeDay(time: Date?): String {
        val sdr = SimpleDateFormat("dd")
        sdr.timeZone = TimeZone.getTimeZone("Asia/Singapore")
        return sdr.format(time)
    }

    fun timeMinute(time: Date?): String {
        val sdr = SimpleDateFormat("mm")
        sdr.timeZone = TimeZone.getTimeZone("Asia/Singapore")
        return sdr.format(time)
    }

    fun timeHourAndMin(time: String): String {
        val sdr = SimpleDateFormat("HH:mm")
        val lcc = java.lang.Long.valueOf(time)
        val i = time.toInt()
        return sdr.format(Date(i * 1000L))
    }

    fun timeHour(time: Date?): String {
        val sdr = SimpleDateFormat("HH")
        sdr.timeZone = TimeZone.getTimeZone("Asia/Singapore")
        return sdr.format(time)
    }

    fun timeHourAndMin(time: Date?): String {
        val sdr = SimpleDateFormat("HH:mm")
        sdr.timeZone = TimeZone.getTimeZone("Asia/Singapore")
        //        Asia/Calcutta  Asia/Shanghai Asia/Jakarta
        val times = sdr.format(time)
        return times
    }

    fun timeYearMonthDayHourMin(time: Date?): String {
        val sdr = SimpleDateFormat("yyyy-MM-dd HH:mm")
        sdr.timeZone = TimeZone.getTimeZone("Asia/Singapore")
        return sdr.format(time)
    }

    fun timeYearMonthDay(time: String): String {
        val sdr = SimpleDateFormat("yyyy-MM-dd")
        val i = time.toInt()
        return sdr.format(Date(i * 1000L))
    }

    fun timeYearMonth(time: String): String {
        val sdr = SimpleDateFormat("yyyy-MM")
        val lcc = java.lang.Long.valueOf(time)
        val i = time.toInt()
        return sdr.format(Date(i * 1000L))
    }

    /**
     * 得到过去几个月的日期
     *
     * @param index
     * @return
     */
    fun getlastMonth(index: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, index) //-3  得到前3个月
        val formNowMonth = calendar.time
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(formNowMonth)
    }

    /**
     * 得到过去几个月的日期
     * 格式：2106年09月
     *
     * @param index
     * @return
     */
    fun getLastMonthNoDay(index: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, index) //-3  得到前3个月
        val formNowMonth = calendar.time
        val sdf = SimpleDateFormat("yyyy-MM")
        val s1 = sdf.format(formNowMonth)
        val year = s1.substring(0, 4)
        val month = s1.substring(5, 7)
        return ""
    }

    /**
     * 得到过去几天的日期
     *
     * @param index
     * @return
     */
    fun getlastDate(index: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, index)
        val formNowDate = calendar.time
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(formNowDate)
    }

    /**
     * 获取今天开始计算未来日期的时间戳
     *
     * @param index index表示今天后的第几天，0表示今天
     * @return boolean
     */
    fun getNextDay(index: Int): Long { // 获取日期，index表示今天后的第几天
        return getNextDay(currentTimeMillis(), index)
    }

    /**
     * 将时间date转化成String类型
     *
     * @param template 时间的格式，为正则表达式，如 “YYYY-MM-dd HH:mm:ss”
     * @param date     要转换日期，单位为毫秒
     * @return String类型日期，如 “2015年08月08日 12:00:00”
     */
    fun getDate(template: String?, date: Long): String {
        if (date > 0) {
            val format =
                SimpleDateFormat(template, Locale.CHINA)
            return format.format(Date(date))
        }
        return ""
    }

    /**
     * 通过判断将时间转换为更容易理解的具体词语
     *
     * @param time 要转换的时间
     * @return String（刚刚 几分钟前 几小时前 几天前 具体时间）
     */
    fun transTime(time: Long): String {
        if (time > 0) {
            var btm = (System.currentTimeMillis() - time) / 1000
            if (btm <= 60) {
                return "刚刚"
            } else {
                btm = btm / 60
                if (btm <= 60) {
                    return btm.toString() + "分钟前"
                } else {
                    btm = btm / 60
                    if (btm <= 24) {
                        if (btm <= 12) return btm.toString() + "小时前"
                        if (btm > 12) return "今天 "
                    } else {
                        btm = btm / 24
                        return if (btm <= 3) {
                            if (btm == 1L) btm.toString() + "昨天" else btm.toString() + "天前"
                        } else {
                            val format = SimpleDateFormat(
                                "yyyy.MM.dd  HH:mm",
                                Locale.CHINA
                            )
                            format.format(Date(time))
                        }
                    }
                }
            }
        }
        return ""
    }

    fun getComentTimeStr(time: Long): String {
        val date = Date(time * 1000)
        val cal1 = Calendar.getInstance()
        cal1.time = date
        val cal2 = Calendar.getInstance()
        val nowDate = Date()
        cal2.time = nowDate
        val mins = (nowDate.time - date.time) / (1000 * 60)
        if (mins == 0L) {
            return "刚刚"
        }
        val hour = (nowDate.time - date.time) / (1000 * 60 * 60)
        if (hour < 1) {
            return mins.toString() + "分钟前"
        }
        if (hour < 12) return hour.toString() + "小时前"
        if (cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.MONTH] + 1 == cal2[Calendar.MONTH] + 1 && cal1[Calendar.DAY_OF_MONTH] == cal2[Calendar.DAY_OF_MONTH] && hour >= 1
        ) {
            return "今天" + cal1[Calendar.HOUR_OF_DAY] + ":" + cal1[Calendar.MINUTE]
        }
        cal1.add(Calendar.DAY_OF_MONTH, 1)
        if (cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.MONTH] + 1 == cal2[Calendar.MONTH] + 1 && cal1[Calendar.DAY_OF_MONTH] == cal2[Calendar.DAY_OF_MONTH]
        ) {
            return "昨天" + cal1[Calendar.HOUR_OF_DAY] + ":" + cal1[Calendar.MINUTE]
        }
        cal1.time = date
        return if (cal1[Calendar.YEAR] == cal2[Calendar.YEAR]) {
            (cal1[Calendar.MONTH] + 1).toString() + "月" + cal1[Calendar.DAY_OF_MONTH] + "日"
        } else cal1[Calendar.YEAR]
            .toString() + "年" + (cal1[Calendar.MONTH] + 1) + "月" + cal1[Calendar.DAY_OF_MONTH] + "日"
    }

    /**
     * 当前时间已经超过目标时间的分钟数
     *
     * @param time 目标时间
     * @return
     */
    fun overTimeMinutes(time: Long): Long {
        return (System.currentTimeMillis() - time) / 1000 / 60
    }

    /* 时间戳转换成字符窜 */
    fun getDateToString(time: Long): String {
        val d = Date(time)
        val sf = SimpleDateFormat("MM-dd HH:mm")
        return sf.format(d)
    }

    /* 时间戳转换成字符窜 */
    fun getDateString(Strdate: String?): String {
        val format = SimpleDateFormat("yyyy.MM")
        var date: Date? = null
        try {
            date = format.parse(Strdate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val time = date?.time.toString() + ""
        return time.substring(0, 10)
    }

    /* 时间戳转换成字符窜 */
    fun getDatString(Strdate: String?): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = null
        try {
            date = format.parse(Strdate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val time = date?.time.toString() + ""
        return time.substring(0, 10)
    }

    fun getTimeStamp(time: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        if ("" != time) { //时间不为空
            try {
                return sdf.parse(time).time
            } catch (e: Exception) {
                println("参数为空！")
            }
        } else {    //时间为空
            return System.currentTimeMillis() //获取当前时间
        }
        return 0L
    }

    fun convert(mill: Long): String {
        val date = Date(mill)
        var strs = ""
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            strs = sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return strs
    }

    fun getWeek(date: Date?): Int {
        val cd = Calendar.getInstance()
        cd.timeZone = TimeZone.getTimeZone("Asia/Singapore")
        cd.time = date
        return cd[Calendar.DAY_OF_WEEK]
    }

    fun currentYear(): String {
        return getDate("yyyy", currentTimeMillis())
    }

    val currentYear: String
        get() {
            val sdf = SimpleDateFormat("yyyy")
            val date = Date()
            return sdf.format(date)
        }

    fun currentMonth(): String {
        return getDate("MM", currentTimeMillis())
    }

    fun currentDay(): String {
        return getDate("dd", currentTimeMillis())
    }

    fun currentYearMonth(): String {
        return getDate("yyyy-MM", currentTimeMillis())
    }

    fun currentYearMonthDay(): String {
        return getDate("yyyy-MM-dd", currentTimeMillis())
    }

    fun currentYearMonthDayHourMin(): String {
        return getDate("yyyy-MM-dd HH:mm", currentTimeMillis())
    }
}