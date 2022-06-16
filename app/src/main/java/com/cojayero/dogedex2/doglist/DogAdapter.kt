package com.cojayero.dogedex2.doglist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.databinding.DogListItemBinding
private val TAG = DogAdapter::class.java.simpleName
class DogAdapter:ListAdapter<Dog, DogAdapter.DogViewHolder>(DiffCallback) {
    companion object DiffCallback:DiffUtil.ItemCallback<Dog>(){
        override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean {
            return oldItem.id == newItem.id
        }
    }


    private var onItemClickListener: ((Dog) -> Unit)? = null
    fun setOnItemClicklistener(onItemClickListener: (Dog)-> Unit){
        this.onItemClickListener = onItemClickListener
    }


    private var onLongItemClickListener: ((Dog) -> Unit)? = null
    fun setOnLongItemClickListener(onLongClickListener: (Dog)->Unit)
    {
        Log.d(TAG, "setOnLongItemClickListener: ")
        this.onLongItemClickListener = onLongClickListener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val binding = DogListItemBinding.inflate(LayoutInflater.from(parent.context))
        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = getItem(position)
        holder.bind(dog)
    }

    inner class DogViewHolder(private val binding:DogListItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(dog:Dog){
            if (dog.inCollection) {
                binding.dogListItemLayout.background = ContextCompat.getDrawable(
                    binding.dogImage.context,
                    R.drawable.dog_list_item_background
                )
                binding.dogName.text = dog.name
                binding.dogImage.load(dog.imageURL)
                binding.dogListItemLayout.setOnClickListener {
                    Log.d(TAG, "bind: setOnClickListener ${dog.id}")
                    onItemClickListener?.invoke(dog)
                }

            } else {
                binding.dogImage.visibility = View.GONE
                binding.dogListItemLayout.background = ContextCompat.getDrawable(
                    binding.dogImage.context,
                    R.drawable.dog_list_item_null_background
                )
                binding.dogName.visibility = View.VISIBLE
                binding.dogName.text = dog.index.toString()
                /*
                binding.dogListItemLayout.setOnLongClickListener {
                    Log.d(TAG, "bind: setOnLognClickListener ${dog.id}")
                    onLongItemClickListener?.invoke(dog)
                    true
                }

                 */
            }
        }

    }

}