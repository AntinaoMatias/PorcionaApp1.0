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

data class Receta(
    val nombre_receta: String,
    val tipo_unidad: List<TipoUnidad>,
    val ingredientes: String,
    val instrucciones: String,
    val image_url: String
)