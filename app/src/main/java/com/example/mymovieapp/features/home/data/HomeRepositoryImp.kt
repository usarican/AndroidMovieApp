package com.example.mymovieapp.features.home.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mymovieapp.core.data.BaseRepository
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.remote.response.GenreListDto
import com.example.mymovieapp.core.data.remote.response.MovieDto
import com.example.mymovieapp.core.data.remote.response.MovieResponse
import com.example.mymovieapp.features.home.data.remote.HomeRemoteDataSource
import com.example.mymovieapp.utils.Constants
import com.example.mymovieapp.utils.MoviesPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(
    private val homeRemoteDataSource: HomeRemoteDataSource
) : HomeRepository, BaseRepository(){

    override fun getTrendingMoviesOfWeek(language: String): Flow<State<MovieResponse>> {
       return apiCall { homeRemoteDataSource.getTrendingMoviesOfWeek(language) }
    }

    override suspend fun getPopularMovies(page: Int, language: String): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.ITEMS_PER_PAGE
            ),
            pagingSourceFactory = {
                MoviesPagingSource(
                    getMoviesFunc = { page ->
                        homeRemoteDataSource.getPopularMovies(
                            page = page,
                            language = language,
                        )
                    }
                )
            }
        ).flow
    }

    override suspend fun getTopRatedMovies(
        page: Int,
        language: String
    ): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.ITEMS_PER_PAGE
            ),
            pagingSourceFactory = {
                MoviesPagingSource(
                    getMoviesFunc = { page ->
                        homeRemoteDataSource.getTopRatedMovies(
                            page = page,
                            language = language,
                        )
                    }
                )
            }
        ).flow
    }

    override suspend fun getUpComingMovies(
        page: Int,
        language: String
    ): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.ITEMS_PER_PAGE
            ),
            pagingSourceFactory = {
                MoviesPagingSource(
                    getMoviesFunc = { page ->
                        homeRemoteDataSource.getUpComingMovies(
                            page = page,
                            language = language,
                        )
                    }
                )
            }
        ).flow
    }

    override fun getMovieGenreList(language: String): Flow<GenreListDto> {
        return flow {
            emit( homeRemoteDataSource.getMovieGenreList(language))
        }
    }
}