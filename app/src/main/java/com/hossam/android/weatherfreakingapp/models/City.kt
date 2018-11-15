package com.hossam.android.weatherfreakingapp.models

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
	override fun writeToParcel(p0: Parcel?, p1: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun describeContents(): Int {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}