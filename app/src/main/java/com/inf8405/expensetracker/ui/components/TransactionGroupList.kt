package com.inf8405.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inf8405.expensetracker.models.TransactionGroup
import com.inf8405.expensetracker.utils.toColor
import java.util.Locale

@Composable
fun TransactionGroupList(transactionGroups: List<TransactionGroup>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .padding(vertical = 20.dp)
            .fillMaxWidth()
    ) {
        transactionGroups.forEach { transactionGroup ->
            item {
                ElevatedCard(
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(transactionGroup.categoryColor.toColor())
                        )

                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp),
                        ) {
                            Text(
                                text = transactionGroup.categoryName,
                                modifier = modifier.weight(1f),
                            )

                            Text(
                                text = String.format(
                                    Locale.CANADA_FRENCH,
                                    "%.2f%%",
                                    transactionGroup.contributionPercentage
                                ),
                                modifier = modifier.width(70.dp),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = String.format(
                                    Locale.CANADA_FRENCH,
                                    "%.2f$",
                                    transactionGroup.categoryTotalAmount
                                ),
                                modifier = modifier.width(90.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = modifier.height(10.dp))
            }
        }
    }
}