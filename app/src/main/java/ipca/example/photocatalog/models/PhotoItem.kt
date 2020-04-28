package ipca.example.photocatalog.models

import androidx.room.*
import java.util.*

//
// Created by lourencogomes on 2020-03-26.
//

@Entity
class PhotoItem {

    @PrimaryKey
    var uid         : Long?   = null
    var filePath    : String? = null
    var description : String? = null
    var date        : Date?   = null
    var gpsLatitude : Double? = null
    var gpsLongitude: Double? = null

    constructor(
        uid: Long?,
        filePath: String?,
        description: String?,
        date: Date?,
        gpsLatitude: Double?,
        gpsLongitude: Double?
    ) {
        this.uid = uid
        this.filePath = filePath
        this.description = description
        this.date = date
        this.gpsLatitude = gpsLatitude
        this.gpsLongitude = gpsLongitude
    }
}

@Dao
interface PhotoItemDao {

    @Query("SELECT * FROM PhotoItem")
    fun getAll():List<PhotoItem>

    @Query("SELECT * FROM PhotoItem WHERE uid=:id")
    fun getById(id: Long?):PhotoItem

    @Insert
    fun insert(photoItem: PhotoItem)

    @Delete
    fun delete(item: PhotoItem)
}