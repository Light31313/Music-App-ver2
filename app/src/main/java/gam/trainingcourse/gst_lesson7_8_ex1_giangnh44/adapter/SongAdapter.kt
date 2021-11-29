package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.R
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.entity.Song


class SongAdapter(
    private val context: Context,
    private val iSongAdapter: ISongAdapter,
    private val songs: MutableList<Song>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedPos = RecyclerView.NO_POSITION

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtAuthor: TextView = itemView.findViewById(R.id.txt_author)
        val txtName: TextView = itemView.findViewById(R.id.txt_name)
        val imgSong: ImageView = itemView.findViewById(R.id.img_song)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val song = songs[position]
        holder as SongViewHolder

        holder.itemView.isSelected = selectedPos == position

        Glide.with(context).load(song.songImage).into(holder.imgSong)
        holder.txtAuthor.text = song.author
        holder.txtName.text = song.name
        holder.itemView.setOnClickListener {
            iSongAdapter.onClickItem(position)
            notifyItemChanged(selectedPos)
            selectedPos = holder.layoutPosition
            notifyItemChanged(selectedPos)
        }
    }

    override fun getItemCount(): Int = songs.size
}