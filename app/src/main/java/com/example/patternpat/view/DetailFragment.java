package com.example.patternpat.view;

import static com.example.patternpat.R.menu.detail_menu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patternpat.R;
import com.example.patternpat.model.DogBreed;
import com.example.patternpat.util.Util;
import com.example.patternpat.viewmodel.DetailViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {


    private int dogUid;
    private DetailViewModel detailViewModel;
    private Boolean sendSmsStarted = false;


    @BindView(R.id.dog_image)
    ImageView dogImage;

    @BindView(R.id.dog_name)
    TextView dogName;

    @BindView(R.id.dog_purpose)
    TextView dogPurpose;

    @BindView(R.id.dog_temperament)
    TextView dogTemperament;

    @BindView(R.id.dog_lifespan)
    TextView dogLifeSpan;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //check if arguments have been passed
        if (getArguments() != null) {
            dogUid = DetailFragmentArgs.fromBundle(getArguments()).getDogUid();

        }

        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.fetch(dogUid);
        observeViewModel();
    }

    private void observeViewModel() {
        detailViewModel.dogBreedMutableLiveData.observe(getViewLifecycleOwner(), dogBreed -> {
            if (dogBreed != null && dogBreed instanceof DogBreed && getContext() != null) {
                dogName.setText(dogBreed.dogBreed);
                dogPurpose.setText(dogBreed.bredFor);
                dogTemperament.setText(dogBreed.temperament);
                dogLifeSpan.setText(dogBreed.lifeSpan);

                if (dogBreed.imageUrl != null) {
                    Util.loadImage(dogImage, dogBreed.imageUrl, new CircularProgressDrawable(getContext()));
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send_sms: {
                if (!sendSmsStarted) {
                    sendSmsStarted = true;
                    ((MainActivity) getActivity()).checkSmsPermission();
                    Toast.makeText(getContext(), "Action Send SMS", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.action_share: {
                Toast.makeText(getContext(), "Action Share", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPermissionResult(Boolean permissionGranted) {
        sendSmsStarted = false;
    }
}