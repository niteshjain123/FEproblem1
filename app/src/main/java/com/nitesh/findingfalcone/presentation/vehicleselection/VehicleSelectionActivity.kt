package com.nitesh.findingfalcone.presentation.vehicleselection

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitesh.findingfalcone.R
import com.nitesh.findingfalcone.application.extensions.getViewModel
import com.nitesh.findingfalcone.application.extensions.observe
import com.nitesh.findingfalcone.domain.model.Planet
import com.nitesh.findingfalcone.domain.model.Vehicle
import com.nitesh.findingfalcone.presentation.vehicleselection.VehicleSelectionViewModel.NavigationEvent
import com.nitesh.findingfalcone.presentation.vehicleselection.VehicleSelectionViewModel.ViewState
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_vehicle_selection.*
import javax.inject.Inject


class VehicleSelectionActivity : AppCompatActivity() {

    companion object {
        const val PLANET = "planet"
        const val VEHICLE = "vehicle"
        const val DISTANCE = "distance"
        const val SPEED = "speed"

        fun intent(context: Context, planet: Planet, vehicles: ArrayList<Vehicle>) =
            Intent(context, VehicleSelectionActivity::class.java).apply {
                putExtra(PLANET, planet)
                putParcelableArrayListExtra(VEHICLE, vehicles)
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: VehicleSelectionViewModel

    private val programsAdapter by lazy { VehicleAdapter(viewModel::onItemClicked) }


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_selection)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = getViewModel(viewModelFactory)
        viewModel.viewState.observe(this, onChange = ::renderView)
        viewModel.navigation.observe(this, onChange = ::navigate)

        vehicleRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this.context)
            adapter = programsAdapter
        }
        vehicleRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        val selectedPlanet = requireNotNull(intent.getParcelableExtra<Planet>(PLANET)) { "no planet selected" }
        viewModel.init(selectedPlanet, intent.getParcelableArrayListExtra(VEHICLE) ?: arrayListOf())
    }

    private fun renderView(state: ViewState) {
        emptyText.visibility = View.INVISIBLE
        vehicleRecyclerView.visibility = View.VISIBLE
        programsAdapter.updateList(state.vehicles)
    }

    private fun navigate(event: NavigationEvent) {
        val intent = Intent().apply {
            putExtra(PLANET, event.planet)
            putExtra(VEHICLE, event.vehicle)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}