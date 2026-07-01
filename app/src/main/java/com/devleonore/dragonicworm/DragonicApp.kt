package com.devleonore.dragonicworm

import android.app.Application
import com.devleonore.dragonicworm.model.ProjectConfig
import com.devleonore.dragonicworm.model.RecentProject

/**
 * Holds all in-memory app state. No database, no persistence —
 * everything resets when the app process dies, by design (per spec:
 * "tanpa database, tanpa login, tanpa AI, tanpa internet").
 */
class DragonicApp : Application() {

    var currentConfig: ProjectConfig = ProjectConfig()
    val recentProjects: MutableList<RecentProject> = mutableListOf()
    var totalGenerated: Int = 0

    companion object {
        lateinit var instance: DragonicApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
