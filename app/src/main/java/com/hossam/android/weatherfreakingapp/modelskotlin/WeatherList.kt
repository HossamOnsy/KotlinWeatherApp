package com.hossam.android.weatherfreakingapp.modelskotlin

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
	constructor(parcel: Parcel) : this(
			parcel.readParcelable(City::class.java.classLoader),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readString(),
			parcel.readValue(Double::class.java.classLoader) as? Double,
			parcel.createTypedArrayList(ListItem)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeParcelable(city, flags)
		parcel.writeValue(cnt)
		parcel.writeString(cod)
		parcel.writeValue(message)
		parcel.writeTypedList(list)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<WeatherList> {
		override fun createFromParcel(parcel: Parcel): WeatherList {
			return WeatherList(parcel)
		}

		override fun newArray(size: Int): Array<WeatherList?> {
			return arrayOfNulls(size)
		}
	}
}