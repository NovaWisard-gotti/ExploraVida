package com.educalab.exploravida

import android.app.Application
import com.educalab.exploravida.data.local.ExploraVidaDatabase
import com.educalab.exploravida.data.repository.ExploraVidaRepository

/** Contenedor minimo de dependencias. Sin librerias de inyeccion. */
class ExploraVidaApp : Application() {

    val database: ExploraVidaDatabase by lazy { ExploraVidaDatabase.get(this) }
    val repository: ExploraVidaRepository by lazy { ExploraVidaRepository(database) }
}
