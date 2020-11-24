package com.bobabelga.asteroidradar.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bobabelga.asteroidradar.R
import com.bobabelga.asteroidradar.room.AsteroidEntity
import com.squareup.picasso.Picasso

@BindingAdapter("setImageOfTheDay")
fun bindImageOfDay(imageView: ImageView, url: String) {
     Picasso.get().load(url).placeholder(R.drawable.placeholder_picture_of_day)
         .into(imageView)
}
@BindingAdapter("statusIcon")
fun ImageView.bindAsteroidStatusImage(asteroid: AsteroidEntity?) {
    asteroid?.let {
        setImageResource(
            if (asteroid.isPotentiallyHazardous)
                R.drawable.ic_status_potentially_hazardous
            else R.drawable.ic_status_normal
        )
    }
}
@BindingAdapter("settitle")
fun TextView.bindSettitle(asteroid: AsteroidEntity?) {
    asteroid?.let {
        text = it.codename
    }
}
@BindingAdapter("setcloseApproachDate")
fun TextView.bindSetcloseApproachDate(asteroid: AsteroidEntity?) {
    asteroid?.let {
        text = it.closeApproachDate
    }
}
@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}


