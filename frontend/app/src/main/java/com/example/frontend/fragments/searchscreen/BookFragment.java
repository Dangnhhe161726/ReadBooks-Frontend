package com.example.frontend.fragments.searchscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.adapters.searchscreen.SearchBookAdapter;
import com.example.frontend.models.Book;
import com.example.frontend.networks.RetrofitClient;
import com.example.frontend.responses.PaginationResponse;
import com.example.frontend.services.BookService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookFragment extends Fragment implements SearchBookAdapter.SearchBookItemListener{

    private RecyclerView reViewFragmentBook;
    private SearchBookAdapter adapter;
    private String query;
    private int currentPage;
    private int pageSize;

    private BookService bookService;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
        resetSearch();
        searchBooks(query);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_search_screen, container, false);
        bookService = RetrofitClient.getClient(view.getContext()).create(BookService.class);
        reViewFragmentBook = view.findViewById(R.id.reViewFragmentBook);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        reViewFragmentBook.setLayoutManager(manager);
        adapter = new SearchBookAdapter();
        adapter.setSearchBookItemListener(this::onItemClick);
        reViewFragmentBook.setAdapter(adapter);
        currentPage = 0;
        pageSize = 5;
        return view;
    }

    private void resetSearch() {
        currentPage = 0;
        adapter.clearBooks();
        reViewFragmentBook.scrollToPosition(0);
        reViewFragmentBook.clearOnScrollListeners();
    }

    private void searchBooks(String query) {
        Call<PaginationResponse> responseCall = bookService.searchBooksByName(query, currentPage, pageSize);
        responseCall.enqueue(new Callback<PaginationResponse>() {
            @Override
            public void onResponse(Call<PaginationResponse> call, Response<PaginationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PaginationResponse paginationResponse = response.body();
                    if (paginationResponse.getContent().size() != 0) {
                        adapter.addBooks(paginationResponse.getContent());
                        currentPage++;
                        reViewFragmentBook.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (!reViewFragmentBook.canScrollVertically(1)) {
                                    searchBooks(query);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<PaginationResponse> call, Throwable throwable) {
                Toast.makeText(getContext(), "Failed to load book", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Book clickedBook = adapter.getBooks().get(position);
        Toast.makeText(view.getContext(), "Clicked: " + clickedBook.getName(), Toast.LENGTH_SHORT).show();
    }
}
