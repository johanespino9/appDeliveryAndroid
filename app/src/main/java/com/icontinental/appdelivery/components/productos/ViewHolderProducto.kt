package com.icontinental.fooddeliveryapp.componentes.productos

import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icontinental.appdelivery.R
import com.icontinental.appdelivery.OnItemClickListener

class ViewHolderProducto(card: View, listener: OnItemClickListener) :RecyclerView.ViewHolder(card) {
    val productoImagen: ImageView
    val productoNombre: TextView
    val productoCategoria: TextView
    val productoPrecioDescuento: TextView
    val productoPrecioRegular: TextView

    init {
        productoImagen = card.findViewById(R.id.imageViewProducto)
        productoNombre = card.findViewById(R.id.textviewNombreProducto)
        productoCategoria = card.findViewById(R.id.textViewCategoriaProducto)
        productoPrecioDescuento = card.findViewById(R.id.textviewPrecioDescuento)
        productoPrecioRegular = card.findViewById(R.id.textviewPrecioRegular)

        card.setOnClickListener {
            val position = adapterPosition
            listener.onItemClick(position)

        }
    }
}