package pt.ipca.escutas.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.ipca.escutas.R

/**
 * Defines the splash screen activity.
 *
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}
