package com.example.porcionaapp10

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.porcionaapp10.ui.theme.PorcionaApp10Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PorcionaApp10Theme {
                AppContent()
            }
        }
    }
}

// --- Mock Data ---
val mockRecipes = listOf(
    Receta(
        nombreReceta = "Lasaña Clásica",
        ingredientes = listOf(
            Ingrediente("500", TipoUnidad.GRAMOS, "carne molida"),
            Ingrediente("1", TipoUnidad.UNIDAD, "cebolla"),
            Ingrediente("2", TipoUnidad.UNIDAD, "dientes de ajo"),
            Ingrediente("800", TipoUnidad.GRAMOS, "tomate triturado"),
            Ingrediente("12", TipoUnidad.UNIDAD, "láminas de lasaña")
        ),
        instrucciones = listOf(
            "Paso 1: Sofríe la cebolla y luego el ajo en aceite de oliva hasta que estén transparentes.",
            "Paso 2: Agrega la carne molida y cocina hasta que se dore; drena el exceso de grasa.",
            "Paso 3: Incorpora el tomate triturado, salpimienta y cuece a fuego lento por 30-45 minutos.",
            "Paso 4: Prepara las láminas de lasaña según las instrucciones del paquete. Precalienta el horno a 180°C.",
            "Paso 5: En un molde, extiende una capa de salsa de carne. Coloca una capa de láminas de lasaña encima.",
            "Paso 6: Alterna las capas: Láminas -> Salsa de Carne -> Queso Mozzarella.",
            "Paso 7: Termina con una capa de salsa, cubre con Queso Mozzarella y Parmesano.",
            "Paso 8: Hornea cubierto con papel aluminio por 20 minutos. Retira el aluminio y hornea 10-15 minutos más hasta gratinar. Deja reposar 10-15 minutos antes de servir."
        ).joinToString("\n"),
        imageUrl = "https://source.unsplash.com/random/800x600?lasagna"
    ),
    Receta(
        nombreReceta = "Tiramisú",
        ingredientes = listOf(
            Ingrediente("200", TipoUnidad.GRAMOS, "queso Mascarpone"),
            Ingrediente("3", TipoUnidad.UNIDAD, "huevo"),
            Ingrediente("100", TipoUnidad.MILILITROS, "café fuerte")
        ),
        instrucciones = listOf(
            "Paso 1: Bate las 3 yemas con el azúcar hasta que estén pálidas y cremosas.",
            "Paso 2: Agrega el queso Mascarpone a la mezcla de yemas y bate hasta que se incorpore bien.",
            "Paso 3: Monta las 3 claras de huevo a punto de nieve e incorpóralas a la mezcla de Mascarpone con movimientos envolventes suaves.",
            "Paso 4: Humedece ligeramente cada galleta de soletilla en el café frío (opcionalmente con licor).",
            "Paso 5: Coloca una capa de galletas humedecidas en el fondo de un molde y cubre con una capa de crema Mascarpone.",
            "Paso 6: Repite las capas: Galletas -> Crema. La capa superior debe ser de crema.",
            "Paso 7: Cubre el molde y refrigera por un mínimo de 6 horas o toda la noche para que se asienten los sabores.",
            "Paso 8: Justo antes de servir, espolvorea generosamente con el cacao en polvo usando un colador."
        ).joinToString("\n"),
        imageUrl = "https://source.unsplash.com/random/800x600?tiramisu"
    ),
    Receta(
        nombreReceta = "Guacamole Fresco",
        ingredientes = listOf(
            Ingrediente("2", TipoUnidad.UNIDAD, "aguacates"),
            Ingrediente("1/2", TipoUnidad.UNIDAD, "cebolla morada"),
            Ingrediente(null, null, "pizca de sal")
        ),
        instrucciones = "Machacar los aguacates en un tazón. Picar finamente la cebolla y el tomate e incorporarlos...",
        imageUrl = "https://source.unsplash.com/random/800x600?guacamole"
    ),
    Receta(
        nombreReceta = "Sopa de Lentejas",
        ingredientes = listOf(
            Ingrediente("200", TipoUnidad.GRAMOS, "lentejas"),
            Ingrediente("1", TipoUnidad.MILILITROS, "agua"),
            Ingrediente(null, null, "trozo de chorizo")
        ),
        instrucciones = "Lavar las lentejas y colocarlas en una olla. Añadir el agua y la zanahoria...",
        imageUrl = "https://source.unsplash.com/random/800x600?lentil-soup"
    ),
    Receta(
        nombreReceta = "Pollo Asado",
        ingredientes = listOf(Ingrediente("1", TipoUnidad.UNIDAD, "pollo entero"), Ingrediente(null, null, "sal y pimienta")),
        instrucciones = "Salpimentar el pollo por dentro y por fuera. Hornear a 200°C por 1 hora.",
        imageUrl = "https://source.unsplash.com/random/800x600?roast-chicken"
    ),
    Receta(
        nombreReceta = "Ramen Casero",
        ingredientes = listOf(Ingrediente("500", TipoUnidad.MILILITROS, "caldo de pollo"), Ingrediente("100", TipoUnidad.GRAMOS, "fideos para ramen")),
        instrucciones = "Hervir el caldo, añadir los fideos y cocinar por 3 minutos. Servir con tus toppings favoritos.",
        imageUrl = "https://source.unsplash.com/random/800x600?ramen"
    )
)

