package com.example.wendywu.startchatdemo.data

import com.example.wendywu.startchatdemo.utils.combineText
import com.google.gson.annotations.SerializedName

data class ReceiverItem(
        @SerializedName("first_name")
        var firstName: String,
        @SerializedName("last_name")
        var lastName: String,
        @SerializedName("mail_address")
        var mailAddress: String,
        var image: String) {
    fun getName(): CharSequence {
        return combineText(firstName, " ", lastName)
    }

    fun getLabelName(): CharSequence {
        var lastNameFirstChar = lastName.get(0).toUpperCase().toString()
        return combineText(firstName, " ", lastNameFirstChar, ".")
    }
}