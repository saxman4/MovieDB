// MovieFragment.java
// Fragment subclass that displays the alphabetical list of movie titles
package com.saxman4.moviedb;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.saxman4.moviedb.data.DatabaseDescription.Movies;

public class MovieFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // callback method implemented by MainActivity
    public interface MovieFragmentListener {
        // called when movie selected
        void onMovieSelected(Uri movieUri);

        // called when add button is pressed
        void onAddMovie();
    }

    private static final int MOVIE_LOADER = 0; // identifies Loader

    // used to inform the MainActivity when a movie is selected
    private MovieFragmentListener listener;

    private MovieAdapter movieAdapter; // adapter for recyclerView

    // configures this fragment's GUI
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true); // fragment has menu items to display

        // inflate GUI and get reference to the RecyclerView
        View view = inflater.inflate(
                R.layout.fragment_movie, container, false);
        RecyclerView recyclerView =
                (RecyclerView) view.findViewById(R.id.recyclerView);

        // recyclerView should display items in a vertical list
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity().getBaseContext()));

        // create recyclerView's adapter and item click listener
        movieAdapter = new MovieAdapter(
                new MovieAdapter.MovieClickListener() {
                    @Override
                    public void onClick(Uri movieUri) {
                        listener.onMovieSelected(movieUri);
                    }
                }
        );
        recyclerView.setAdapter(movieAdapter); // set the adapter

        // attach a custom ItemDecorator to draw dividers between list items
        recyclerView.addItemDecoration(new ItemDivider(getContext()));

        // improves performance if RecyclerView's layout size never changes
        recyclerView.setHasFixedSize(true);

        // get the FloatingActionButton and configure its listener
        FloatingActionButton addButton =
                (FloatingActionButton) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(
                new View.OnClickListener() {
                    // displays the AddEditFragment when FAB is touched
                    @Override
                    public void onClick(View view) {
                        listener.onAddMovie();
                    }
                }
        );

        return view;
    }

    // set MovieFragmentListener when fragment attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MovieFragmentListener) context;
    }

    // remove MovieFragmentListener when Fragment detached
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    // initialize a Loader when this fragment's activity is created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
    }

    // called from MainActivity when other Fragment's update database
    public void updateMovieList() {
        movieAdapter.notifyDataSetChanged();
    }

    // called by LoaderManager to create a Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        switch (id) {
            case MOVIE_LOADER:
                return new CursorLoader(getActivity(),
                        Movies.CONTENT_URI, // Uri of contacts table
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        Movies.COLUMN_TITLE + " COLLATE NOCASE ASC"); // sort order
            default:
                return null;
        }
    }

    // called by LoaderManager when loading completes
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieAdapter.swapCursor(data);
    }

    // called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }
}
