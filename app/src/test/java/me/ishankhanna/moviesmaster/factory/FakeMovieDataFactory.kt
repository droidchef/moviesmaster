package me.ishankhanna.moviesmaster.factory

import me.ishankhanna.moviesmaster.data.model.Movie

object FakeMovieDataFactory {

    fun getFakeMovie(): Movie {
        return Movie(
            DataFactory.randomInt(),
            DataFactory.randomInt(),
            DataFactory.randomBoolean(),
            DataFactory.randomDouble(),
            DataFactory.randomString(),
            DataFactory.randomDouble(),
            DataFactory.randomString(),
            DataFactory.randomString(),
            DataFactory.randomString(),
            arrayListOf(
                1, 2, 3
            ),
            DataFactory.randomString(),
            DataFactory.randomBoolean(),
            DataFactory.randomString(),
            DataFactory.randomString()
        )
    }


    fun getFakeMovies(numberOfFakeMovies: Int) : Collection<Movie> {
        val listOfFakeMovies = ArrayList<Movie>()

        for (i in 0 until numberOfFakeMovies) {
            listOfFakeMovies.add(getFakeMovie())
        }

        return listOfFakeMovies
    }


}