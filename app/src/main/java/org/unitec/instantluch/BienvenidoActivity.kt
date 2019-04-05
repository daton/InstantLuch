package org.unitec.instantluch

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_bienvenido.*

class BienvenidoActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback  {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var mPermissionDenied = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    var miLati: Double? = null
    var miLongi: Double? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenido)
        supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        //La location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    // ...
                }
            }
        }

        enableMyLocation()
       obtenerUbicacion("")

       //Simulamos que ya levantamos el registro el registro
        //y la orden de comida
        registrar.setOnClickListener {
         //INvocamos el metodo obtenerLocalizacion

        }

       comprar.setOnClickListener {


           var i=Intent(this, MapsActivity::class.java)
           startActivity(i)
       }



    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(
                this, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            mPermissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(supportFragmentManager, "dialog")
    }

    /***
     * ESTE METODO DE OBTENER UBICACION ES SUMAMEMTE IMPORTANTE, ES EL QUE NOS VA  Y SE INVOCA
     * EN LA CLASE INTERNA DE REGISTRARSE PARA QUE NOS DE EN AUTOMÁTICO LA GEOLOCALIZACION
     * CON EL THREAD QUE ESTA CONSTANTEMENTE ACTUALIZANDOSE
     */
    @SuppressLint("MissingPermission")
    fun obtenerUbicacion(momento: String) {


        // Este es otro cdigo

        val locationRequest = LocationRequest().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);


        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                println("PPPPEEEEERRRRRROOOO");

                miLati = location?.latitude
                miLongi = location?.longitude
                Globales.lati=miLati
                Globales.longi=miLongi

             //   Toast.makeText(
             //       applicationContext,
             //       "Loca " + location?.latitude + " Longi" + location?.longitude + " alti " + location?.altitude
             //       ,
             //       Toast.LENGTH_LONG
            //    ).show()


            }

        println("ACEPTADO");

    }
}
