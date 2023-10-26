package com.icontinental.appdelivery.model

data class Producto(
    val imagen: String? = null,
    val nombre: String? = null,
    val categoria: String? = null,
    val precioRegular: Double? = null,
    val precioDescuento: Double? = null
) {}

data class CarritoProducto(
    val imagen: String? = null,
    val nombre: String? = null,
    val categoria: String? = null,
    val precioRegular: Double? = null,
    val precioDescuento: Double? = null,
    var cantidad: Int = 1
) {}

data class PedidoProductos(
    var cantidad: Int? = null,
    val imagen: String? = null,
    val nombre: String? = null,
    val precio: Double? = null,

) {}

