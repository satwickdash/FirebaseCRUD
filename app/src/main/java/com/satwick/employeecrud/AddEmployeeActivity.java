package com.satwick.employeecrud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class AddEmployeeActivity extends AppCompatActivity {

    public static final String TAG = "AddEmployeeActivityTag";
    private DatabaseReference query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        Intent intent = getIntent();
        String db_path = intent.getStringExtra("path");

        query = FirebaseDatabase.getInstance().getReference(db_path);
        MaterialButton submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameText = findViewById(R.id.nameText);
                EditText empIdText = findViewById(R.id.empIdText);

                String name = nameText.getEditableText().toString();
                String id = empIdText.getEditableText().toString();

                nameText.setText("");
                empIdText.setText("");
                nameText.requestFocus();

                query.child(id).push();
                query.child(id).setValue(new Employee(name, id));
            }
        });
    }
}
