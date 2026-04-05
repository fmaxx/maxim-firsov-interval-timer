package run.simple.feature_training_screen.ui.components.interval

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green500 = Color(0xFF4CAF50)
private val BorderGray = Color(0xFFE5E7EB)

@Composable
fun IntervalItem(
    index: Int,
    title: String,
    timeSeconds: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Номер в зелёном круге ---
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Green500),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = index.toString(),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(14.dp))

            // --- Название интервала ---
            Text(
                text = title,
                color = Color.Black,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            // --- Длительность ---
            Text(
                text = formatDuration(timeSeconds),
                color = Green500,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun formatDuration(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%d:%02d".format(m, s)
}

// ---------- Preview ----------

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5, widthDp = 360)
@Composable
private fun IntervalItemPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IntervalItem(index = 1, title = "Ходьба в среднем темпе", timeSeconds = 300)
            IntervalItem(index = 2, title = "Ходьба в интенсивном темпе", timeSeconds = 300)
            IntervalItem(index = 3, title = "Ходьба в среднем темпе", timeSeconds = 120)
            IntervalItem(index = 4, title = "Медленный бег", timeSeconds = 30)
        }
    }
}