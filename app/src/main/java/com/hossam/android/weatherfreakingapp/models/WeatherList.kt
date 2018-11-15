package com.hossam.android.weatherfreakingapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class WeatherList(

		@field:SerializedName("city")
		val city: City? = null,

		@field:SerializedName("cnt")
		val cnt: Int? = null,

		@field:SerializedName("cod")
		val cod: String? = null,

		@field:SerializedName("message")
		val message: Double? = null,

		@field:SerializedName("list")
		val list: List<ListItem?>? = null
) : Parcelable {
	constructor(source: Parcel) : this(
			source.readParcelable<City>(City::class.java.classLoader),
			source.readValue(Int::class.java.classLoader) as Int?,
			source.readString(),
			source.readValue(Double::class.java.classLoader) as Double?,
			source.createTypedArrayList(ListItem.CREATOR)
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
		writeParcelable(city, 0)
		writeValue(cnt)
		writeString(cod)
		writeValue(message)
		writeTypedList(list)
	}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<WeatherList> = object : Parcelable.Creator<WeatherList> {
			override fun createFromParcel(source: Parcel): WeatherList = WeatherList(source)
			override fun newArray(size: Int): Array<WeatherList?> = arrayOfNulls(size)
		}
	}
}
