package dam_A51811.missionimpossiblepossible2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dam_A51811.missionimpossiblepossible2.databinding.ItemCatImageBinding
import dam_A51811.missionimpossiblepossible2.model.CatImageItem

class CatImageAdapter(private val onImageClicked: (CatImageItem) -> Unit) : ListAdapter<CatImageItem, CatImageAdapter.CatImageViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatImageViewHolder {
        val binding = ItemCatImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onImageClicked(item)
        }
    }

    class CatImageViewHolder(private val binding: ItemCatImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(catImage: CatImageItem) {
            Glide.with(binding.root.context)
                .load(catImage.url)
                .centerCrop()
                .into(binding.catImageView)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CatImageItem>() {
        override fun areItemsTheSame(oldItem: CatImageItem, newItem: CatImageItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CatImageItem, newItem: CatImageItem): Boolean {
            return oldItem == newItem
        }
    }
}
