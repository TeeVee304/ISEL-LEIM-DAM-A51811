package dam_A51811.coolweatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador responsável por processar e apresentar a previsão meteorológica horária numa [RecyclerView].
 * @property hourlyData
 */
class HourlyAdapter(private val hourlyData: Hourly) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {
    /**
     * Cache que guarda as referências dos elementos visuais de um único item da lista.
     * Evita chamadas repetidas ao [View.findViewById] sempre que ocorre um scroll,
     * reciclando as vistas de itens antigos.
     * @param view Layout raiz do item individual da lista.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeText: TextView = view.findViewById(R.id.hourly_time)
        val iconImage: ImageView = view.findViewById(R.id.hourly_icon)
        val tempText: TextView = view.findViewById(R.id.hourly_temp)
    }

    /**
     * Invocado quando a [RecyclerView] necessita de um novo [ViewHolder] para representar um item.
     * @param parent O [ViewGroup] ao qual a nova [View] será anexada.
     * @param viewType O tipo de [View] do novo item (não será utilizado, neste caso).
     * @return Um novo [ViewHolder] contendo o layout "insuflado".
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_forecast, parent, false)
        return ViewHolder(view)
    }

    /**
     * Invocado pela [RecyclerView] para injetar os dados na vista de uma posição específica.*
     * @param holder O [ViewHolder] que deve ser atualizado com os conteúdos do item.
     * @param position A posição atual do item dentro da lista de dados.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context

        val fullTime = hourlyData.time[position]

        // Extract just the hour part ("14") and the full HH:mm ("14:00")
        val hourString = fullTime.substring(11, 13)
        holder.timeText.text = fullTime.substring(11, 16)

        // Convert the hour to an Integer to check if it's day or night
        val hourInt = hourString.toInt()
        val isDayHour = hourInt in 7..19 // Day is between 07:00 and 19:00

        // 2. Set the temperature
        holder.tempText.text = "${hourlyData.temperature_2m[position]}°"

        // 3. Get the correct image based on the specific hour
        val apiCode = hourlyData.weathercode[position]
        val codes = context.resources.getIntArray(R.array.weather_codes)
        val images = context.resources.getStringArray(R.array.weather_images)
        val index = codes.indexOf(apiCode)

        if (index != -1) {
            var imageName = images[index]

            // Check if this icon has Day/Night variants (ends with "_")
            if (imageName.endsWith("_")) {
                // Append "day" or "night" dynamically based on the current item's hour!
                imageName += if (isDayHour) "day" else "night"
            }
            val resID = context.resources.getIdentifier(imageName, "drawable", context.packageName)
            if (resID != 0) holder.iconImage.setImageResource(resID)
        }
    }
    // Mostramos apenas as próximas 24 horas para não sobrecarregar a lista
    override fun getItemCount() = 24
}