package dam_A51811.missionimpossiblepossible2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import dam_A51811.missionimpossiblepossible2.databinding.ActivityImageDetailsBinding

class ImageDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityImageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        if (imageUrl != null) {
            Glide.with(this)
                .load(imageUrl)
                .into(binding.detailImageView)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val EXTRA_IMAGE_URL = "extra_image_url"
    }
}
