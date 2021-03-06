package com.buzzware.iridedriver.Screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.buzzware.iridedriver.Models.VehicleModel;
import com.buzzware.iridedriver.databinding.ActivityVehicleDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CollectVehicleDataScreen extends BaseActivity {

    ActivityVehicleDetailsBinding binding;

    VehicleModel vehicle;

    FirebaseFirestore db;

    Boolean isFromSideMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityVehicleDetailsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        onViewCreated();
    }

    public static void startVehicleInformation(Context c) {

        c.startActivity(new Intent(c, CollectVehicleDataScreen.class)

        .putExtra("isFromSideMenu", true));

    }

    private void onViewCreated() {

        isFromSideMenu = false;

        initialize();

        getExtrasFromIntent();

        if (isFromSideMenu) {

            binding.continueTV.setText("Save");

        }

        checkVehicleDetails();

        setListeners();
    }

    private void getExtrasFromIntent() {

        if (getIntent().getExtras() != null) {

            Bundle b = getIntent().getExtras();

            isFromSideMenu = b.getBoolean("isFromSideMenu");

        }

    }

    private void setListeners() {

        binding.btnContinue.setOnClickListener(v -> validateAndSaveData());

        binding.appBarTitleInclude.backIcon.setOnClickListener(v -> finish());

    }

    private void validateAndSaveData() {

        if (validate()) {

            if (vehicle == null) {

                initializeVehicleModel();

                uploadDataToFirestore();
            }
            else {

                initializeVehicleModel();

                uploadDataToFirestore();
            }
        }
    }

    private void uploadDataToFirestore() {

        showLoader();

        FirebaseFirestore.getInstance().collection("Vehicle")
                .document()
                .set(vehicle);

        hideLoader();

        if (isFromSideMenu){

            finish();

            return;

        }

        Intent i = new Intent(CollectVehicleDataScreen.this, UploadVehicleImagesScreen.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(i);
    }

    private void initializeVehicleModel() {

        vehicle = new VehicleModel();

        vehicle.make = binding.makeET.getText().toString();
        vehicle.tagNumber = binding.tagNumberET.getText().toString();
        vehicle.noOfDoors = binding.doorsET.getText().toString();
        vehicle.year = binding.yearET.getText().toString();
        vehicle.noOfSeatBelts = binding.seatBeltsET.getText().toString();
        vehicle.name = binding.vehicleNameET.getText().toString();
        vehicle.userId = getUserId();

    }

    private boolean validate() {

        if (binding.vehicleNameET.getText().toString().isEmpty()) {

            showErrorAlert("Vehicle Name Required");

            return false;
        }

        if (binding.tagNumberET.getText().toString().isEmpty()) {

            showErrorAlert("Tag Number Required");

            return false;
        }

        if (binding.seatBeltsET.getText().toString().isEmpty()) {

            showErrorAlert("Seat Belts Info Required");

            return false;
        }

        if (binding.makeET.getText().toString().isEmpty()) {

            showErrorAlert("Vehicle Make Required");

            return false;
        }

        if (binding.doorsET.getText().toString().isEmpty()) {

            showErrorAlert("Doors Info Required");

            return false;
        }

        if (binding.yearET.getText().toString().isEmpty()) {

            showErrorAlert("Year field Required");

            return false;
        }

        return true;
    }

    private void initialize() {

        binding.appBarTitleInclude.backAppBarTitle.setText("Vehicle Details");

        db = FirebaseFirestore.getInstance();

    }

    private void checkVehicleDetails() {

        db.collection("Vehicle")
                .whereEqualTo(
                        "userId",
                        getUserId()
                )
                .get()
                .addOnCompleteListener(
                        (OnCompleteListener<QuerySnapshot>)
                                this::parseSnapshot
                );

    }

    void parseSnapshot(Task<QuerySnapshot> task) {

        if (task.getResult() != null || task.getResult().size() > 0) {

            for (QueryDocumentSnapshot document : task.getResult()) {

                vehicle = document.toObject(VehicleModel.class);

            }

        }

        if (vehicle != null)

            setData();
    }

    private void setData() {

        binding.vehicleNameET.setText(vehicle.getName());

        binding.yearET.setText(vehicle.getYear());

        binding.doorsET.setText(vehicle.getNoOfDoors());

        binding.makeET.setText(vehicle.getMake());

        binding.seatBeltsET.setText(vehicle.getNoOfSeatBelts());

        binding.tagNumberET.setText(vehicle.getTagNumber());
    }

}
