package com.nitesh.findingfalcone.application.injection.modules

import androidx.lifecycle.ViewModel
import com.nitesh.findingfalcone.application.injection.ViewModelKey
import com.nitesh.findingfalcone.data.ServiceManager
import com.nitesh.findingfalcone.domain.Repository
import com.nitesh.findingfalcone.presentation.MainActivity
import com.nitesh.findingfalcone.presentation.MainViewModel
import com.nitesh.findingfalcone.presentation.find.FindActivity
import com.nitesh.findingfalcone.presentation.find.FindViewModel
import com.nitesh.findingfalcone.presentation.vehicleselection.VehicleSelectionActivity
import com.nitesh.findingfalcone.presentation.vehicleselection.VehicleSelectionViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {

    @Binds
    abstract fun bindRepository(manager: ServiceManager): Repository

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun bindVehicleSelectionActivity(): VehicleSelectionActivity

    @Binds
    @IntoMap
    @ViewModelKey(VehicleSelectionViewModel::class)
    abstract fun bindVehicleSelectionViewModel(viewModel: VehicleSelectionViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun bindFindActivity(): FindActivity

    @Binds
    @IntoMap
    @ViewModelKey(FindViewModel::class)
    abstract fun bindFindViewModel(viewModel: FindViewModel): ViewModel
}