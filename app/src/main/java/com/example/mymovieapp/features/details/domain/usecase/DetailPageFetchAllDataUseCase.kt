package com.example.mymovieapp.features.details.domain.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.remote.response.GenreResponse
import com.example.mymovieapp.core.data.remote.response.MovieDto
import com.example.mymovieapp.features.details.data.MovieDetailsRepository
import com.example.mymovieapp.features.details.data.remote.MovieDetailReviewDto
import com.example.mymovieapp.features.details.domain.mapper.*
import com.example.mymovieapp.features.details.domain.model.MovieDetailPageItem
import com.example.mymovieapp.features.details.domain.model.MovieDetailVideo
import com.example.mymovieapp.features.home.domain.mapper.GenreListMapper
import com.example.mymovieapp.features.home.domain.mapper.MovieMapper
import com.example.mymovieapp.features.home.domain.usecase.GetGenreListUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DetailPageFetchAllDataUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository,
    private val movieDetailMapper: MovieDetailMapper,
    private val movieMapper: MovieMapper,
    private val movieDetailCastMapper: MovieDetailCastMapper,
    private val movieDetailReviewMapper: MovieDetailReviewMapper,
    private val movieGenreListMapper: DetailsGenreListMapper,
    private val movieDetailTrailerMapper : MovieDetailTrailerMapper,
    private val genreListMapper : GenreListMapper,
    private val genreListUseCase: GetGenreListUseCase,
) {

    operator fun invoke(movieId: Int, scope: CoroutineScope,language : String): Flow<State<MovieDetailPageItem?>> {
        val flow = flow {
            emit(State.Loading)

            val movieDetailPageItem = scope.async {
                val movieDto = movieDetailsRepository.getMovieDetailForDetailPage(movieId)
                val movieDetailCastResponse = movieDetailsRepository.getMovieDetailCast(movieId)
                val movieDetailVideoResponse =
                    movieDetailsRepository.getMovieDetailTrailerVideos(movieId)

                val movieDetail =
                    movieDetailMapper.mapOnMovieDetailDtoToMovieDetailPageItem(movieDto)

                return@async movieDetail.copy(
                    movieCasts = movieDetailCastResponse.movieCast.map {
                        movieDetailCastMapper.mapOnCastDtoToMovieDetailCast(it)
                    },
                    genres = movieGenreListMapper.mapToGenreDtoToGenreList(movieDto.genres),
                    movieTrailers = movieDetailVideoResponse.videos.map { movieDetailVideoDto ->
                        movieDetailTrailerMapper.mapMovieTrailerDtoToMovieDetailVideo(movieDetailVideoDto)
                    }.filter { movieDetailVideo ->
                        movieDetailVideo.type == MovieDetailVideo.VideoType.TRAILER.typeName || movieDetailVideo.type == MovieDetailVideo.VideoType.TEASER.typeName
                    }.sortedBy { it.date }
                )
            }

            emit(State.Success(movieDetailPageItem.await()))
        }
        return combine(
            movieDetailsRepository.getMovieDetailReviews(movieId).cachedIn(scope),
            movieDetailsRepository.getMovieDetailMoreLikeThisMovie(movieId).cachedIn(scope),
            genreListUseCase.invoke(language),
            flow
        ) { pagingDataMovieDetailReview: PagingData<MovieDetailReviewDto>,
            pagingDataMovieDetailMoreLikeThis: PagingData<MovieDto>,
            stateGenreList : State<List<GenreResponse>>,
            stateMovieDetailPageItem : State<MovieDetailPageItem> ->

            State.Loading

            if (stateMovieDetailPageItem is State.Success && stateGenreList is State.Success) {
                val movieDetailPageItem = stateMovieDetailPageItem.data
                val genreListMap = stateGenreList.data
                val newMovieDetailPageItem = movieDetailPageItem.copy(
                    movieReviews = pagingDataMovieDetailReview.map {
                        movieDetailReviewMapper.mapOnReviewDtoToMovieDetailReview(it)
                    },
                    movieMoreLikeThis = pagingDataMovieDetailMoreLikeThis.map {
                        val movie = movieMapper.mapMovieDtoToMovieDetailSimilarMovie(it)
                        val genreList= genreListMapper.mapOnGenreListKeyToValue(genreListMap,it.genreIds)
                        movie.copy(
                            genreList = if (movieDetailPageItem.genres.isNotEmpty()){
                                movieGenreListMapper.genreListSeparateWithBar(genreList.take(2))
                            } else {
                                null
                            }
                        )
                    }
                )
                State.Success(newMovieDetailPageItem)
            } else {
                State.Loading
            }
        }.catch {
            State.Error(it)
        }
    }
}