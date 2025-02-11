package com.example.calories.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.toSize

@Composable
fun CalorieApp(modifier: Modifier = Modifier) {
    var weightInput by remember { mutableStateOf("") }
    val weight = weightInput.toIntOrNull() ?: 0
    var male by remember { mutableStateOf(true) }
    var intensity by remember { mutableFloatStateOf(1.3f) }
    var result by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Heading("Calories Tracker")
        WeightField(weightInput = weightInput, onValueChange = { weightInput = it })
        GenderChoices(male = male, setGenderMale = { male = it })
        IntensityList(onClick = { intensity = it })

        //Spacer(modifier = Modifier.height(8.dp))

        // ✅ Use the Calculation function as the button
        Calculation(
            male = male,
            weight = weight,
            intensity = intensity,
            setResult = { result = it } // Updates the result
        )

        Text(
            text = "Calories Burned: $result",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold

        )

        /*Button(onClick = {
            result = if (male) {
                ((879 + 10.2 * weight) * intensity).toInt()
            } else {
                ((795 + 7.18 * weight) * intensity).toInt()
            }
        }) {
            Text("Calculate Calories")
        }*/


    }
}
@Composable
fun Calculation(male: Boolean, weight: Int, intensity: Float, setResult: (Int) -> Unit) {
    Button(
        onClick = {
            if (male) {
                setResult(((879 + 10.2 * weight) * intensity).toInt())
            } else {
                setResult(((795 + 7.18 * weight) * intensity).toInt())
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "CALCULATE")
    }
}

// ✅ 1. Heading Component
@Composable
fun Heading(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp)
    )
}

// ✅ 2. WeightField Component
@Composable
fun WeightField(weightInput: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = weightInput,
        onValueChange = onValueChange,
        label = { Text("Enter weight (kg)") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

// ✅ 3. GenderChoices Component
@Composable
fun GenderChoices(male: Boolean, setGenderMale: (Boolean) -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = male, onClick = { setGenderMale(true) })
            Text(text = "Male")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = !male, onClick = { setGenderMale(false) })
            Text(text = "Female")
        }
    }
}

// ✅ 4. IntensityList (Dropdown Menu)
@Composable
fun IntensityList(onClick: (Float) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Light") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val intensityLevels = listOf("Light", "Usual", "Moderate", "Hard", "Very hard")
    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedTextField(
            readOnly = true,
            value = selectedText,
            onValueChange = { },
            label = { Text("Select Intensity") },
            trailingIcon = {
                Icon(icon, "Select intensity", Modifier.clickable { expanded = !expanded })
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            intensityLevels.forEach { level ->
                val intensityValue = when (level) {
                    "Light" -> 1.3f
                    "Usual" -> 1.5f
                    "Moderate" -> 1.7f
                    "Hard" -> 2.0f
                    "Very hard" -> 2.2f
                    else -> 1.3f
                }

                DropdownMenuItem(
                    text = { Text(level) },
                    onClick = {
                        selectedText = level
                        expanded = false
                        onClick(intensityValue)
                    }
                )
            }
        }
    }
}

// ✅ Preview Function
@Preview(showBackground = true)
@Composable
fun CalorieAppPreview() {
    CaloriesTheme {
        CalorieApp()
    }
}
