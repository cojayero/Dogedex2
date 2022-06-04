package com.cojayero.dogedex2.doglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.databinding.ActivityDogListBinding
private val TAG = DogListActivity::class.java.simpleName
class DogListActivity : AppCompatActivity() {
    private val dogListViewModel:DogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recycler = binding.dogRecycler
        val viewModel = DogListViewModel()
        val adapter = DogAdapter()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        dogListViewModel.dogList.observe(this){
           doglist ->  adapter.submitList(doglist)
            Log.d(TAG, "onCreate: $doglist")
        }
    }


}