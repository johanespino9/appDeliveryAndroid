//package com.icontinental.fooddeliveryapp.componentes.categoria
//
//import android.content.res.Resources
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.RecyclerView.Adapter
//
//class AdapterCategorias(val lista: List<Categoria>, val context: AppCompatActivity, private val listener: OnItemClickListener): Adapter<ViewHolderCategorias>() {
//    // El adapter naneja un listado de card y se comunica con la lista de datos
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCategorias {
//        var card = LayoutInflater.from(parent.context).inflate(R.layout.card_categoria, parent, false)
//
//        return ViewHolderCategorias(card, listener) // Manejar un card especifico
//    }
//
//    override fun getItemCount(): Int {
//        return lista.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolderCategorias, position: Int) {
//        val item = lista[position]
//        Log.d("PACKAGE",context.packageName)
//        val identifier = context.resources.getIdentifier(item.imagen, "drawable", context.packageName)
//        holder.categoriaImagen.setImageResource(identifier)
//        holder.nombreCategoria.text = item.nombre
//    }
//}