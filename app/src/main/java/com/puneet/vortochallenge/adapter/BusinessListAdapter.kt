package com.puneet.vortochallenge.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.puneet.vortochallenge.R
import com.puneet.vortochallenge.data.model.Businesse

class BusinessListAdapter(val context: Context, val listOfBusinesses: List<Businesse>) :
    RecyclerView.Adapter<BusinessListAdapter.BusinessItemVH>() {

    var itemClick: ((Int) -> Unit)? = null

    inner class BusinessItemVH(view: View) : RecyclerView.ViewHolder(view) {
        val imageView = view.findViewById<ImageView>(R.id.id_image_view)
        val businessName = view.findViewById<TextView>(R.id.id_business_title)
        val ratingBar = view.findViewById<RatingBar>(R.id.id_rating_bar)
        val category = view.findViewById<TextView>(R.id.id_business_description)
        val openOrClosed = view.findViewById<TextView>(R.id.id_is_open)
        var itemClick: ((Int) -> Unit)? = null

        fun bindView(viewHolder: BusinessItemVH, pos: Int) {
            viewHolder.businessName.text = listOfBusinesses[pos].name
            viewHolder.category.text = listOfBusinesses[pos].categories[0].title
            viewHolder.ratingBar.rating = listOfBusinesses[pos].rating.toFloat()
            viewHolder.openOrClosed.text =
                if (listOfBusinesses[pos].is_closed) context.resources.getString(R.string.closed) else context.resources.getString(
                    R.string.open
                )
            if (!listOfBusinesses[pos].is_closed)
                viewHolder.openOrClosed.setTextColor(Color.GREEN)
            Glide.with(context)
                .load(listOfBusinesses[pos].image_url)
                .into(imageView)
            itemView.setOnClickListener { itemClick?.invoke(pos) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessItemVH {
        Log.e("PuneetChugh", "onCreateViewHolder() called..")
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.business_item, parent, false)
        return BusinessItemVH(view).apply {
            itemClick = { position ->
                this@BusinessListAdapter.itemClick?.invoke(position)
                Log.e("PuneetChugh", "item clicked $position")
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfBusinesses?.size ?: return 0
    }

    override fun onBindViewHolder(holder: BusinessItemVH, position: Int) {
        Log.e("PuneetChugh", "OnBindViewHolder() callled...")
        holder.bindView(holder, position)
    }
}