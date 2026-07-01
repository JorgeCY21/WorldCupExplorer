package com.example.worldcupexplorer.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.worldcupexplorer.data.local.dao.CompetitionDao
import com.example.worldcupexplorer.data.local.dao.MatchDao
import com.example.worldcupexplorer.data.local.dao.ScorerDao
import com.example.worldcupexplorer.data.local.dao.StandingDao
import com.example.worldcupexplorer.data.local.dao.TeamDao
import com.example.worldcupexplorer.data.local.entity.CompetitionEntity
import com.example.worldcupexplorer.data.local.entity.MatchEntity
import com.example.worldcupexplorer.data.local.entity.PlayerEntity
import com.example.worldcupexplorer.data.local.entity.ScorerEntity
import com.example.worldcupexplorer.data.local.entity.StandingEntryEntity
import com.example.worldcupexplorer.data.local.entity.StandingGroupEntity
import com.example.worldcupexplorer.data.local.entity.TeamEntity

@Database(
    entities = [
        CompetitionEntity::class,
        TeamEntity::class,
        PlayerEntity::class,
        MatchEntity::class,
        StandingGroupEntity::class,
        StandingEntryEntity::class,
        ScorerEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FootballDatabase : RoomDatabase() {
    abstract fun competitionDao(): CompetitionDao
    abstract fun teamDao(): TeamDao
    abstract fun matchDao(): MatchDao
    abstract fun standingDao(): StandingDao
    abstract fun scorerDao(): ScorerDao
}
