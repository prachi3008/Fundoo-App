package com.bridgelabz.myfundooapp.note_module

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bridgelabz.myfundooapp.Data.DatabaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class NoteViewModel (application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    // By default all the coroutines launched in this scope should be using the Main dispatcher
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository : NoteDataManager? = null

    //val allNotes: LiveData<List<Note>>

    init{

        //allNotes = repository.allNotes
    }
}