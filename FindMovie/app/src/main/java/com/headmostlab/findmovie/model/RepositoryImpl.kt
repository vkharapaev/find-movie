package com.headmostlab.findmovie.model

class RepositoryImpl : Repository {
    override fun getMovies(): List<Movie> {
        return listOf(
            Movie(791373,"Zack Snyder's Justice League", 2021, 8.7, "/tnAuB8q5vv7Ax9UAEje5Xi4BXik.jpg"),
            Movie(527774, "Raya and the Last Dragon", 2021, 8.4, "/lPsD10PP4rgUGiGR4CCXA6iY0QQ.jpg"),
            Movie(581389, "Space Sweepers", 2021, 7.2, "/lykPQ7lgrLJPwLlSyetVXsl2wDf.jpg"),
            Movie(793723, "Sentinelle", 2021, 6.0, "/fFRq98cW9lTo6di2o4lK1qUAWaN.jpg"),
        )
    }
}