package com.hossam.android.weatherfreakingapp.modelskotlin

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class City(

		@field:SerializedName("country")
		val country: String? = null,

		@field:SerializedName("coord")
		val coord: Coord? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("population")
		val population: Int? = null
) : Parcelable {
	constructor(source: Parcel) : this(
			source.readString(),
			source.readParcelable<Coord>(Coord::class.java.classLoader),
			source.readString(),
			source.readValue(Int::class.java.classLoader) as Int?,
			source.readValue(Int::class.java.classLoader) as Int?
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
		writeString(country)
		writeParcelable(coord, 0)
		writeString(name)
		writeValue(id)
		writeValue(population)
	}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<City> = object : Parcelable.Creator<City> {
			override fun createFromParcel(source: Parcel): City = City(source)
			override fun newArray(size: Int): Array<City?> = arrayOfNulls(size)
		}
	}
}