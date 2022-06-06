package com.cojayero.dogedex2.doglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.api.ApiResponseStatus
import com.cojayero.dogedex2.databinding.ActivityDogListBinding
import com.cojayero.dogedex2.dogDetailActivity.DogDetailActivity
import com.cojayero.dogedex2.dogDetailActivity.DogDetailActivity.Companion.DOG_KEY

private val TAG = DogListActivity::class.java.simpleName

class DogListActivity : AppCompatActivity() {
    private val dogListViewModel: DogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loadingWheel = binding.loadingWheel
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
        dogListViewModel.dogList.observe(this) { doglist ->
            adapter.submitList(doglist)
            Log.d(TAG, "onCreate: $doglist")
        }

        dogListViewModel.status.observe(this) { status ->
            when (status) {
                is ApiResponseStatus.Error -> {
                    loadingWheel.visibility = View.GONE
                    Toast.makeText(this,status.messageId, Toast.LENGTH_SHORT).show()
                }
                is ApiResponseStatus.Loading -> loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> loadingWheel.visibility = View.GONE
            }
        }

    }


}