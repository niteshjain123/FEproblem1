package com.nitesh.findingfalcone.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nitesh.findingfalcone.R
import com.nitesh.findingfalcone.application.extensions.getViewModel
import com.nitesh.findingfalcone.application.extensions.observe
import com.nitesh.findingfalcone.domain.model.Planet
import com.nitesh.findingfalcone.domain.model.Vehicle
import com.nitesh.findingfalcone.presentation.MainViewModel.Loading
import com.nitesh.findingfalcone.presentation.MainViewModel.NavigationEvent
import com.nitesh.findingfalcone.presentation.MainViewModel.ViewState
import com.nitesh.findingfalcone.presentation.find.FindActivity
import com.nitesh.findingfalcone.presentation.vehicleselection.VehicleSelectionActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    var oldTime = 0;
    var totalTime = 0;
    var selectedPlanetDistance = 0;
    var selectedVehicleSpeed = 0;

    companion object {
        private const val VEHICLE_SELECT_REQUEST = 123
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = getViewModel(viewModelFactory)
        viewModel.message.observe(this, onChange = ::showMessage)
        viewModel.viewState.observe(this, onChange = ::renderView)
        viewModel.loading.observe(this, onChange = ::renderLoading)
        viewModel.navigation.observe(this, onChange = ::navigate)

        planet1Image.setOnClickListener { viewModel.onPlanetClicked(planet1Name.text.toString()) }
        planet2Image.setOnClickListener { viewModel.onPlanetClicked(planet2Name.text.toString()) }
        planet3Image.setOnClickListener { viewModel.onPlanetClicked(planet3Name.text.toString()) }
        planet4Image.setOnClickListener { viewModel.onPlanetClicked(planet4Name.text.toString()) }
        planet5Image.setOnClickListener { viewModel.onPlanetClicked(planet5Name.text.toString()) }
        planet6Image.setOnClickListener { viewModel.onPlanetClicked(planet6Name.text.toString()) }

        btnFind.setOnClickListener { viewModel.onFindClicked() }

        viewModel.init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && resultCode == Activity.RESULT_OK && requestCode == VEHICLE_SELECT_REQUEST) {
            val planet =
                requireNotNull(data.getParcelableExtra<Planet>(VehicleSelectionActivity.PLANET)) { "planet is missing" }
            viewModel.onActivityResult(
                planet,
                data.getParcelableExtra(VehicleSelectionActivity.VEHICLE)
            )
            val vehicle =
                requireNotNull(data.getParcelableExtra<Vehicle>(VehicleSelectionActivity.VEHICLE)) { "Vehicle not loading" }
            viewModel.onActivityResult(
                planet,
                data.getParcelableExtra(VehicleSelectionActivity.VEHICLE)
            )
            selectedPlanetDistance = planet.distance
            selectedVehicleSpeed = vehicle.speed

            oldTime = totalTime + calculateTime(selectedPlanetDistance,selectedVehicleSpeed)
            totalTime = oldTime
        }
    }

    private fun renderView(state: ViewState) {
        val planets = state.planetVehicle.keys.toList()
        planet1Image.visibility = View.VISIBLE
        planet1Name.visibility = View.VISIBLE
        planet1Dist.visibility = View.VISIBLE
        planet1Shuttle.visibility = View.VISIBLE
        planet1Name.text = planets[0].name
        planet1Dist.text = "( "+planets[0].distance.toString()+" megamiles )"
        state.planetVehicle[planets[0]]?.let { planet1Shuttle.setImageResource(it.getImage()) }

        planet2Image.visibility = View.VISIBLE
        planet2Name.visibility = View.VISIBLE
        planet2Dist.visibility = View.VISIBLE
        planet2Shuttle.visibility = View.VISIBLE
        planet2Name.text = planets[1].name
        planet2Dist.text = "( "+planets[1].distance.toString()+" megamiles )"
        state.planetVehicle[planets[1]]?.let { planet2Shuttle.setImageResource(it.getImage()) }

        planet3Image.visibility = View.VISIBLE
        planet3Name.visibility = View.VISIBLE
        planet3Dist.visibility = View.VISIBLE
        planet3Shuttle.visibility = View.VISIBLE
        planet3Name.text = planets[2].name
        planet3Dist.text = "( "+planets[2].distance.toString()+" megamiles )"
        state.planetVehicle[planets[2]]?.let { planet3Shuttle.setImageResource(it.getImage()) }

        planet4Image.visibility = View.VISIBLE
        planet4Name.visibility = View.VISIBLE
        planet4Dist.visibility = View.VISIBLE
        planet4Shuttle.visibility = View.VISIBLE
        planet4Name.text = planets[3].name
        planet4Dist.text = "( "+planets[3].distance.toString()+" megamiles )"
        state.planetVehicle[planets[3]]?.let { planet4Shuttle.setImageResource(it.getImage()) }

        planet5Image.visibility = View.VISIBLE
        planet5Name.visibility = View.VISIBLE
        planet5Dist.visibility = View.VISIBLE
        planet5Shuttle.visibility = View.VISIBLE
        planet5Name.text = planets[4].name
        planet5Dist.text = "( "+planets[4].distance.toString()+" megamiles )"
        state.planetVehicle[planets[4]]?.let { planet5Shuttle.setImageResource(it.getImage()) }

        planet6Image.visibility = View.VISIBLE
        planet6Name.visibility = View.VISIBLE
        planet6Dist.visibility = View.VISIBLE
        planet6Shuttle.visibility = View.VISIBLE
        planet6Name.text = planets[5].name
        planet6Dist.text = "( "+planets[5].distance.toString()+" megamiles )"
        state.planetVehicle[planets[5]]?.let { planet6Shuttle.setImageResource(it.getImage()) }

        btnFind.isEnabled = state.isFindEnabled
        if(btnFind.isEnabled)
        {
            btnFind.setBackgroundColor(resources.getColor(R.color.colorWhite))
            btnFind.setTextColor(resources.getColor(R.color.colorPrimary))
        }
    }

    private fun renderLoading(loading: Loading) {
        when (loading) {
            Loading.Show -> progressLoading.visibility = View.VISIBLE
            Loading.Hide -> progressLoading.visibility = View.GONE
        }
    }

    private fun navigate(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.ShowVehicleSelection ->
                startActivityForResult(
                    VehicleSelectionActivity.intent(
                        this,
                        planet = event.selectedPlanet,
                        vehicles = event.vehicles
                    ),
                    VEHICLE_SELECT_REQUEST
                )

            is NavigationEvent.ShowResult -> {
                startActivity(FindActivity.intent(this, event.planets, event.vehicles,totalTime))
                finish()
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun calculateTime(distance: Int, speed: Int) : Int {
        return  (distance/speed)
    }
}