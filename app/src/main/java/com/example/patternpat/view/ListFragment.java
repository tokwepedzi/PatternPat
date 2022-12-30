package com.example.patternpat.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.patternpat.R;
import com.example.patternpat.model.DogBreed;
import com.example.patternpat.viewmodel.ListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment {

    private ListViewModel viewModel;
    private DogsListAdapter dogsListAdapter = new DogsListAdapter(new ArrayList<>());

    //Use Butter knife to bind

    @BindView(R.id.dogs_list)
    RecyclerView dogListRecyclerView;

    @BindView(R.id.list_error)
    TextView listError;

    @BindView(R.id.loading_view)
    ProgressBar loadingView;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Navigate to detail fragment directly
       /* ListFragmentDirections.ActionDetail actionDetail = ListFragmentDirections.actionDetail();
        Navigation.findNavController(view).navigate(actionDetail);*/

        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        viewModel.refresh();

        dogListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dogListRecyclerView.setAdapter(dogsListAdapter);

        observeViewModel();

        //Force refresh and pull data from endpoint, bypassing the cache
        refreshLayout.setOnRefreshListener(() -> {
            dogListRecyclerView.setVisibility(View.GONE);
            listError.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
            viewModel.refreshByPassCache();
            refreshLayout.setRefreshing(false);
        });
    }

    private void observeViewModel() {
        viewModel.dogs.observe(getViewLifecycleOwner(), dogs -> {
            if (dogs != null && dogs instanceof List) {
                dogListRecyclerView.setVisibility(View.VISIBLE);
                dogsListAdapter.updateDogsList(dogs);
            }
        });

        viewModel.dogLoadError.observe(getViewLifecycleOwner(), isError -> {
            if (isError != null && isError instanceof Boolean) {
                listError.setVisibility(isError ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.loading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading instanceof Boolean) {
                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    listError.setVisibility(View.GONE);
                    dogListRecyclerView.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_to_settings:{
                if(isAdded()){
                    Navigation.findNavController(getView()).navigate(ListFragmentDirections.actionSettings());
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}