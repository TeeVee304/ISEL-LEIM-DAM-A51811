package dam_A51811.missionimpossiblepossible2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dam_A51811.missionimpossiblepossible2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CatImageAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.swipeRefreshLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        observeViewModel()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadImages()
        }

        // Initial load
        viewModel.loadImages()
    }

    private fun setupRecyclerView() {
        adapter = CatImageAdapter { catImage ->
            val intent = android.content.Intent(this, ImageDetailsActivity::class.java).apply {
                putExtra(ImageDetailsActivity.EXTRA_IMAGE_URL, catImage.url)
            }
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.images.observe(this) { images ->
            adapter.submitList(images)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.swipeRefreshLayout.isRefreshing = isLoading
            if (isLoading && adapter.currentList.isEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            if (errorMsg != null) {
                Toast.makeText(this, "Error: $errorMsg", Toast.LENGTH_LONG).show()
            }
        }
    }
}