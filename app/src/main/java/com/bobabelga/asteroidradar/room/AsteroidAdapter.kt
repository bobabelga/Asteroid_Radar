package com.bobabelga.asteroidradar.room


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bobabelga.asteroidradar.databinding.AsteroidItemBinding

class AsteroidAdapter (val clickListener: AsteroidListener) : ListAdapter<AsteroidEntity, AsteroidAdapter.ViewHolder>(AsteroidDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clickListener,getItem(position)!!)
    }

    class ViewHolder private constructor(val binding: AsteroidItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AsteroidListener, item: AsteroidEntity) {
            binding.asteroid = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}
class AsteroidDiffCallback :
    DiffUtil.ItemCallback<AsteroidEntity>() {
    override fun areItemsTheSame(oldItem: AsteroidEntity, newItem: AsteroidEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AsteroidEntity, newItem: AsteroidEntity): Boolean {
        return oldItem == newItem
    }
}
class AsteroidListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(asteroid: AsteroidEntity) = clickListener(asteroid.id)
}

