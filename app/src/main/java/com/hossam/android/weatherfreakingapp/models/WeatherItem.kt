package com.hossam.android.weatherfreakingapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class WeatherItem(

		@field:SerializedName("icon")
		val icon: String? = null,

		@field:SerializedName("description")
		val description: String? = null,

		@field:SerializedName("main")
		val main: String? = null,

		@field:SerializedName("id")
		val id: Int? = null
) : Parcelable {
	constructor(source: Parcel) : this(
			source.readString(),
			source.readString(),
			source.readString(),
			source.readValue(Int::class.java.classLoader) as Int?
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
		writeString(icon)
		writeString(description)
		writeString(main)
		writeValue(id)
	}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<WeatherItem> = object : Parcelable.Creator<WeatherItem> {
			override fun createFromParcel(source: Parcel): WeatherItem = WeatherItem(source)
			override fun newArray(size: Int): Array<WeatherItem?> = arrayOfNulls(size)
		}
	}
}