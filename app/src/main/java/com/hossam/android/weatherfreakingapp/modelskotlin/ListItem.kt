package com.hossam.android.weatherfreakingapp.modelskotlin

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ListItem(

	@field:SerializedName("dt")
	val dt: Int? = null,

	@field:SerializedName("temp")
	val temp: Temp? = null,

	@field:SerializedName("deg")
	val deg: Int? = null,

	@field:SerializedName("weather")
	val weather: List<WeatherItem?>? = null,

	@field:SerializedName("humidity")
	val humidity: Double? = null,

	@field:SerializedName("pressure")
	val pressure: Double? = null,

	@field:SerializedName("clouds")
	val clouds: Int? = null,

	@field:SerializedName("speed")
	val speed: Double? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readParcelable(Temp::class.java.classLoader),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.createTypedArrayList(WeatherItem.CREATOR),
			parcel.readValue(Double::class.java.classLoader) as? Double,
			parcel.readValue(Double::class.java.classLoader) as? Double,
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Double::class.java.classLoader) as? Double) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(dt)
		parcel.writeParcelable(temp, flags)
		parcel.writeValue(deg)
		parcel.writeTypedList(weather)
		parcel.writeValue(humidity)
		parcel.writeValue(pressure)
		parcel.writeValue(clouds)
		parcel.writeValue(speed)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<ListItem> {
		override fun createFromParcel(parcel: Parcel): ListItem {
			return ListItem(parcel)
		}

		override fun newArray(size: Int): Array<ListItem?> {
			return arrayOfNulls(size)
		}
	}
}