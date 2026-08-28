package com.educalab.exploravida.ui.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.ui.components.StarRow
import com.educalab.exploravida.ui.theme.LabColors

/** Cabecera comun de las actividades: situacion real + estrellas ganadas. */
@Composable
fun ActivityHeader(
    title: String,
    situation: String,
    stars: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(LabColors.GlassSoft)
                    .clickable { onBack() }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("Volver", style = MaterialTheme.typography.labelLarge, color = LabColors.Sand)
            }
            Spacer(Modifier.width(12.dp))
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall,
                color = LabColors.Paper,
                modifier = Modifier.weight(1f)
            )
            StarRow(stars)
        }
        if (situation.isNotBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                situation,
                style = MaterialTheme.typography.bodySmall,
                color = LabColors.Sand.copy(alpha = 0.9f),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
