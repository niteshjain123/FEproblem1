package com.nitesh.findingfalcone.presentation.find

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nitesh.findingfalcone.R
import com.nitesh.findingfalcone.application.extensions.getViewModel
import com.nitesh.findingfalcone.application.extensions.observe
import com.nitesh.findingfalcone.domain.model.FindResponse
import com.nitesh.findingfalcone.domain.model.Planet
import com.nitesh.findingfalcone.domain.model.Vehicle
import com.nitesh.findingfalcone.presentation.find.FindViewModel.Loading
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_find.*
import kotlinx.android.synthetic.main.activity_find.btnFind
import kotlinx.android.synthetic.main.activity_find.progressLoading
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class FindActivity : AppCompatActivity() {

    companion object {
        private const val PLANETS = "planets"
        private const val VEHICLES = "vehicles"

        fun intent(context: Context, planets: ArrayList<Planet>, vehicles: ArrayList<Vehicle>,totalTime : Int) =
            Intent(context, FindActivity::class.java).apply {
                putExtra(PLANETS, planets)
                putExtra("TOTALTIME", totalTime)
                putParcelableArrayListExtra(VEHICLES, vehicles)
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: FindViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        viewModel = getViewModel(viewModelFactory)
        viewModel.message.observe(this, onChange = ::showMessage)
        viewModel.viewState.observe(this, onChange = ::renderView)
        viewModel.loading.observe(this, onChange = ::renderLoading)

        val selectedPlanet =
            requireNotNull(intent.getParcelableArrayListExtra<Planet>(PLANETS)) { "Planets list not provided" }
        val selectedVehicle =
            requireNotNull(intent.getParcelableArrayListExtra<Vehicle>(VEHICLES)) { "Vehicle list not provided" }
        viewModel.init(selectedPlanet, selectedVehicle)

        btnFind.setOnClickListener { viewModel.onFindClicked(this) }
    }


    private fun renderView(state: FindResponse) {
        if(state is FindResponse.Success)
        {
            planetFound.visibility = View.VISIBLE
            timeTaken.visibility = View.VISIBLE
            resultText.text = getString(R.string.status_message)
            planetFound.text = getString(R.string.planet_found_result,state.planetName)
            timeTaken.text = getString(R.string.time_taken_result, intent.getIntExtra("TOTALTIME",0).toString())
        }
        else
        {
            planetFound.visibility = View.GONE
            timeTaken.visibility = View.GONE
            resultText.text = getString(R.string.princes_not_found)
        }
    }

    private fun renderLoading(loading: Loading) {
        when (loading) {
            Loading.Show -> progressLoading.visibility = View.VISIBLE
            Loading.Hide -> progressLoading.visibility = View.GONE
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


}