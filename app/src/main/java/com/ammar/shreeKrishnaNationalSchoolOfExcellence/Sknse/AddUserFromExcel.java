package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.NewUserModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

public class AddUserFromExcel extends AppCompatActivity {

    FirebaseAuth mAuth;
    String class_name, rollno, yoa, dob, name, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_from_excel);

        Log.d("Excel Sheet", "In Activity" );
        mAuth = FirebaseAuth.getInstance();

    }

    public void ChooseExcelFile(View view) {
        Log.d("Excel Sheet", "Selecting" );
        Intent intent = new Intent();
        intent.setType("application/vnd.ms-excel");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LovelyProgressDialog progressDialog = new LovelyProgressDialog(this);
        progressDialog.setTitle("Adding Users");
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Excel Sheet", "Selected" );
        Uri PathHolder = data.getData();
        FileInputStream fileInputStream = null;
        StringBuilder text = new StringBuilder();
        progressDialog.show();
        try {
            Log.d("Excel Sheet", "Reading" );
            InputStream inputStream = getContentResolver().openInputStream(PathHolder);
            POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileSystem);
            HSSFSheet mySheet = hssfWorkbook.getSheetAt(0);

            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowNo = 0;
            while(rowIter.hasNext())
            {
                HSSFRow row = (HSSFRow) rowIter.next();
                if(rowNo != 0)
                {
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int colNo = 0;
                    String sno = "", name = "";
                    while(cellIterator.hasNext())
                    {
                        HSSFCell cell = (HSSFCell) cellIterator.next();
                        if(colNo == 0)
                        {
                            sno = cell.toString();
                        }
                        else if(colNo == 1)
                        {
                            name = cell.toString();
                        }
                        else if(colNo == 2)
                        {
                            username = cell.toString();
                        }
                        else if(colNo == 3)
                        {
                            class_name = cell.toString();
                        }
                        else if(colNo == 4)
                        {
                            dob = cell.toString();
                        }
                        else if(colNo == 5)
                        {
                            yoa = cell.toString();
                        }
                        else if(colNo == 6)
                        {
                            rollno = cell.toString();
                        }
                        AddUser(username, class_name, rollno, yoa, dob, name);
                        colNo++;
                    }
                    Log.d("Excel Sheet", sno + "    " + name);
                }
                rowNo ++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    public void AddUser(String username, String class_name, String rollno, String yoa, String dob, String name)
    {
        if(username.equals("") || class_name.equals("") || rollno.equals("") || yoa.equals("") || dob.equals("") || name.equals(""))
        {
            Toast.makeText(AddUserFromExcel.this, "All Fields are required", Toast.LENGTH_LONG).show();
        }
        else {
            final HashMap<String, String> userModel = new HashMap<>();
            userModel.put("class", class_name);
            userModel.put("dob", dob);
            userModel.put("email", username);
            userModel.put("yearofadmission", yoa);
            userModel.put("rollno", rollno);
            userModel.put("name", name);
            mAuth.createUserWithEmailAndPassword(username, name + rollno).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getUser() != null)
                    {
                        String uid = task.getResult().getUser().getUid();
                        userModel.put("uid", uid);
                        FirebaseFirestore.getInstance().collection("users").document(uid)
                                .set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(AddUserFromExcel.this, "User Added", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(AddUserFromExcel.this, "Problem in adding user", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(AddUserFromExcel.this, "Problem in adding user", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}