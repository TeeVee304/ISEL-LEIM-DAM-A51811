package dam_A51811.coolweatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

// O Adapter recebe os dados diários
class DailyAdapter(private val dailyData: Daily) : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayText: TextView = view.findViewById(R.id.daily_day)
        val iconImage: ImageView = view.findViewById(R.id.daily_icon)
        val maxTempText: TextView = view.findViewById(R.id.daily_max_temp)
        val minTempText: TextView = view.findViewById(R.id.daily_min_temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context

        // 1. Format the date (From "2026-04-12" to "Monday" or "Segunda-feira" based on device language)
        try {
            val parseFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = parseFormat.parse(dailyData.time[position])

            // USE Locale.getDefault() to respect the phone's language settings
            val displayFormat = SimpleDateFormat("EEEE", Locale.getDefault())

            holder.dayText.text = displayFormat.format(date!!).replaceFirstChar { it.uppercase() }
        } catch (e: Exception) {
            holder.dayText.text = dailyData.time[position] // Fallback in case of error
        }

        // 2. Temperaturas Max e Min
        holder.maxTempText.text = "${dailyData.temperature_max[position]}°"
        holder.minTempText.text = "${dailyData.temperature_min[position]}°"

        // 3. Ícone
        val apiCode = dailyData.weathercode[position]
        val codes = context.resources.getIntArray(R.array.weather_codes)
        val images = context.resources.getStringArray(R.array.weather_images)
        val index = codes.indexOf(apiCode)

        if (index != -1) {
            var imageName = images[index]
            // Só adiciona "day" se o nome terminar em "_"
            if (imageName.endsWith("_")) {
                imageName += "day"
            }

            val resID = context.resources.getIdentifier(imageName, "drawable", context.packageName)
            if (resID != 0) holder.iconImage.setImageResource(resID)
        }
    }

    // A API envia normalmente 7 dias por defeito
    override fun getItemCount() = dailyData.time.size
}