package com.bobabelga.asteroidradar.ui.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bobabelga.asteroidradar.R
import com.bobabelga.asteroidradar.databinding.FragmentMainBinding
import com.bobabelga.asteroidradar.room.AsteroidAdapter
import com.bobabelga.asteroidradar.room.AsteroidDatabase
import com.bobabelga.asteroidradar.room.AsteroidEntity
import com.bobabelga.asteroidradar.room.AsteroidListener
import com.bobabelga.asteroidradar.ui.AsteroidViewModelFactory
import com.bobabelga.asteroidradar.ui.MainViewModel


class MainFragment : Fragment() {
    lateinit var viewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val asteroidDao = AsteroidDatabase.getInstance(application).AsteroidDao
        val asteroidViewModelFactory = AsteroidViewModelFactory(asteroidDao, application)

        viewModel =
            ViewModelProvider(this, asteroidViewModelFactory).get(MainViewModel::class.java)

        val adapter = AsteroidAdapter(AsteroidListener { asteroidId ->
            val asteroid = viewModel.listAsteroidLiveData.value?.find {
                it.id == asteroidId
            }
            this.findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailFragment(
                    asteroid!!
                )
            )
        })
        binding.asteroidRecycler.adapter = adapter
        viewModel.listAsteroidLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNullOrEmpty()){
                    viewModel.populateRoomDb(asteroidDao)
                    Log.d(ContentValues.TAG, "listAsteroidLiveData Null")
                } else {
                    adapter.submitList(it)
                    binding.statusLoadingWheel.visibility = View.GONE
                    Log.d(ContentValues.TAG, "listAsteroidLiveData Not Null")
                }
            }
        })
        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
           it?.let {
                if (it.url == "") {
                    Log.d(ContentValues.TAG, "pictureOfDay Null")
                    viewModel.addAPictureOfDay()
                } else {
                    Log.d(ContentValues.TAG, "pictureOfDay Not Null")
                    binding.picofday = it
                }
            }

        })

        setHasOptionsMenu(true)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_all_menu -> {
                viewModel.getWeekAsteroid()
                true
            }
            R.id.show_rent_menu -> {
                viewModel.getTodayAsteroid()
                true
            }
            R.id.show_buy_menu -> {
                viewModel.getWeekAsteroid()
                true
            }
            else -> false
        }
    }
}