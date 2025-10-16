package com.julietgisemba.kipesa.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.julietgisemba.kipesa.model.TransactionEntity
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TransactionItem(transactionEntity: TransactionEntity, onClick: () -> Unit = {}) {
    val shortFormatter = SimpleDateFormat("MMM", Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Icon(transactionEntity.icon, contentDescription = "")
        Spacer(Modifier.width(20.dp))
        Column {
            Text(transactionEntity.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text("${transactionEntity.date.day} ${shortFormatter.format(transactionEntity.date)} - ${transactionEntity.category}", fontSize = 14.sp, fontWeight = FontWeight.Light)
        }
        Spacer(Modifier.weight(1f))
        Text("$${transactionEntity.amount}", fontSize = 16.sp, color = if (transactionEntity.isIncome) Color. Green else Color. Red, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.End)
    }
}