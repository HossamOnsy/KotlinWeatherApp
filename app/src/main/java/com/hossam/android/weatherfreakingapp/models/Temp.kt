package com.hossam.android.weatherfreakingapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Temp(

		@field:SerializedName("min")
		val min: Double? = null,

		@field:SerializedName("max")
		val max: Double? = null,

		@field:SerializedName("eve")
		val eve: Double? = null,

		@field:SerializedName("night")
		val night: Double? = null,

		@field:SerializedName("day")
		val day: Double? = null,

		@field:SerializedName("morn")
		val morn: Double? = null
) : Parcelable {
	constructor(source: Parcel) : this(
			source.readValue(Double::class.java.classLoader) as Double?,
			source.readValue(Double::class.java.classLoader) as Double?,
			source.readValue(Double::class.java.classLoader) as Double?,
			source.readValue(Double::class.java.classLoader) as Double?,
			source.readValue(Double::class.java.classLoader) as Double?,
			source.readValue(Double::class.java.classLoader) as Double?
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
		writeValue(min)
		writeValue(max)
		writeValue(eve)
		writeValue(night)
		writeValue(day)
		writeValue(morn)
	}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<Temp> = object : Parcelable.Creator<Temp> {
			override fun createFromParcel(source: Parcel): Temp = Temp(source)
			override fun newArray(size: Int): Array<Temp?> = arrayOfNulls(size)
		}
	}
}