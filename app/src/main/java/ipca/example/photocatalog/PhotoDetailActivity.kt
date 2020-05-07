package ipca.example.photocatalog

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import ipca.example.photocatalog.models.AppDatabase
import ipca.example.photocatalog.models.Converters
import ipca.example.photocatalog.models.PhotoItem
import kotlinx.android.synthetic.main.activity_photo_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class PhotoDetailActivity : AppCompatActivity() {

    private var bitmap : Bitmap? = null
    private var date : Date? = null
    private var client : FusedLocationProviderClient? =null
    private var latitude : Double? = null
    private var longitude : Double? = null
    private var photoPath : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        floatingActionButtonTakePhoto.setOnClickListener {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_CODE_PHOTO)


        }



        client  = LocationServices.getFusedLocationProviderClient(this)

        client!!.lastLocation.addOnSuccessListener {location ->
            latitude = location.latitude
            longitude = location.longitude
            textViewLatitude.text =  "$latitude"
            texViewLongitude.text = "$longitude"
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_photo,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add){
            val intentResult = Intent()

            val photoItem = PhotoItem()
            photoItem.description = editTextDescription.text.toString()
            photoItem.date = date!!
            photoItem.filePath = photoPath
            photoItem.gpsLatitude = latitude
            photoItem.gpsLongitude = longitude

            doAsync{
                AppDatabase.getDatabase(this@PhotoDetailActivity)?.photoItemDao()?.insert(photoItem)
                uiThread {
                    setResult(Activity.RESULT_OK, intentResult)
                    finish()
                }
            }

            return true
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode === Activity.RESULT_OK){
            if (requestCode == REQUEST_CODE_PHOTO){
                // new photo has arrive
                data?.extras?.let {
                    bitmap = it.get("data") as Bitmap
                    imageViewPhoto.setImageBitmap(bitmap)
                    date = Date()
                    textViewDate.text = Converters().dateToString(date!!)
                    photoPath = saveImageToCard(this, bitmap!!)


                }
            }
        }
    }

    companion object {

        val PHOTO_PATH          = "photo_path"
        val PHOTO_DATE          = "photo_date"
        val PHOTO_LATITUDE      = "photo_latitude"
        val PHOTO_LONGITUDE     = "photo_longitude"
        val PHOTO_DESCRIPTION   = "photo_description"

        const val REQUEST_CODE_PHOTO = 23524
     }
}
