import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatUtcMillisToString(utcMillis: Long): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")

    return formatter.format(Date(utcMillis))
}
