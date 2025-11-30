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

private val abbreviations = mapOf(
    TipoUnidad.GRAMOS to "gr",
    TipoUnidad.UNIDAD to "ud",
    TipoUnidad.KILOGRAMOS to "kg",
    TipoUnidad.CUCHARADITAS to "cdta",
    TipoUnidad.CUCHARADAS to "cda",
    TipoUnidad.MILILITROS to "ml",
    TipoUnidad.LITROS to "l",
    TipoUnidad.TAZAS to "tza",
    TipoUnidad.ONZAS to "oz",
    TipoUnidad.PIZCA to "pzc"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecipeScreen(
    navController: NavController,
    onRecipeCreated: (Receta) -> Unit
) {
    var recipeName by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(listOf<Ingrediente>()) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var personCount by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Estados para el nuevo ingrediente
    var newIngredientName by remember { mutableStateOf("") }
    var newIngredientQuantity by remember { mutableStateOf("") }
    var newIngredientUnit by remember { mutableStateOf<TipoUnidad?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> imageUri = uri }
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Crear Nueva Receta") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.5f)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val personas = personCount.toIntOrNull()

                    // Validar campos vacíos
                    if (recipeName.isBlank() ||
                        instructions.isBlank() ||
                        ingredients.isEmpty() ||
                        personCount.isBlank()
                    ) {
                        Toast.makeText(
                            context,
                            "Por favor, rellena todos los campos",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    // Validar que la cantidad de personas sea un número válido
                    if (personas == null || personas <= 0) {
                        Toast.makeText(
                            context,
                            "La cantidad de personas debe ser un número válido",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    val newRecipe = Receta(
                        nombreReceta = recipeName,
                        ingredientes = ingredients,
                        instrucciones = instructions,
                        imageUrl = imageUri?.toString() ?: "",
                        personCount = personas
                    )

                    onRecipeCreated(newRecipe)
                    navController.popBackStack()
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

                    // Imagen
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .clickable { imagePickerLauncher.launch("image/*") }
                    ) {
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
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nombre receta
                    OutlinedTextField(
                        value = recipeName,
                        onValueChange = { recipeName = it },
                        label = { Text("Nombre de la receta") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Instrucciones
                    OutlinedTextField(
                        value = instructions,
                        onValueChange = { instructions = it },
                        label = { Text("Instrucciones") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Cantidad de personas
                    OutlinedTextField(
                        value = personCount,
                        onValueChange = { personCount = it },
                        label = { Text("Cantidad de personas") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Ingredientes", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Lista de ingredientes ya agregados
            if (ingredients.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            ingredients.forEachIndexed { index, ingrediente ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${ingrediente.cantidad ?: ""} " +
                                                "${abbreviations[ingrediente.tipoUnidad] ?: ""} " +
                                                ingrediente.nombre,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(onClick = {
                                        ingredients = ingredients
                                            .toMutableList()
                                            .also { it.removeAt(index) }
                                    }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Eliminar"
                                        )
                                    }
                                }
                                if (index < ingredients.size - 1) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Sección para añadir nuevo ingrediente
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
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        ),
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
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        ),
                        modifier = Modifier.weight(3f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (newIngredientName.isNotBlank()) {
                            ingredients = ingredients + Ingrediente(
                                newIngredientQuantity,
                                newIngredientUnit,
                                newIngredientName
                            )
                            newIngredientName = ""
                            newIngredientQuantity = ""
                            newIngredientUnit = null
                        } else {
                            Toast.makeText(
                                context,
                                "El nombre del ingrediente no puede estar vacío",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
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

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            value = abbreviations[selectedTipoUnidad] ?: "Uni.",
            onValueChange = {},
            readOnly = true,
            label = { Text("Uni.") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TipoUnidad.entries.forEach { tipoUnidad ->
                DropdownMenuItem(
                    text = {
                        Text(abbreviations[tipoUnidad] ?: tipoUnidad.name)
                    },
                    onClick = {
                        onTipoUnidadSelected(tipoUnidad)
                        expanded = false
                    }
                )
            }
        }
    }
}
