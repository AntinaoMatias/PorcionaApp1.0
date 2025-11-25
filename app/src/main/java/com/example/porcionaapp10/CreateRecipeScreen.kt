package com.example.porcionaapp10

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecipeScreen(navController: NavController, onRecipeCreated: (Receta) -> Unit) {
    var recipeName by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(listOf<Ingrediente>()) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var personCount by remember { mutableStateOf("") }
    val context = LocalContext.current

    // States for the ingredient input
    var newIngredientName by remember { mutableStateOf("") }
    var newIngredientQuantity by remember { mutableStateOf("") }
    var newIngredientUnit by remember { mutableStateOf<TipoUnidad?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> imageUri = uri }
    )

    val abbreviations = mapOf(
        TipoUnidad.GRAMOS to "gr",
        TipoUnidad.UNIDAD to "ud",
        TipoUnidad.KILOGRAMOS to "kg",
        TipoUnidad.CUCHARADITAS to "cditas",
        TipoUnidad.CUCHARADAS to "cda",
        TipoUnidad.MILILITROS to "ml",
        TipoUnidad.LITROS to "lt",
        TipoUnidad.TAZAS to "tazas",
        TipoUnidad.ONZAS to "oz",
        TipoUnidad.PIZCA to "pizca"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nueva Receta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (recipeName.isNotBlank() && instructions.isNotBlank() && ingredients.isNotEmpty() && personCount.isNotBlank()) {
                        val newRecipe = Receta(
                            nombreReceta = recipeName,
                            ingredientes = ingredients,
                            instrucciones = instructions,
                            imageUrl = imageUri?.toString() ?: "",
                            personCount = personCount.toIntOrNull() ?: 1
                        )
                        onRecipeCreated(newRecipe)
                    } else {
                        Toast.makeText(context, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = "Guardar Receta")
                Spacer(modifier = Modifier.width(8.dp))
                Text("GUARDAR RECETA")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable { imagePickerLauncher.launch("image/*") })
                    {
                        if (imageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "Imagen de la receta",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = "Agregar imagen",
                                tint = Color.Gray,
                                modifier = Modifier.size(50.dp).align(Alignment.Center)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = recipeName,
                        onValueChange = { recipeName = it },
                        label = { Text("Nombre de la receta") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = instructions,
                        onValueChange = { instructions = it },
                        label = { Text("Instrucciones") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = personCount,
                        onValueChange = { personCount = it },
                        label = { Text("Cantidad de personas") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Ingredientes", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (ingredients.isNotEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            ingredients.forEachIndexed { index, ingrediente ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${ingrediente.cantidad ?: ""} ${abbreviations[ingrediente.tipoUnidad] ?: ""} ${ingrediente.nombre}",
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(onClick = {
                                        ingredients = ingredients.toMutableList().also { it.removeAt(index) }
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                    }
                                }
                                if (index < ingredients.size - 1) {
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                Text("Añadir Ingrediente", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newIngredientQuantity,
                        onValueChange = { newIngredientQuantity = it },
                        label = { Text("Cant.") },
                        modifier = Modifier.weight(1.5f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TipoUnidadDropDown(
                        selectedTipoUnidad = newIngredientUnit,
                        onTipoUnidadSelected = { newIngredientUnit = it },
                        modifier = Modifier.weight(1.8f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = newIngredientName,
                        onValueChange = { newIngredientName = it },
                        label = { Text("Ingr.") },
                        modifier = Modifier.weight(3f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    if (newIngredientName.isNotBlank()) {
                        ingredients = ingredients + Ingrediente(newIngredientQuantity, newIngredientUnit, newIngredientName)
                        newIngredientName = ""
                        newIngredientQuantity = ""
                        newIngredientUnit = null
                    } else {
                        Toast.makeText(context, "El nombre del ingrediente no puede estar vacío", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar ingrediente")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipoUnidadDropDown(
    selectedTipoUnidad: TipoUnidad?,
    onTipoUnidadSelected: (TipoUnidad) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val abbreviations = mapOf(
        TipoUnidad.GRAMOS to "gr",
        TipoUnidad.UNIDAD to "ud",
        TipoUnidad.KILOGRAMOS to "kg",
        TipoUnidad.CUCHARADITAS to "cditas",
        TipoUnidad.CUCHARADAS to "cda",
        TipoUnidad.MILILITROS to "ml",
        TipoUnidad.LITROS to "lt",
        TipoUnidad.TAZAS to "tazas",
        TipoUnidad.ONZAS to "oz",
        TipoUnidad.PIZCA to "pizca"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = abbreviations[selectedTipoUnidad] ?: "Uni.",
            onValueChange = {},
            readOnly = true,
            label = { Text("Uni.") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            TipoUnidad.values().forEach { tipoUnidad ->
                DropdownMenuItem(
                    text = { Text(abbreviations[tipoUnidad] ?: tipoUnidad.name) },
                    onClick = {
                        onTipoUnidadSelected(tipoUnidad)
                        expanded = false
                    }
                )
            }
        }
    }
}
