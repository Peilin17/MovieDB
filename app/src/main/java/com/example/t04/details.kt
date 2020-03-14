package com.example.t04

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.net.URL

class details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deatils)
        findViewById<TextView>(R.id.overview).text = intent.getStringExtra("Describe")
        findViewById<TextView>(R.id.date).text = intent.getStringExtra("DATE")
        findViewById<TextView>(R.id.title).text = intent.getStringExtra("TITLE")
//        var imageLink = URL(resources.getString(R.string.picture_base_url)+intent.getStringExtra("PIC"))
//        var image = Drawable.createFromStream(imageLink.openStream(), "src")
//        findViewById<ImageView>(R.id.posterbig).setImageDrawable(image)
        Glide.with(this@details).load(resources.getString(R.string.picture_base_url)+intent.getStringExtra("PIC")).apply( RequestOptions().override(364, 264)).into(findViewById(R.id.posterbig))

    }
    fun onClickLike (view: View)
    {
        val model  = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        model.addLike(intent.getStringExtra("TITLE"))
    }
    fun onClickDislike (view: View)
    {
        val model  = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        model.dislike(intent.getStringExtra("TITLE"))
    }
}
