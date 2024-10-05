package com.example.src.ui.theme.dataBases

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Entity(tableName = "gums")
data class Gum(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hallName: String,         // Название зала
    val coache: String,           // Информация о тренерах
    val trainingTimeAndStudents: String, // Время тренировок и информация об учениках
    val imageName: Int            // Название картинки (ID ресурса)
)

@Dao
interface GumDao {
    @Insert
    suspend fun insertGum(gum: Gum)

    @Query("SELECT * FROM gums")
    suspend fun getAllGums(): List<Gum>

    @Query("DELETE FROM gums WHERE id = :id")
    suspend fun deleteGumById(id: Int)
}

@Database(entities = [Gum::class], version = 1)
abstract class GumDatabase : RoomDatabase() {
    abstract fun gumDao(): GumDao

    companion object {
        @Volatile
        private var INSTANCE: GumDatabase? = null

        fun getDatabase(context: Context): GumDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GumDatabase::class.java,
                    "gum_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


class GumViewModel(application: Application) : AndroidViewModel(application) {
    private val gumDao = GumDatabase.getDatabase(application).gumDao()

    private val _gums = MutableLiveData<List<Gum>>()
    val gums: LiveData<List<Gum>> = _gums

    fun getAllGums() {
        viewModelScope.launch {
            val gumList = gumDao.getAllGums()
            _gums.postValue(gumList)
        }
    }

    fun insertGum(gum: Gum) {
        viewModelScope.launch {
            gumDao.insertGum(gum)
        }
    }

    fun deleteGum(id: Int) {
        viewModelScope.launch {
            gumDao.deleteGumById(id)
        }
    }
}




