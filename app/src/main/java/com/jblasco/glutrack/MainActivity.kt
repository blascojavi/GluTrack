package com.jblasco.glutrack

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jblasco.glutrack.ui.theme.GluTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GluTrackTheme {
                DashboardScreen()
            }
        }
    }
}

@Composable
fun DashboardScreen() {
    val fakeWeightData = listOf(
        "01/06" to 74.2f,
        "03/06" to 73.8f,
        "05/06" to 73.5f,
        "07/06" to 73.2f,
        "09/06" to 72.8f,
        "11/06" to 72.5f
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Hola, Javi 👋",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Recuerda tu dosis de hoy",
                fontSize = 18.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Primera fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatCard(title = "Proteínas", value = "76g", color = Color(0xFF81C784), modifier = Modifier.weight(1f))
                StatCard(title = "Agua", value = "1.5L", color = Color(0xFF64B5F6), modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Segunda fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatCard(title = "Glucosa", value = "112 mg/dL", color = Color(0xFFFFB74D), modifier = Modifier.weight(1f))
                StatCard(title = "Pasos", value = "3.582", color = Color(0xFFA1887F), modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Próxima dosis: 20:30h",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Peso actual: 72,3 kg (hace 2 días)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "¿Cómo te sientes hoy?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MoodButton("😄")
                MoodButton("😐")
                MoodButton("😟")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Card con el gráfico
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Historial de peso",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3F51B5)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    WeightChart(weights = fakeWeightData)
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MoodButton(emoji: String) {
    Button(
        onClick = { /* Aquí más adelante puedes registrar el estado de ánimo */ },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
        modifier = Modifier.size(64.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 24.sp)
        }
    }
}

@Composable
fun WeightChart(weights: List<Pair<String, Float>>) {
    AndroidView(
        factory = { context ->
            val chart = LineChart(context)

            val entries = weights.mapIndexed { index, pair ->
                Entry(index.toFloat(), pair.second)
            }

            val dataSet = LineDataSet(entries, "Peso (kg)").apply {
                color = Color(0xFF3F51B5).toArgb()
                valueTextColor = android.graphics.Color.BLACK
                lineWidth = 2f
                circleRadius = 5f
                setCircleColor(Color(0xFF3F51B5).toArgb())
                valueTextSize = 10f
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            chart.apply {
                data = LineData(dataSet)
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(false)
                setScaleEnabled(false)
                axisRight.isEnabled = false
                xAxis.isEnabled = false
                axisLeft.apply {
                    textColor = android.graphics.Color.DKGRAY
                    textSize = 12f
                }
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    300
                )
                invalidate()
            }

            chart
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}
