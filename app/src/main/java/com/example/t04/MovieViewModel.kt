package com.example.t04

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MovieViewModel(application: Application): AndroidViewModel(application){

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob+ Dispatchers.Main


    private val scope = CoroutineScope(coroutineContext)


    private var disposable: Disposable? = null


    private val repository: MovieItemRepository = MovieItemRepository(MovieRoomDatabase.getDatabase(application).movieDao())

    val allMovies: LiveData<List<MovieItem>>

    init {
        allMovies = repository.allMovies
    }
    companion object{
        val likeList = ArrayList<String>()
        var page = 1;
        var threePage = ArrayList<MovieItem>()
    }

    fun refreshMovies(page: Int){


        disposable =
            RetrofitService.create("https://api.themoviedb.org/3/").getNowPlaying("5cc1ee37b1200d1ea78ff1da5f578914",page).subscribeOn(
                Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(
                {result -> showResult(result)},
                {error -> showError(error)})
    }
//    fun searchMovie(keyword: String)
//    {
//        disposable =
//            RetrofitService.create("https://api.themoviedb.org/3/").getDiscover("5cc1ee37b1200d1ea78ff1da5f578914",keyword).subscribeOn(
//                Schedulers.io()).observeOn(
//                AndroidSchedulers.mainThread()).subscribe(
//                {result -> showResult(result)},
//                {error -> showError(error)})
//    }



    private fun showError(error: Throwable?) {


    }

    private fun showResult(movies: Movies?) {
        deleteAll()
        movies?.results?.forEach { movie ->
            insert(movie)
        }
    }

    private fun insert(movie: MovieItem) = scope.launch(Dispatchers.IO) {
        repository.insert(movie)
    }

    private fun deleteAll() = scope.launch (Dispatchers.IO){
        repository.deleteAll()
    }
    fun addLike(i : String)
    {
        likeList.add(i)
    }
    fun getLike(): ArrayList<String> {
        return likeList
    }
    fun dislike(i: String)
    {
        likeList.remove(i)
    }
    fun getPage(): Int {
        return page
    }
    fun setPage(i: Int){
        page = i
    }
    fun getThree(): ArrayList<MovieItem> {
        return threePage
    }
    fun addThree(movies: List<MovieItem>)
    {
        threePage.addAll(movies)
    }





}