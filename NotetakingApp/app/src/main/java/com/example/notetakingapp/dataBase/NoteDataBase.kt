package com.example.notetakingapp.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notetakingapp.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDataBase : RoomDatabase() {
    abstract fun getNoteDao() : NoteDao
    companion object{
        @Volatile
        private var instance: NoteDataBase? = null
        private val LOCCK = Any()
        operator fun invoke(context: Context) = instance?:synchronized(LOCCK){
            instance?: createDataBase(context).also{
                instance = it 
            }
        }

        private fun createDataBase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            NoteDataBase::class.java,
            "note_db"
        ).build()
    }
}