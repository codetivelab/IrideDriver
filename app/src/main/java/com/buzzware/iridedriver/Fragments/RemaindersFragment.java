package com.buzzware.iridedriver.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.buzzware.iridedriver.Models.NotificationAdapter;
import com.buzzware.iridedriver.Models.NotificationModel;
import com.buzzware.iridedriver.Models.Reminder;
import com.buzzware.iridedriver.Models.ReminderAdapter;
import com.buzzware.iridedriver.Models.User;
import com.buzzware.iridedriver.R;
import com.buzzware.iridedriver.Screens.Home;
import com.buzzware.iridedriver.databinding.FragmentNotificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RemaindersFragment extends Fragment {


    FragmentNotificationBinding binding;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    List<Reminder> notificationList = new ArrayList<>();

    String image = "";

    public RemaindersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_notification,
                container,
                false);

        binding.tvTitle.setText("Reminders");

        setListener();

        showRequest();

        return binding.getRoot();
    }

    private void setListener() {

        binding.drawerIcon.setOnClickListener(v -> OpenCloseDrawer());

    }

    public static void OpenCloseDrawer() {

        if (Home.mBinding.drawerLayout.isDrawerVisible(GravityCompat.START)) {

            Home.mBinding.drawerLayout.closeDrawer(GravityCompat.START);

        } else {

            Home.mBinding.drawerLayout.openDrawer(GravityCompat.START);

        }

    }

    private void showRequest() {

        firebaseFirestore.collection("Reminder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    notificationList.clear();

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Reminder reminder = document.toObject(Reminder.class);

                        if (reminder != null) {
                            reminder.id = document.getId();

//                        notification.setImage(getImage(notification.getFromId()));

                            if(reminder.r_v_id != null) {

                                if (reminder.r_v_id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                    notificationList.add(reminder);

                                }

                            }
                        }
                    }
                    setRecycler();
                }
            }
        });

    }

    private void setRecycler() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        binding.notificationRV.setLayoutManager(layoutManager);

        ReminderAdapter normalBottleAdapter = new ReminderAdapter(getContext(), notificationList);

        binding.notificationRV.setAdapter(normalBottleAdapter);

    }

}