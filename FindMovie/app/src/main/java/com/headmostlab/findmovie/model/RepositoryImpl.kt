package com.headmostlab.findmovie.model

import io.reactivex.Single

class RepositoryImpl : Repository {
    override fun getMovies(): Single<List<ShortMovie>> {
        return Single.fromCallable {listOf(
            ShortMovie(791373,"Zack Snyder's Justice League", 2021, 8.7, "/tnAuB8q5vv7Ax9UAEje5Xi4BXik.jpg"),
            ShortMovie(527774, "Raya and the Last Dragon", 2021, 8.4, "/lPsD10PP4rgUGiGR4CCXA6iY0QQ.jpg"),
            ShortMovie(581389, "Space Sweepers", 2021, 7.2, "/lykPQ7lgrLJPwLlSyetVXsl2wDf.jpg"),
            ShortMovie(793723, "Sentinelle", 2021, 6.0, "/fFRq98cW9lTo6di2o4lK1qUAWaN.jpg"),
        )}
    }

    override fun getMovie(movieId: Int): Single<FullMovie> {
        return Single.fromCallable {FullMovie(76341, "Mad Max: Fury Road", "Mad Max: Fury Road",
        listOf("Action", "Adventure", "Science Fiction"), 121, 7.5, 150000000,
        378858340, 2015, "An apocalyptic story set in the furthest " +
                "reaches of our planet, in a stark desert landscape where humanity is " +
                "broken, and most everyone is crazed fighting for the necessities " +
                "of life. Within this world exist two rebels on the run who just might " +
                "be able to restore order.",
        "/8tZYtuWezp8JbcsvHYO0O46tFbo.jpg")}
    }
}