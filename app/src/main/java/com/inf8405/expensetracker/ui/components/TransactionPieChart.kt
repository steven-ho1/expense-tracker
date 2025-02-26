package com.inf8405.expensetracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inf8405.expensetracker.models.TransactionGroup
import com.inf8405.expensetracker.utils.toColor
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import java.util.Locale

@Composable
fun TransactionPieChart(
    transactionGroups: List<TransactionGroup>,
    modifier: Modifier = Modifier
) {
    val data =
        transactionGroups.map {
            Pie(
                label = it.categoryName,
                data = it.contributionPercentage,
                color = it.categoryColor.toColor()
            )
        }


    Box(
        modifier = modifier.size(180.dp),
        contentAlignment = Alignment.Center
    ) {
        PieChart(
            modifier = modifier.fillMaxSize(),
            data = data,
            style = Pie.Style.Stroke(width = 25.dp),
            spaceDegree = 1.5f
        )

        Text(
            text = String.format(Locale.CANADA_FRENCH, "%.2f$", transactionGroups.sumOf { it.categoryTotalAmount }),
            fontSize = 16.sp
        )
    }
}
