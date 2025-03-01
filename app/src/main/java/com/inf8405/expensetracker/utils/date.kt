import android.os.Build
import androidx.annotation.RequiresApi
import com.inf8405.expensetracker.database.entities.TransactionEntity
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatUtcMillisToString(utcMillis: Long): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")

    return formatter.format(Date(utcMillis))
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertUnixTimestampToLocalDateTime(timestamp: Long): LocalDateTime {
    return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.UTC).toLocalDateTime()
}

@RequiresApi(Build.VERSION_CODES.O)
fun filterTransactionsByPeriod(
    transactions: List<TransactionEntity>,
    periodIndex: Int
): List<TransactionEntity> {
    val currentDate = LocalDate.now()

    return when (periodIndex) {
        0 -> { // Jour
            transactions.filter {
                val transactionDate = convertUnixTimestampToLocalDateTime(it.date)
                transactionDate.toLocalDate().isEqual(currentDate)
            }
        }

        1 -> { // Semaine
            val startOfWeek =
                currentDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY))
            val endOfWeek = startOfWeek.plusDays(6)
            transactions.filter {
                val transactionDate = convertUnixTimestampToLocalDateTime(it.date)
                val transactionLocalDate = transactionDate.toLocalDate()
                transactionLocalDate.isAfter(startOfWeek.minusDays(1)) && transactionLocalDate.isBefore(
                    endOfWeek.plusDays(
                        1
                    )
                )
            }
        }

        2 -> { // Mois
            val startOfMonth = currentDate.withDayOfMonth(1)
            val endOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())
            transactions.filter {
                val transactionDate = convertUnixTimestampToLocalDateTime(it.date)
                val transactionLocalDate = transactionDate.toLocalDate()
                transactionLocalDate.isAfter(startOfMonth.minusDays(1)) && transactionLocalDate.isBefore(
                    endOfMonth.plusDays(1)
                )
            }
        }

        3 -> { // Année
            val startOfYear = currentDate.withDayOfYear(1)
            val endOfYear = currentDate.withDayOfYear(currentDate.lengthOfYear())
            transactions.filter {
                val transactionDate = convertUnixTimestampToLocalDateTime(it.date)
                val transactionLocalDate = transactionDate.toLocalDate()
                transactionLocalDate.isAfter(startOfYear.minusDays(1)) && transactionLocalDate.isBefore(
                    endOfYear.plusDays(1)
                )
            }
        }

        else -> transactions
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getPeriodText(periodIndex: Int): String {
    val currentDate = LocalDate.now()
    return when (periodIndex) {
        0 -> "${currentDate.dayOfMonth} ${formatMonth(currentDate.month)}"
        1 -> {
            val startOfWeek =
                currentDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY))
            val endOfWeek = startOfWeek.plusDays(6)
            "${startOfWeek.dayOfMonth} ${formatMonth(startOfWeek.month)} - ${endOfWeek.dayOfMonth} ${
                formatMonth(
                    endOfWeek.month
                )
            }"
        }
        2 -> "${formatMonth(currentDate.month)} ${currentDate.year}"
        3 -> "${currentDate.year}"
        else -> ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatMonth(month: Month): String {
    return month.getDisplayName(TextStyle.SHORT, Locale.CANADA_FRENCH)
        .replaceFirstChar { it.uppercase() }
}