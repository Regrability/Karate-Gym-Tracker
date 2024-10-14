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
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.src.R
import kotlinx.coroutines.launch

@Entity(tableName = "gums")
data class Gum(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hallName: String,             // Название зала
    val coache: String,               // Информация о тренерах
    val trainingTimeAndStudents: String, // Время тренировок и информация об учениках
    val imageName: Int,               // Название картинки (ID ресурса)
    val coachPhotoId: Int,            // ID изображения тренера
    val coachDescription: String      // Описание тренера
)


@Dao
interface GumDao {
    @Insert
    suspend fun insertGum(gum: Gum)

    @Query("SELECT * FROM gums")
    suspend fun getAllGums(): List<Gum>

    @Query("DELETE FROM gums WHERE id = :id")
    suspend fun deleteGumById(id: Int)

    @Query("SELECT * FROM gums WHERE id = :id")
    suspend fun getGumById(id: Int): Gum?

    @Query("DELETE FROM gums")
    suspend fun deleteAllGums()

    @Query("SELECT COUNT(*) FROM gums")
    suspend fun getGumCount(): Int

}

@Database(entities = [Gum::class], version = 2)
abstract class GumDatabase : RoomDatabase() {
    abstract fun gumDao(): GumDao

    companion object {
        @Volatile
        private var INSTANCE: GumDatabase? = null
        private const val PREF_NAME = "gum_database_prefs"
        private const val KEY_FIRST_LAUNCH = "first_launch"

        fun getDatabase(context: Context): GumDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GumDatabase::class.java,
                    "gum_database"
                ).addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance

