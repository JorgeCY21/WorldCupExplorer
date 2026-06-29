package com.example.worldcupexplorer.navigation

object AppDestination {
    const val HomeRoute = "home"
    const val TeamsRoute = "teams"
    const val TeamDetailsRoute = "team_details"
    const val TeamDetailsArg = "teamId"
    const val MatchesRoute = "matches"
    const val StandingsRoute = "standings"
    const val ScorersRoute = "scorers"
    const val AboutRoute = "about"

    fun teamDetailsRoute(teamId: Int): String = "$TeamDetailsRoute/$teamId"
}
