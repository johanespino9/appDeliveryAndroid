package com.icontinental.appdelivery.model

data class Producto(
    val imagen: String? = null,
    val nombre: String? = null,
    val categoria: String? = null,
    val precioRegular: Double? = null,
    val precioDescuento: Double? = null
) {}