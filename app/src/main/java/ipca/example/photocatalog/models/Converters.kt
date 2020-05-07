package ipca.example.photocatalog.models

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

//
// Created by lourencogomes on 07/05/2020.
//
class Converters {

    @TypeConverter
    fun dateToString(date : Date) : String{

        val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss", Locale.getDefault())
        return formatter.format(date)
    }

    @TypeConverter
    fun stringToDate(dateStr: String) : Date {
        val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss", Locale.getDefault())
        val date = formatter.parse(dateStr)

        return date ?: Date()

    }
}