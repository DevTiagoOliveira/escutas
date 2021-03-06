package pt.ipca.escutas.views

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.SystemClock
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import de.hdodenhof.circleimageview.CircleImageView
import pt.ipca.escutas.R
import pt.ipca.escutas.controllers.ProfileController
import pt.ipca.escutas.models.User
import pt.ipca.escutas.resources.Strings
import pt.ipca.escutas.services.callbacks.GenericCallback
import java.util.Calendar

/**
 * The profile controller.
 */
private val profileController by lazy { ProfileController() }

/**
 * Double click counter.
 */
private var mLastClickTime: Long = 0

/**
 * Defines the profile fragment.
 *
 */
class ProfileActivity : AppCompatActivity() {
    /**
     * Invoked when the activity is starting.
     *
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // toolbar
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        val imageLayout = findViewById<CircleImageView>(R.id.frameLayout_circleimage)

        val emailText = findViewById<TextView>(R.id.textView_emailUser)
        val nameText = findViewById<TextView>(R.id.textView_nameUser)
        val birthdayText = findViewById<TextView>(R.id.textView_birthdayUser)
        val groupText = findViewById<TextView>(R.id.textView_groupUser)

        profileController.getUser(object : GenericCallback {
            override fun onCallback(value: Any?) {
                if (value != null) {
                    var user = value as User
                    nameText.text = user.name
                    emailText.text = user.email
                    var calendar = Calendar.getInstance()
                    calendar.time = user.birthday
                    birthdayText.text =
                        calendar[Calendar.DAY_OF_MONTH].toString() + "-" + calendar[Calendar.MONTH].toString() + "-" + calendar[Calendar.YEAR]
                    groupText.text = user.groupName
                    if (user.photo != null && user.photo != "") {
                        profileController.getUserImage(
                            user.photo,
                            object : GenericCallback {
                                override fun onCallback(value: Any?) {
                                    if (value != null) {
                                        var image = value as Bitmap
                                        imageLayout.setImageBitmap(image)
                                        profileController.saveImage(image)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        })

        toolbar.title = Strings.MSG_PERSONAL_AREA_ACT_TITLE
        setSupportActionBar(toolbar)

        // add back arrow to toolbar
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        val logoutButton = findViewById<Button>(R.id.Button_logout)

        logoutButton.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            profileController.logoutUser(this@ProfileActivity)
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * This method implements return toolbar action.
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
