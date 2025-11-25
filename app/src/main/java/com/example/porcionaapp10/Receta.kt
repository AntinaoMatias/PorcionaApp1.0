package com.example.porcionaapp10

enum class TipoUnidad {
    GRAMOS,
    UNIDAD,
    KILOGRAMOS,
    CUCHARADITAS,
    CUCHARADAS,
    MILILITROS,
    LITROS,
    TAZAS,
    ONZAS
}

data class Ingrediente(
    val cantidad: String?,
    val tipoUnidad: TipoUnidad?,
    val nombre: String,
) {
    val isContable: Boolean
        get() = tipoUnidad == TipoUnidad.UNIDAD
}

data class Receta(
    val nombreReceta: String,
    val ingredientes: List<Ingrediente>,
    val instrucciones: String,
    val imageUrl: String,
    val personCount: Int
)
