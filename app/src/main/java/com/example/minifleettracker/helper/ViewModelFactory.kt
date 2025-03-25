package com.example.minifleettracker.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.minifleettracker.data.repository.MiniFleetTrackerRepository
import androidx.lifecycle.ViewModelProvider
import com.example.minifleettracker.viewmodel.FleetViewModel

class ViewModelFactory private constructor(private val repository : MiniFleetTrackerRepository) :
        ViewModelProvider.NewInstanceFactory() {
            companion object {
                @Volatile
                private var INSTANCE: ViewModelFactory? = null

                @JvmStatic
                fun getInstance(context : Context): ViewModelFactory {
                    if (INSTANCE == null) {
                        synchronized(ViewModelFactory::class.java) {
                            INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                        }
                    }
                    return INSTANCE as ViewModelFactory
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass : Class<T>) : T {
                if (modelClass.isAssignableFrom(FleetViewModel::class.java)) {
                    return FleetViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }