package com.cojayero.dogedex2.doglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.databinding.ActivityDogListBinding
import com.cojayero.dogedex2.dogDetailActivity.DogDetailActivity
import com.cojayero.dogedex2.dogDetailActivity.DogDetailActivity.Companion.DOG_KEY

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
        adapter.setOnItemClicklistener {
            // pasar el dog a DogDetailActivity
            val intent = Intent(this, DogDetailActivity::class.java)
            intent.putExtra(DOG_KEY, it)
            startActivity(intent)
        }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        dogListViewModel.dogList.observe(this){
           doglist ->  adapter.submitList(doglist)
            Log.d(TAG, "onCreate: $doglist")
        }
    }


}