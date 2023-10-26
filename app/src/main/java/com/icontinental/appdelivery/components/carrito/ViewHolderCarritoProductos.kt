package com.icontinental.fooddeliveryapp.componentes.productos

import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icontinental.appdelivery.R
import com.icontinental.appdelivery.OnItemClickListener

class ViewHolderCarritoProductos(card: View) :RecyclerView.ViewHolder(card) {
    val productoImagen: ImageView
    val productoNombre: TextView
//    val productoCategoria: TextView
    val productoPrecioDescuento: TextView
    val montoTotalProducto: TextView
//    val productoPrecioRegular: TextView
    val cantidadProducto: TextView

    init {
        productoImagen = card.findViewById(R.id.imageViewProducto)
        productoNombre = card.findViewById(R.id.textviewNombreProducto)
//        productoCategoria = card.findViewById(R.id.textViewCategoriaProducto)
        productoPrecioDescuento = card.findViewById(R.id.textviewPrecioDescuento)
//        productoPrecioRegular = card.findViewById(R.id.textviewPrecioRegular)
        montoTotalProducto = card.findViewById(R.id.montoTotalProducto)
        cantidadProducto = card.findViewById(R.id.textViewCantidadProducto)


//        card.setOnClickListener {
//            val position = adapterPosition
//            listener.onItemClick(position)
//
//        }
    }
}