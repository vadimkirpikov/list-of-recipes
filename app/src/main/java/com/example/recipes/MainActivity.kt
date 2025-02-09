package com.example.recipes

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.recipes.ui.theme.RecipesTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun loadRecipes(context: Context): List<Recipe> {
    val jsonString = context.assets.open("recipes.json").bufferedReader().use {it.readText()}
    val listType = object: TypeToken<List<Recipe>>() {}.type;
    return Gson().fromJson(jsonString, listType);
}

@Composable
fun RecipeList(recipes: List<Recipe>) {
    LazyColumn {
        itemsIndexed(recipes) { index, recipe ->
            RecipeCard(recipe, index+1)
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, day: Int) {
    val robotoFamily = FontFamily(Font(R.font.roboto))
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Day $day",
                style = TextStyle(
                    fontSize = 15.sp
                ),
                fontFamily = robotoFamily,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = recipe.title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = robotoFamily,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Image(
                painter = rememberAsyncImagePainter(model = recipe.imageUrl),
                contentDescription = "Recipe Image",
                Modifier.fillMaxSize().height(300.dp).clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.padding(top=20.dp),
                text = recipe.description,
                style = TextStyle(
                    fontSize = 18.sp
                ),
                fontFamily = robotoFamily,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
data class Recipe(
    val title: String,
    val description: String,
    val imageUrl: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recipes = loadRecipes(this)
        enableEdgeToEdge()
        setContent {
            RecipesTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = false) {
                RecipeList(recipes)
            }
        }
    }
}
