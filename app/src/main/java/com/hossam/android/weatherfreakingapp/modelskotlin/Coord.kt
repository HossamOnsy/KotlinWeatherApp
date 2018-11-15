package com.hossam.android.weatherfreakingapp.modelskotlin

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Coord(

		@field:SerializedName("lon")
		val lon: Double? = null,

		@field:SerializedName("lat")
		val lat: Double? = null
) : Parcelable {
	constructor(source: Parcel) : this(
			source.readValue(Double::class.java.classLoader) as Double?,
			source.readValue(Double::class.java.classLoader) as Double?
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
		writeValue(lon)
		writeValue(lat)
	}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<Coord> = object : Parcelable.Creator<Coord> {
			override fun createFromParcel(source: Parcel): Coord = Coord(source)
			override fun newArray(size: Int): Array<Coord?> = arrayOfNulls(size)
		}
	}
}