@Composable
fun AppContent() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("recipeDetail/{recipeName}") { backStackEntry ->
            val recipeName = backStackEntry.arguments?.getString("recipeName")
            val recipe = mockRecipes.find { it.nombreReceta == recipeName }
            if (recipe != null) {
                RecipeDetailScreen(recipe = recipe, navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú", modifier = Modifier.padding(16.dp), fontSize = 24.sp)
                Divider()
                NavigationDrawerItem(label = { Text("Recetas Propias") }, selected = false, onClick = { Toast.makeText(context, "Recetas Propias", Toast.LENGTH_SHORT).show() })
                NavigationDrawerItem(label = { Text("Recetas Precargadas") }, selected = false, onClick = { Toast.makeText(context, "Recetas Precargadas", Toast.LENGTH_SHORT).show() })
                NavigationDrawerItem(label = { Text("Ayuda") }, selected = false, onClick = { Toast.makeText(context, "Ayuda", Toast.LENGTH_SHORT).show() })
                Divider()
                NavigationDrawerItem(label = { Text("Cerrar App") }, selected = false, onClick = { Toast.makeText(context, "Cerrar App", Toast.LENGTH_SHORT).show() })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Porciona App") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // --- Tus Recetas Section ---
                Text("Tus Recetas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.heightIn(max = 400.dp)) {
                    mockRecipes.take(2).forEach { receta ->
                        TusRecetasCard(receta = receta, navController = navController)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Crear Receta Button ---
                Button(
                    onClick = { Toast.makeText(context, "Botón 'Crear Receta' presionado", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Icon(Icons.Default.Restaurant, contentDescription = "Crear Receta")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CREAR RECETA", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Recetas Precargadas Section ---
                Text("Recetas Precargadas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(mockRecipes.drop(2)) { receta ->
                        PrecargadasRecetaCard(receta = receta, navController = navController)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun TusRecetasCard(receta: Receta, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("recipeDetail/${receta.nombreReceta}") },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = receta.imageUrl,
                contentDescription = receta.nombreReceta,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp).clip(CircleShape),
                placeholder = painterResource(id = R.drawable.ic_launcher_background)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(receta.nombreReceta, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    receta.ingredientes.take(4).joinToString(", ") { it.nombre } + "...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun PrecargadasRecetaCard(receta: Receta, navController: NavController) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable { navController.navigate("recipeDetail/${receta.nombreReceta}") },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = receta.imageUrl,
                contentDescription = receta.nombreReceta,
                modifier = Modifier.height(100.dp).fillMaxWidth(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher_background)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = receta.nombreReceta,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = receta.ingredientes.take(4).joinToString(", ") { it.nombre } + "...",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(recipe: Receta, navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        bottomBar = {
            Button(
                onClick = { Toast.makeText(context, "Navegar a Porcionar Receta", Toast.LENGTH_SHORT).show() },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Icon(Icons.Default.Restaurant, contentDescription = "Porcionar")
                Spacer(modifier = Modifier.width(8.dp))
                Text("PORCIONAR RECETA")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.height(300.dp)) {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = recipe.nombreReceta,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(id = R.drawable.ic_launcher_background)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(shape = CircleShape, elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                    Card(shape = CircleShape, elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                        IconButton(onClick = { Toast.makeText(context, "Editar Receta Presionado", Toast.LENGTH_SHORT).show() }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = recipe.nombreReceta.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("Ingredientes", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
                recipe.ingredientes.forEach { ingrediente ->
                    val text = when {
                        ingrediente.cantidad == null || ingrediente.cantidad == "0" || ingrediente.tipoUnidad == null || listOf("pizca", "chorrito").any { it in ingrediente.nombre.lowercase() } -> ingrediente.nombre
                        ingrediente.isContable -> "${ingrediente.cantidad} ${ingrediente.nombre}"
                        else -> "${ingrediente.cantidad} ${ingrediente.tipoUnidad.name.lowercase()} ${ingrediente.nombre}"
                    }
                    Text(text)
                }

                Text("Instrucciones", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
                Text(recipe.instrucciones)
            }
        }
    }
}
