package com.satwick.employeecrud;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddEmployeeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = "DebugTag";
    private DatabaseReference query;
    private EditText nameText, empIdText;
    private TextView dobText;
    private MaterialButton submitButton;

    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String name = nameText.getEditableText().toString();
                String id = empIdText.getEditableText().toString();
                String dob = dobText.getText().toString();

                Employee employee = new Employee(name, id, dob);
                Log.d(TAG, employee.toString());

                query.child(id).push();
                query.child(id).setValue(employee);
            } catch (Exception e) {
                Log.d(TAG, "error:" + e.getMessage());
                e.printStackTrace();
            } finally {
                nameText.setText("");
                empIdText.setText("");
                dobText.setText("");
                nameText.requestFocus();
            }
        }
    };

    private View.OnClickListener dateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    AddEmployeeActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            dpd.setMaxDate(now);
            dpd.setVersion(DatePickerDialog.Version.VERSION_2);
            dpd.show(getSupportFragmentManager(), "DatePickerDialog");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        String db_path = getIntent().getStringExtra("path");
        query = FirebaseDatabase.getInstance().getReference(db_path);

        nameText = findViewById(R.id.nameText);
        empIdText = findViewById(R.id.empIdText);

        dobText = findViewById(R.id.dobText);
        dobText.setOnClickListener(dateClickListener);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(submitListener);
    }

    /**
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link java.util.Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dobText.setText(String.format(Locale.UK, "%d/%d/%d", dayOfMonth, monthOfYear, year));
    }
}
