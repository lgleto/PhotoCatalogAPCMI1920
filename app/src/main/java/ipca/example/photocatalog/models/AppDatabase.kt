package ipca.example.photocatalog.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//
// Created by lourencogomes on 27/04/2020.
//

@Database(entities = [PhotoItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoItemDao() : PhotoItemDao

    companion object {

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase?{
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, "db_photos"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }

            return INSTANCE
        }

    }

}