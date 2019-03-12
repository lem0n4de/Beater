package com.lem0n.beater.junit4


import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.lem0n.beater.data.database.AppDatabase
import com.lem0n.beater.data.database.Roles
import com.lem0n.beater.data.database.dao.UserDao
import com.lem0n.beater.data.database.entity.User
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class RoleSaveTest {
    private lateinit var userDao : UserDao
    private lateinit var db : AppDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java)
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    runBlocking {
                        db.execSQL("INSERT INTO users (name, role, deviceName) VALUES ('yunus', 'SERVER', 'bombing')")
                    }
                }
            }).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun shouldWriteUserToDatabase() {
        val user = User("name", Roles.CLIENT, "Tablet")
        runBlocking {
            userDao.upsert(user).doOnError {
                throw it
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun shouldReadFromDatabase() {
        lateinit var user : User
        runBlocking {
            user = userDao.getUser().blockingGet()
        }
        user.id shouldBe 1
        user.role shouldEqual Roles.SERVER
        user.name shouldEqual "yunus"
        user.deviceName shouldEqual "bombing"
    }

    @Test
    @Throws(Exception::class)
    fun shouldOverrideUser() {
        lateinit var oldUser : User
        lateinit var newUser : User
        runBlocking {
            oldUser = userDao.getUser().blockingGet()
            userDao.upsert(User("new user", Roles.CLIENT, "Tablet"))
                .doOnComplete {
                    newUser = userDao.getUser().blockingGet()
                    newUser shouldNotEqual oldUser
                }
                .doOnError {
                    throw Exception("User couldn't be inserted.")
                }
        }
    }
}