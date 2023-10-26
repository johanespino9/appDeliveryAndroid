package com.icontinental.fooddeliveryapp.componentes.productos

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.icontinental.appdelivery.R
import com.icontinental.appdelivery.OnItemClickListener
import com.icontinental.appdelivery.model.CarritoProducto
import com.icontinental.appdelivery.model.Producto
import kotlin.coroutines.coroutineContext

class AdapterCarritoProductos(val lista: List<CarritoProducto>, val context: AppCompatActivity): RecyclerView.Adapter<ViewHolderCarritoProductos>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCarritoProductos {
        val card = LayoutInflater.from(parent.context).inflate(R.layout.card_carrito_producto, parent, false)

        return ViewHolderCarritoProductos(card)
    }

    override fun getItemCount(): Int {
        Log.d("TAMANIO DE LA LISTA DE PRODUCTOS:", "${lista.size}")
       return lista.size
    }

    override fun onBindViewHolder(holder: ViewHolderCarritoProductos, position: Int) {
        // unir la data con las vistas o componentes

        val producto = lista[position]
        val cantidad = producto.cantidad
        val total =  (producto.precioDescuento ?: 0.0) * (cantidad ?: 1)
            Log.d("PRODUCTOS", "${producto.nombre}")
        holder.productoNombre.text = producto.nombre
//        holder.productoCategoria.text = producto.categoria
        holder.productoPrecioDescuento.text = producto.precioDescuento.toString()
        holder.montoTotalProducto.text = "S/"+total.toString()
//        holder.productoPrecioRegular.text = producto.precioRegular.toString()
        val id = context.resources.getIdentifier(producto.imagen, "drawable", context.packageName)
        holder.productoImagen.setImageResource(id)
        holder.cantidadProducto.text = cantidad.toString()
    }
}