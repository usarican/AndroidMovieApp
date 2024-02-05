package com.example.mymovieapp.features.home.domain.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.mymovieapp.R
import com.example.mymovieapp.core.data.State
import com.example.mymovieapp.core.data.remote.response.GenreResponse
import com.example.mymovieapp.core.data.remote.response.MovieDto
import com.example.mymovieapp.features.home.data.HomeRepository
import com.example.mymovieapp.features.home.domain.mapper.GenreListMapper
import com.example.mymovieapp.features.home.domain.mapper.MovieMapper
import com.example.mymovieapp.features.home.domain.model.Category
import com.example.mymovieapp.features.home.domain.model.CategoryList
import com.example.mymovieapp.features.home.domain.model.CategoryType
import com.example.mymovieapp.utils.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetCategoryMoviesUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val movieMapper: MovieMapper,
    private val genreListUseCase: GetGenreListUseCase,
    private val genreListMapper: GenreListMapper,
    private val stringProvider: StringProvider
) {
    operator fun invoke(language: String, scope: CoroutineScope): Flow<State<CategoryList>> {
        return combine(
            homeRepository.getPopularMovies(language).cachedIn(scope),
            homeRepository.getTopRatedMovies(language).cachedIn(scope),
            homeRepository.getUpComingMovies(language).cachedIn(scope),
            genreListUseCase(language)
        ) { popularMoviesPagingData: PagingData<MovieDto>,
            topRatedMoviesPagingData: PagingData<MovieDto>,
            upComingMoviesPagingData: PagingData<MovieDto>,
            stateGenreList: State<List<GenreResponse>> ->

            State.Loading
            val mutableCategoryList = mutableListOf<Category>()

            if (stateGenreList is State.Success) {
                val genreListMap = stateGenreList.data
                val popularMoviesCategoryItem = Category(
                    categoryType = CategoryType.POPULAR,
                    categoryName = stringProvider.getString(R.string.Category_Name_Popular),
                    categoryItems = popularMoviesPagingData.map { movieDto ->
                        val movie = movieMapper.mapOnMovieDto(movieDto)
                        movie.copy(
                            genreList = genreListMapper.mapOnGenreListKeyToValue(
                                genreResponseList = genreListMap,
                                genreKeys = movieDto.genreIds
                            )
                        )
                    }
                )

                val topRatedMoviesCategoryItem = Category(
                    categoryType = CategoryType.TOP_RATED,
                    categoryName = stringProvider.getString(R.string.Category_Name_Top_Rated),
                    categoryItems = topRatedMoviesPagingData.map { movieDto ->
                        val movie = movieMapper.mapOnMovieDto(movieDto)
                        movie.copy(
                            genreList = genreListMapper.mapOnGenreListKeyToValue(
                                genreResponseList = genreListMap,
                                genreKeys = movieDto.genreIds
                            )
                        )
                    }
                )

                val upComingMoviesCategoryItem = Category(
                    categoryType = CategoryType.UP_COMING,
                    categoryName = stringProvider.getString(R.string.Category_Name_Up_Coming),
                    categoryItems = upComingMoviesPagingData.map { movieDto ->
                        val movie = movieMapper.mapOnMovieDto(movieDto)
                        movie.copy(
                            genreList = genreListMapper.mapOnGenreListKeyToValue(
                                genreResponseList = genreListMap,
                                genreKeys = movieDto.genreIds
                            )
                        )
                    }
                )

                mutableCategoryList.add(popularMoviesCategoryItem)
                mutableCategoryList.add(topRatedMoviesCategoryItem)
                mutableCategoryList.add(upComingMoviesCategoryItem)

                val categoryListItem = CategoryList(
                    categoryList = mutableCategoryList
                )
                State.Success(categoryListItem)
            } else {
                State.Loading
            }
        }.catch { throwable ->
            State.Error(throwable)
        }
    }
}