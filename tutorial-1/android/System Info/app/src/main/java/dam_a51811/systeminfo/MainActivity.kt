package dam_a51811.systeminfo

import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Extração das propriedades de hardware e software através do objeto android.os.Build.
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val brand = Build.BRAND
        val type = Build.TYPE
        val user = Build.USER
        val incremental = Build.VERSION.INCREMENTAL
        val sdk = Build.VERSION.SDK_INT
        val board = Build.BOARD
        val display = Build.DISPLAY

        // Formata a string com recurso aos placeholders (%s, %d) definidos na <string name="system_info">.
        val formattedInfo = getString(
            R.string.system_info,
            manufacturer, model, brand, type, user, incremental, sdk, board, display
        )

        // Localiza o widget 'MultiLine Tex't e atualiza o conteúdo com a formattedInfo.
        val infoDisplay = findViewById<EditText>(R.id.editTextTextMultiLine)
        infoDisplay.setText(formattedInfo)
    }
}