                // Проверяем первый запуск и выполняем вставку данных
                val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                val isFirstLaunch = sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true)

                if (isFirstLaunch) {
                    // Если это первый запуск, вставляем данные
                    instance.insertGumsIfEmpty(context)

                    // Обновляем SharedPreferences, чтобы отметить, что первый запуск завершен
                    sharedPreferences.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
                }

                instance
            }
        }
    }

    private fun insertGumsIfEmpty(context: Context) {
        // Получаем ViewModel для работы с базой данных
        val gumViewModel = GumViewModel(context.applicationContext as Application)

        gumViewModel.isDatabaseEmpty().observeForever { isEmpty ->
            if (isEmpty) {
                val gums1 = listOf(
                    Gum(
                        hallName = "г. Минск, ул. Ленина, д. 1",
                        coache = "Урбанович Артём Николаевчи",
                        trainingTimeAndStudents = "Вт, Чт, Сб - 19:00-21:00 (18+ лет)\nВт, Пн, Пт - 17:00 - 19:00 (14-16 лет)",
                        imageName = R.drawable.zal_1, // Замените на ваш ресурс
                        coachPhotoId = R.drawable.coach_1, // Замените на ваш ресурс
                        coachDescription = "Мастерская степень: 1 дан\n" +
                                "Должность: Зам. Председателя Правления ОО \"ДЮСК \"Самурай\"\n" +
                                "Начало тренерской деятельности: c 2016 года.\n" +
                                "Стаж занятий Киокушином: с 2009 года.\n" +
                                "Первый тренер: Максимов Максим Валерьевич (1 Дан)"
                    ),
                    Gum(
                        hallName = "г. Минск, пр. Победителей, д. 10",
                        coache = "Пастухов Антон Алексеевич",
                        trainingTimeAndStudents = "Ср, Пт - 18:00-20:00 (16+ лет)\nЧт, Пн - 15:00 - 17:00 (12-14 лет)",
                        imageName = R.drawable.zal_2,
                        coachPhotoId = R.drawable.coach_2,
                        coachDescription = "Мастерская степень: 1 дан\n" +
                                "Должность: Зам. Председателя Правления ОО \"ДЮСК \"Самурай\"\n" +
                                "Начало тренерской деятельности: c 2015 года.\n" +
                                "Стаж занятий Киокушином: с 2012 года."
                    ),
                    Gum(
                        hallName = "г. Минск, ул. Октябрьская, д. 5",
                        coache = "Воробей Дмитрий Александрович",
                        trainingTimeAndStudents = "Пн, Чт - 20:00-22:00 (18+ лет)\nСб, Вс - 10:00 - 12:00 (14-16 лет)",
                        imageName = R.drawable.zal_3,
                        coachPhotoId = R.drawable.coach_3,
                        coachDescription = "Мастерская степень: 3 Дан\n" +
                                "Основатель ОО \"ДЮСК \"Самурай\"\n" +
                                "Должность: Председатель Правления ОО \"ДЮСК \"Самурай\"\n" +
                                "Должность: Зам. Председателя Правления Федерации ОО БРФКК \"ИКО1\"\n" +
                                "Должность: Исполнительный директор \"БАК\"\n" +
                                "Судья международной категории: IKO Judge (2)\n" +
                                "Начало тренерской деятельности: c 2013 года.\n" +
                                "Стаж занятий Киокушином: с 2006 года."
                    ),
                    Gum(
                        hallName = "г. Минск, ул. Беларусь, д. 20",
                        coache = "Иванов Иван Иванович",
                        trainingTimeAndStudents = "Вт, Чт - 19:00-21:00 (16+ лет)\nСр, Пт - 17:00 - 19:00 (12-14 лет)",
                        imageName = R.drawable.zal_4,
                        coachPhotoId = R.drawable.coach_4,
                        coachDescription = "Мастерская степень: 3 дан\n" +
                                "Должность: Зам. Председателя Правления ОО \"ДЮСК \"Самурай\"\n" +
                                "Начало тренерской деятельности: c 2017 года.\n" +
                                "Стаж занятий Киокушином: с 2015 года."
                    ),
                    Gum(
                        hallName = "г. Минск, ул. Сурганова, д. 3",
                        coache = "Кочеров Роман Сергеевич",
                        trainingTimeAndStudents = "Пн, Ср - 18:00-20:00 (18+ лет)\nЧт, Сб - 16:00 - 18:00 (14-16 лет)",
                        imageName = R.drawable.zal_5,
                        coachPhotoId = R.drawable.coach_5,
                        coachDescription = "Мастерская степень: 5 КЮ\n" +
                                "Должность: Зам. Председателя Правления ОО \"ДЮСК \"Сэйман\"\n" +
                                "Начало тренерской деятельности: c 2024 года.\n" +
                                "Стаж занятий Киокушином: с 2012 года."
                    ),
                    Gum(
                        hallName = "г. Минск, ул. Независимости, д. 12",
                        coache = "Тренер 6",
                        trainingTimeAndStudents = "Чт, Пт - 19:00-21:00 (18+ лет)\nСб, Вс - 11:00 - 13:00 (14-16 лет)",
                        imageName = R.drawable.zal_6,
                        coachPhotoId = R.drawable.coach_6,
                        coachDescription = "Мастерская степень: 1 КЮ\n" +
                                "Основатель ОО \"ДЮСК \"Сэйман\"\n" +
                                "Должность: Председатель Правления ОО \"ДЮСК \"Сэйман\"\n" +
                                "Начало тренерской деятельности: c 2012 года.\n" +
                                "Стаж занятий Киокушином: с 1990 года."
                    )
                )

                // Вставка объектов в базу данных
                gums1.forEach { gum ->
                    gumViewModel.insertGum(gum)
                }
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Добавляем новые колонки с дефолтными значениями
        database.execSQL("ALTER TABLE gums ADD COLUMN coachPhotoId INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE gums ADD COLUMN coachDescription TEXT NOT NULL DEFAULT ''")
    }
}



class GumViewModel(application: Application) : AndroidViewModel(application) {
    private val gumDao = GumDatabase.getDatabase(application).gumDao()

    private val _gums = MutableLiveData<List<Gum>>()
    val gums: LiveData<List<Gum>> = _gums

    private val _selectedGum = MutableLiveData<Gum?>()
    val selectedGum: LiveData<Gum?> = _selectedGum

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

    fun getGumById(id: Int) {
        viewModelScope.launch {
            val gum = gumDao.getGumById(id)
            _selectedGum.postValue(gum)
        }
    }

    fun deleteGum(id: Int) {
        viewModelScope.launch {
            gumDao.deleteGumById(id)
        }
    }

    // Новый метод для удаления всех записей
    fun clearDatabase() {
        viewModelScope.launch {
            gumDao.deleteAllGums()
        }
    }

    // Метод для проверки, пуста ли база данных
    fun isDatabaseEmpty(): LiveData<Boolean> {
        val isEmpty = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val count = gumDao.getGumCount()
            isEmpty.postValue(count == 0) // Если count равно 0, база данных пуста
        }
        return isEmpty
    }

}




