package edu.ap.padelpro.model

import androidx.annotation.StringRes
import androidx.annotation.DrawableRes
import edu.ap.padelpro.R

data class Field (
   @StringRes val name:Int,
    @DrawableRes val imageResourceId: Int
){

}