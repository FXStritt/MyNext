package com.example.mynext.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mynext.model.Category
import com.example.mynext.model.Item
import com.example.mynext.util.RoomHelper

@Database(entities = arrayOf(Category::class, Item::class), version = 1, exportSchema = false) //TODO consider implementing database migration
@TypeConverters(TypeConverter::class)
abstract class MainRoomDB : RoomDatabase() {

    abstract fun dao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MainRoomDB? = null

        fun getDatabase(context: Context): MainRoomDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainRoomDB::class.java,
                    RoomHelper.dbNamed
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}