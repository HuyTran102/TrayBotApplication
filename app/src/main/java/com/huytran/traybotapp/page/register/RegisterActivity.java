package com.huytran.traybotapp.page.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huytran.traybotapp.R;
import com.huytran.traybotapp.helper.HelperClass;
import com.huytran.traybotapp.page.login.LoginActivity;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private EditText editTextName, editTextPassword, editTextMail;
    private CircularProgressButton signUp;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private RadioButton chose_boy, chose_girl;
    private String date, signUpDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
        editTextName = findViewById(R.id.acc_name);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextMail = findViewById(R.id.editTextMail);
        signUp = findViewById(R.id.RegisterButton);
        chose_boy = findViewById(R.id.checked_nam);
        chose_girl = findViewById(R.id.checked_nu);
        signUpDate = getTodaysDate();

        // User click sign up button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("user");

                String name, password, email, gender;
                name = String.valueOf(editTextName.getText());
                password = String.valueOf(editTextPassword.getText());
                email = String.valueOf(editTextMail.getText());
                if (chose_boy.isChecked()) gender = "Nam";
                else gender = "Nu";
                HelperClass helperClass = new HelperClass(name, password, email, date, gender, signUpDate);
                reference.child(name).setValue(helperClass);

                Toast.makeText(RegisterActivity.this, "Đăng kí tài khoản thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // Get today date information: day / month / year
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    // Date Picker for user to select their date of birth
    private void initDatePicker() {
        // Create a DatePickerDialog with Holo theme
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        // Get the current date
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // Use Holo theme here
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    // Convert and format from date to String
    private String makeDateString(int day, int month, int year) {
        date = getMonthFormat(month) + "/" + day + "/" + year;
        return "Ngày sinh: " + getMonthFormat(month) + " " + day + " " + year;
    }

    // Get Month Format from number to String
    private String getMonthFormat(int month) {
        if (month == 1) return "JAN";
        if (month == 2) return "FEB";
        if (month == 3) return "MAR";
        if (month == 4) return "APR";
        if (month == 5) return "MAY";
        if (month == 6) return "JUN";
        if (month == 7) return "JUL";
        if (month == 8) return "AUG";
        if (month == 9) return "SEP";
        if (month == 10) return "OCT";
        if (month == 11) return "NOV";
        if (month == 12) return "DEC";

        return "JAN";
    }

    // open Date picker
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    // when user click on login button
    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}