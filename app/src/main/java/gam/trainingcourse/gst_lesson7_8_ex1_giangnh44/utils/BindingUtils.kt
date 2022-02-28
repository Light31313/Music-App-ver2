package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun ImageView.loadImageByUrl(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(this).load(it).placeholder(R.drawable.ic_baseline_eco_24).into(this)
    }
}

@BindingAdapter("milliseconds")
fun TextView.convertSecondsToTimeFormat(milliseconds: Int) {
    val timeFormat = SimpleDateFormat("mm:ss", Locale.US)
    text = timeFormat.format(milliseconds)
}

@BindingAdapter("sourceImageUrl")
fun View.getImageDominatedColor(sourceImageUrl: String?){
    sourceImageUrl?.let {
        Glide.with(this).asBitmap().load(it).into(object : CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val palette = Palette.from(resource).generate()
                this@getImageDominatedColor.setBackgroundColor(palette.getDarkMutedColor(
                    resources.getColor(
                        R.color.black,
                        null
                    )
                ))
            }
            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
    }

}

