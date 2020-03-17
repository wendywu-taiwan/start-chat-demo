package com.example.wendywu.startchatdemo.data

import com.example.wendywu.startchatdemo.utils.combineText
import com.google.gson.annotations.SerializedName

data class ReceiverItem(
        val id: Int,
        @SerializedName("first_name")
        var firstName: String,
        @SerializedName("last_name")
        var lastName: String,
        @SerializedName("mail_address")
        var mailAddress: String,
        var image: String) {

    constructor(id: Int, mailAddress: String) : this(id, "", "", mailAddress, "") {
    }

    fun getName(): CharSequence {
        return combineText(firstName, " ", lastName)
    }

    fun getLabelName(): CharSequence {
        var lastNameFirstChar = lastName[0].toUpperCase().toString()
        return combineText(firstName, " ", lastNameFirstChar, ".")
    }

    fun getFirstNameChar(): String {
        return firstName[0].toUpperCase().toString()
    }

    fun getSelectedReceiverDisplayName(): String {
        var displayName: String
        if (firstName.isNotEmpty() && lastName.isNotEmpty())
            displayName = getLabelName().toString()
        else
            displayName = mailAddress
        return displayName
    }

    fun showUserIcon(): Boolean {
        return lastName.isNotEmpty() && firstName.isNotEmpty() && image.isNotEmpty()
    }

    fun showEmailIcon(): Boolean {
        return lastName.isEmpty() && firstName.isEmpty() && mailAddress.isNotEmpty()
    }
}