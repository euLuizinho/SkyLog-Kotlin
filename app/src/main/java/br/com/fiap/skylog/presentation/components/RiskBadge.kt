package br.com.fiap.skylog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.skylog.domain.model.NivelRisco
import br.com.fiap.skylog.core.theme.*

@Composable
fun RiskBadge(nivelRisco: NivelRisco, modifier: Modifier = Modifier) {
    val (backgroundColor, label) = when (nivelRisco) {
        NivelRisco.CRITICAL -> RiskCritical to "CRÍTICO"
        NivelRisco.HIGH -> RiskHigh to "ALTO"
        NivelRisco.MEDIUM -> RiskMedium to "MÉDIO"
        NivelRisco.LOW -> RiskLow to "BAIXO"
    }

    Text(
        text = label,
        color = Color.White,
        fontSize = 10.sp,
        style = Typography.labelSmall,
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
