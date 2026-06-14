import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

val nowForSeeding = System.currentTimeMillis()

val cal = Calendar.getInstance()
cal.set(Calendar.HOUR_OF_DAY, 12)
val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
val offset = when (dayOfWeek) {
    Calendar.SUNDAY -> -6
    Calendar.MONDAY -> 0
    Calendar.TUESDAY -> -1
    Calendar.WEDNESDAY -> -2
    Calendar.THURSDAY -> -3
    Calendar.FRIDAY -> -4
    Calendar.SATURDAY -> -5
    else -> 0
}
cal.add(Calendar.DAY_OF_MONTH, offset)
val startOfWeek = cal.timeInMillis
val oneDay = 24 * 60 * 60 * 1000L

data class Task(val title: String, val focusDuration: Int, val completedAt: Long, val isCompleted: Boolean = true)

val completedTodayTasks = listOf(
    Task("Implementasi Dark Mode", 30, nowForSeeding),
    Task("Refactor Repository Layer", 75, nowForSeeding),
    Task("Testing Notification Feature", 15, nowForSeeding),
    Task("Perbaikan Profile UI", 60, nowForSeeding)
)

val historicalTasks = listOf(
    Task("Analisis Kebutuhan", 60, startOfWeek + 0 * oneDay),
    Task("Perancangan Arsitektur", 120, startOfWeek + 1 * oneDay),
    Task("Pembuatan Unit Testing", 45, startOfWeek + 2 * oneDay),
    Task("Integrasi Database", 180, startOfWeek + 3 * oneDay),
    Task("Desain Wireframe", 90, startOfWeek + 4 * oneDay),
    Task("Review Kode", 0, startOfWeek + 5 * oneDay),
    Task("Persiapan Rilis", 300, startOfWeek + 6 * oneDay)
)

val allCompletedTasks = completedTodayTasks + historicalTasks

val format = SimpleDateFormat("EEE, yyyy-MM-dd HH:mm", Locale.ENGLISH)

println("Dataset Currently Being Used (from Seeder Logic):")
allCompletedTasks.forEach { 
    println("- Title: ${it.title} | Duration: ${it.focusDuration}m | Completed At: ${format.format(it.completedAt)} (${it.completedAt})")
}

val totalCompleted = allCompletedTasks.size
val totalFocusMinutes = allCompletedTasks.sumOf { it.focusDuration }
val totalFocusHours = totalFocusMinutes / 60

val calToday = Calendar.getInstance()
calToday.set(Calendar.HOUR_OF_DAY, 0)
calToday.set(Calendar.MINUTE, 0)
calToday.set(Calendar.SECOND, 0)
calToday.set(Calendar.MILLISECOND, 0)
val startOfToday = calToday.timeInMillis
val endOfToday = startOfToday + oneDay

val todayFocusMinutes = allCompletedTasks.filter { it.completedAt in startOfToday until endOfToday }.sumOf { it.focusDuration }
val currentWeekFocusMinutes = allCompletedTasks.filter { it.completedAt in startOfWeek until (startOfWeek + 7 * oneDay) }.sumOf { it.focusDuration }

println("\nCalculated Totals:")
println("1. Total completed tasks: $totalCompleted")
println("2. Total focus minutes: $totalFocusMinutes")
println("3. Total focus hours: $totalFocusHours")
println("4. Today's focus minutes: $todayFocusMinutes")
println("5. Current week focus minutes: $currentWeekFocusMinutes")

