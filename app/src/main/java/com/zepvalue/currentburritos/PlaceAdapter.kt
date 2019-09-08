package com.zepvalue.currentburritos

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zepvalue.currentburritos.model.others.Spot
import com.zepvalue.currentburritos.utility.genDescription
import kotlinx.android.synthetic.main.place_list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class PlaceAdapter(val items : ArrayList<Spot>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.place_list_item, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val place = items.get(position)
        holder?.itemView.tv_place_name?.text = place.name
        holder?.itemView.tv_place_address?.text = place.vicinity
        holder?.itemView.tv_place_price?.text = "$".repeat(place.price) + " • " + genDescription(place.userRating)


        holder.itemView.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("EXTRA_MARKER", place)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        }
    }

}

class CustomViewHolder (view: View) : RecyclerView.ViewHolder(view) {}
