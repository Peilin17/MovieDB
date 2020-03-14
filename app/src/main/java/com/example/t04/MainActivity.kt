package com.example.t04

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.lang.Exception
import java.text.SimpleDateFormat
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {


    val adapter = MovieListAdapter()

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.restore()
        adapter.search(newText)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        adapter.search(query)
        return true
    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.search_movie)
        searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {

                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                adapter.restore()
                return true
            }
        })



        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var model  = ViewModelProviders.of(this).get(MovieViewModel::class.java)

        when (item.itemId) {
            R.id.like_movies ->{
               adapter.showLike(model.getLike())
            }
            R.id.byRating ->{
                adapter.sortByRating()
            }
            R.id.byTitle ->{

                adapter.sortByTitle()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.movie_list)


        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val model = ViewModelProviders.of(this).get(MovieViewModel::class.java)

        model.allMovies.observe(
            this,
            Observer<List<MovieItem>>{ movies ->
                movies?.let{
                    adapter.setMovies(it)
                }
            }
        )

        (findViewById<Button>(R.id.refresh)).setOnClickListener{
            model.refreshMovies(1)
        }




    }








    inner class MovieListAdapter():
        RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>(){


//        private var mContext: Context? = null

        private var movies = emptyList<MovieItem>()

        private var moviesBackup= emptyList<MovieItem>()


        internal fun setMovies(movies: List<MovieItem>) {

            moviesBackup = movies
            this.movies = movies
            notifyDataSetChanged()
        }

        fun search(query: String?) {

            movies = movies.filter{it.title.contains(query!!)}

            notifyDataSetChanged()

        }
        fun showLike(like: ArrayList<String>)
        {
            val  temp = ArrayList<MovieItem>()
            for ((i, str) in like.withIndex())
            {

                temp.add( movies.find { it.title == str }!!)
            }
            movies = temp
            notifyDataSetChanged()
        }

        fun restore(){

            movies = moviesBackup
            notifyDataSetChanged()
        }
        fun sortByTitle()
        {
            movies = movies.sortedBy { it.title }
            notifyDataSetChanged()
        }
        fun sortByRating()
        {
            movies = movies.sortedByDescending { it.vote_average}
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {

            return movies.size
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {


            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view, parent, false)
            return MovieViewHolder(v)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {


            //holder.bindItems(movieList[position])

            Glide.with(this@MainActivity).load(resources.getString(R.string.picture_base_url)+movies[position].poster_path).apply( RequestOptions().override(128, 128)).into(holder.view.findViewById(R.id.poster))

            holder.view.findViewById<TextView>(R.id.title).text=movies[position].title

            holder.view.findViewById<TextView>(R.id.rating).text=movies[position].vote_average.toString()


            holder.itemView.setOnClickListener(){
                val intent = Intent(applicationContext, details::class.java).apply{
//                    setClass(mContext, details::class.java)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("TITLE", movies[position].title)
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    putExtra("position", position)

                    putExtra("DATE", formatter.format(movies[position].release_date))
                    putExtra("Describe", movies[position].overview)
                    putExtra("PIC", movies[position].poster_path)
                }
                startActivity(intent)

            }

        }



        inner class MovieViewHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
            override fun onClick(view: View?){

                if (view != null) {


                }


            }


        }
    }

}



