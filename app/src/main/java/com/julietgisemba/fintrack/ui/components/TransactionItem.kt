package com.julietgisemba.fintrack.ui.components

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
import com.julietgisemba.fintrack.model.TransactionEntity
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TransactionItem(
    transaction: TransactionEntity,
    onClick: (TransactionEntity) -> Unit
) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick(transaction) }, // 👈 make item clickable
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = transaction.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = transaction.category, style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = "${transaction.amount}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
