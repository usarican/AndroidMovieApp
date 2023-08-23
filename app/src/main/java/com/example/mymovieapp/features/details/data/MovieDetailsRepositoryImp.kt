package com.example.mymovieapp.features.details.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mymovieapp.core.data.BaseRepository
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.remote.response.MovieDto
import com.example.mymovieapp.core.data.remote.response.MovieResponse
import com.example.mymovieapp.features.details.data.remote.*
import com.example.mymovieapp.utils.Constants
import com.example.mymovieapp.utils.MovieDetailReviewPagingSource
import com.example.mymovieapp.utils.MoviesPagingSource

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieDetailsRepositoryImp @Inject constructor(
    private val movieDetailRemoteDataSource: MovieDetailRemoteDataSource
) : BaseRepository(),MovieDetailsRepository{


    override fun getMovieDetail(movieId: Int): Flow<State<MovieDetailDto>> {
        return apiCall { movieDetailRemoteDataSource.getMovieDetails(movieId) }
    }

    override suspend fun getMovieDetailForDetailPage(movieId: Int): MovieDetailDto {
        return movieDetailRemoteDataSource.getMovieDetails(movieId)
    }

    override suspend fun getMovieDetailCast(movieId: Int): MovieDetailCastResponse {
        return movieDetailRemoteDataSource.getMovieDetailCast(movieId)
    }

    override fun getMovieDetailReviews(movieId: Int): Flow<PagingData<MovieDetailReviewDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.ITEMS_PER_PAGE
            ),
            pagingSourceFactory = {
                MovieDetailReviewPagingSource(
                    movieDetailRemoteDataSource,
                    movieId
                )
            }
        ).flow
    }

    override fun getMovieDetailMoreLikeThisMovie(movieId: Int): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.ITEMS_PER_PAGE
            ),
            pagingSourceFactory = {
                MoviesPagingSource(
                    getMoviesFunc = { page ->
                        movieDetailRemoteDataSource.getMovieDetailMoreLikeThisMovie(
                            page = page,
                            movieId = movieId,
                        )
                    }
                )
            }
        ).flow
    }

    override suspend fun getMovieDetailTrailerVideos(movieId: Int): MovieDetailVideoResponse {
        return movieDetailRemoteDataSource.getMovieDetailTrailerVideos(movieId)
    }


}