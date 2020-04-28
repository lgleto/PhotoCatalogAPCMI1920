package ipca.example.photocatalog

import android.Manifest
import android.app.Activity
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.app.ActivityCompat
import ipca.example.photocatalog.models.AppDatabase
import ipca.example.photocatalog.models.PhotoItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_photo.view.*
import kotlinx.android.synthetic.main.row_photo.view.imageViewPhoto

import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    val photos : MutableList<PhotoItem> = ArrayList<PhotoItem>()

    var adapter : PhotosAdapter = PhotosAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listViewPhotos.adapter = adapter

        requestPermission()
        //Create a list view with taken photos array

        floatingActionButtonNewPhoto.setOnClickListener {
            val intent = Intent(this, PhotoDetailActivity::class.java)
           // startActivity(intent)
            startActivityForResult(intent, 1002)
        }
        var photos : List<PhotoItem> = AppDatabase.getDatabase(this)?.photoItemDao()?.getAll()
    }

    inner class PhotosAdapter : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = layoutInflater.inflate(R.layout.row_photo, parent, false)
            view.textViewDate.text = photos[position].date.toString()
            view.textViewDescription.text = photos[position].description
            view.textViewPath.text = photos[position].filePath

            loadImageFromCard(photos[position].filePath ?: "")?.let {
                view.imageViewPhoto.setImageBitmap(it)
            }

            return view

        }

        override fun getItem(position: Int): Any {
            return photos[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return photos.size
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode === Activity.RESULT_OK){
            if (requestCode == 1002){
                // new photo has arrive
                data?.extras?.let {
                    val description = it.getString(PhotoDetailActivity.PHOTO_DESCRIPTION)
                    val date        = it.getString(PhotoDetailActivity.PHOTO_DATE)
                    val path        = it.getString(PhotoDetailActivity.PHOTO_PATH)
                    val latitude    = it.getDouble(PhotoDetailActivity.PHOTO_LATITUDE)
                    val logigitude  = it.getDouble(PhotoDetailActivity.PHOTO_LONGITUDE)

                    val photoItem = PhotoItem()
                    photoItem.description = description
                    photoItem.date = stringToDate(date!!)
                    photoItem.filePath = path
                    photoItem.gpsLatitude = latitude
                    photoItem.gpsLongitude = logigitude

                    photos.add(photoItem)

                    adapter.notifyDataSetChanged()
                }
            }
        }
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )
    }

}
