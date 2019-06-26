package com.satwick.employeecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static class EmployeeHolder extends RecyclerView.ViewHolder {
        MaterialCardView mView;
        TextView t1, t2;
        public EmployeeHolder(MaterialCardView v) {
            super(v);
            mView = v;
            t1 = v.findViewById(R.id.cardTextView1);
            t2 = v.findViewById(R.id.cardTextView2);
        }
    }

    private static final String TAG = "MainActivityTag";
    private static final String DB_PATH = "employees";
    private static final int RC_SIGN_IN = 1005;

    private RecyclerView recyclerView;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference query = FirebaseDatabase.getInstance().getReference(DB_PATH);
    private FirebaseRecyclerAdapter<Employee, EmployeeHolder> mFirebaseAdapter;
    private ValueEventListener empListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            try {
                for (DataSnapshot empSnap: dataSnapshot.getChildren()) {
                    Log.d(TAG, "employee: " + empSnap.getValue().toString());
                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "onCancelled: " + databaseError.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        query.addValueEventListener(empListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFirebaseUser == null) {
            setContentView(R.layout.activity_signin);
            MaterialButton signIn = findViewById(R.id.sign_in_button);
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.PhoneBuilder().build());

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            });
        } else {
            setContentView(R.layout.activity_main);
            // FloatingActionButton Listener
            FloatingActionButton fab = findViewById(R.id.addEmployeeButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onclick clicked");
                    Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
                    intent.putExtra("path", DB_PATH);
                    startActivity(intent);
                }
            });

            setupFirebaseAdapter();
        }
    }

    private void setupFirebaseAdapter() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Employee> options = new FirebaseRecyclerOptions.Builder<Employee>()
                .setQuery(query, Employee.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Employee, EmployeeHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EmployeeHolder employeeHolder, int i, @NonNull Employee employee) {
                employeeHolder.t1.setText(employee.getName());
                employeeHolder.t2.setText(employee.getId());
                Log.d(TAG, "onBindViewHolder: item :" + i);
            }

            @NonNull
            @Override
            public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                MaterialCardView mView = (MaterialCardView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.employee_item, parent, false);
                return new EmployeeHolder(mView);
            }
        };

        recyclerView.setAdapter(mFirebaseAdapter);
        Log.d(TAG, "adapter has been set.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            try {
                if (resultCode == RESULT_OK) {
                    mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    Log.d(TAG, "user: " + mFirebaseUser);
//                    pushDataToDb();
                    setContentView(R.layout.activity_main);
                } else {
                    Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Sign In failed: Code - " + resultCode + ", Error - " + response.getError().toString());
                }
            } catch (NullPointerException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
