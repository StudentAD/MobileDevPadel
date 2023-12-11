package edu.ap.padelpro.model

import androidx.annotation.StringRes
import androidx.annotation.DrawableRes
import edu.ap.padelpro.R

data class Field (
   @StringRes val name:Int,
    @DrawableRes val imageResourceId: Int
){

    var hours = mutableListOf<String>(
        "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00"
    )

}