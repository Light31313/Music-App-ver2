package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.model.Song
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.databinding.ItemSongBinding

class SongAdapter(
    private val iSongAdapter: ISongAdapter
) : ListAdapter<Song, SongAdapter.SongViewHolder>(diffCallback) {

    private var selectedPos = RecyclerView.NO_POSITION

    class SongViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.song = song
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(currentList[position])

        holder.itemView.isSelected = selectedPos == position
        holder.itemView.setOnClickListener {
            iSongAdapter.onClickItem(position)
            notifyItemChanged(selectedPos)
            selectedPos = holder.layoutPosition
            notifyItemChanged(selectedPos)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Song>() {
            override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem == newItem
            }
        }
    }


}

