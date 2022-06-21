package ru.ivmak.zerogame

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ivmak.zerogame.databinding.RvItemNumberBinding

class GameRvAdapter(
    private var items: MutableList<Number>
) : RecyclerView.Adapter<GameRvAdapter.ViewHolder>() {

    fun replace(list: List<Number>) {
        items = list.toMutableList()
        notifyDataSetChanged()
    }

    fun remove(id: Int) {
        val item = items.find { it.id == id }
        val position = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: RvItemNumberBinding = RvItemNumberBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class ViewHolder(private val binding: RvItemNumberBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Number) {
            binding.root.setOnClickListener {
                item.onClick()
                binding.rootLayout.animate().setDuration(100L).scaleX(0f).withEndAction {
                    binding.txtNumber.text = item.number.toString()
                    binding.rootLayout.animate().setDuration(100L).scaleX(1f)
                }
            }
            binding.txtNumber.text = "?"

            when (item.color) {
                0 -> binding.imgCircle.setImageResource(R.drawable.round_color_1)
                1 -> binding.imgCircle.setImageResource(R.drawable.round_color_2)
                2 -> binding.imgCircle.setImageResource(R.drawable.round_color_3)
                3 -> binding.imgCircle.setImageResource(R.drawable.round_color_4)
                4 -> binding.imgCircle.setImageResource(R.drawable.round_color_5)
                5 -> binding.imgCircle.setImageResource(R.drawable.round_color_6)
                6 -> binding.imgCircle.setImageResource(R.drawable.round_color_7)
                7 -> binding.imgCircle.setImageResource(R.drawable.round_color_8)
                8 -> binding.imgCircle.setImageResource(R.drawable.round_color_9)
                9 -> binding.imgCircle.setImageResource(R.drawable.round_color_10)
                10 -> binding.imgCircle.setImageResource(R.drawable.round_color_11)
                11 -> binding.imgCircle.setImageResource(R.drawable.round_color_12)
                12 -> binding.imgCircle.setImageResource(R.drawable.round_color_13)
                else -> binding.imgCircle.setImageResource(R.drawable.round_color_14)
            }
        }
    }

}