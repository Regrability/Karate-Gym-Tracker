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

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String
)

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    @Query("DELETE FROM users WHERE username = :username")
    suspend fun deleteUserByUsername(username: String)

}

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()

    // LiveData для наблюдения за результатом поиска пользователя
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    // Запрос на получение пользователя с использованием корутин
    fun getUser(username: String, password: String) {
        viewModelScope.launch {
            // Вызов suspend функции в корутине
            val foundUser = userDao.getUser(username, password)
            _user.postValue(foundUser) // Обновляем LiveData результатом
        }
    }

    // Запрос на получение пользователя только по имени пользователя
    fun getUserByUsername(username: String) {
        viewModelScope.launch {
            val foundUser = userDao.getUserByUsername(username)
            _user.postValue(foundUser)
        }
    }


    // Вставка нового пользователя
    fun insertUser(user: User) {
        viewModelScope.launch {
            userDao.insertUser(user)
        }
    }

    // Метод для удаления пользователя по имени
    fun deleteUser(username: String) {
        viewModelScope.launch {
            userDao.deleteUserByUsername(username)
        }
    }
}



