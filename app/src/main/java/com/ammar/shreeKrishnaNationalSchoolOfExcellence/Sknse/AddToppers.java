package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Objects;

public class AddToppers extends AppCompatActivity {

    FirebaseAuth mAuth;
    String class_name, marks, name;
    Uri PathHolder, ImageFile;
    EditText class_et, videolink_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toppers);

        class_et = findViewById(R.id.class_et);
        videolink_et = findViewById(R.id.video_link);

        mAuth = FirebaseAuth.getInstance();
    }

    public void ChooseExcelFile(View view) {
        Log.d("Excel Sheet", "Selecting" );
        Intent intent = new Intent();
        intent.setType("application/vnd.ms-excel");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Excel Sheet", "Selected" );
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            PathHolder = data.getData();
        }
        if(requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            ImageFile = data.getData();
        }
    }

    public void UploadToppers(View view) {
        class_name = class_et.getText().toString();
        if(TextUtils.isEmpty(class_name))
        {
            class_et.requestFocus();
            class_et.setError("Please Enter Class");
            return;
        }
        final LovelyProgressDialog progressDialog = new LovelyProgressDialog(this);
        progressDialog.setTitle("Adding Toppers");
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
                    String sno = "", name = "", marks = "";
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
                            marks = cell.toString();
                            marks = marks.substring(0,1);
                        }
                        AddDb(class_name, name, sno, marks);
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

    public void ChooseImage(View view) {
        Log.d("Excel Sheet", "Selecting" );
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);
    }

    public class TopperModel{
        String name;
        String marks;

        public TopperModel(String name, String marks) {
            this.name = name;
            this.marks = marks;
        }

        public TopperModel() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMarks() {
            return marks;
        }

        public void setMarks(String marks) {
            this.marks = marks;
        }
    }

    private void AddDb(String class_name, String name, final String sno, String marks) {
        if(class_name.equals("")|| marks.equals("") || name.equals(""))
        {
            Toast.makeText(AddToppers.this, "All Fields are required", Toast.LENGTH_LONG).show();
        }
        else{
            TopperModel topperModel = new TopperModel(name, marks);
            FirebaseFirestore.getInstance().collection("topper" + class_name).document("topper" + sno)
                    .set(topperModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(AddToppers.this, "Topper" + sno + " Added", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(AddToppers.this, "Problem in adding Topper" + sno, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void UploadVideolink(View view) {
        class_name = class_et.getText().toString();
        if(TextUtils.isEmpty(class_name))
        {
            class_et.requestFocus();
            class_et.setError("Please Enter Class");
            return;
        }
        String url = videolink_et.getText().toString();
        if(TextUtils.isEmpty(url))
        {
            videolink_et.setError("Please enter video link");
            videolink_et.requestFocus();
            return;
        }
        final LovelyProgressDialog progressDialog = new LovelyProgressDialog(this);
        progressDialog.setTitle("Adding Link");
        progressDialog.show();
        FirebaseFirestore.getInstance().collection("topper" + class_name).document("motivational")
                .set(url).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(AddToppers.this, "Link added successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(AddToppers.this, "Problem in adding link", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void UploadStudytips(View view) {
        class_name = class_et.getText().toString();
        if(TextUtils.isEmpty(class_name))
        {
            class_et.requestFocus();
            class_et.setError("Please Enter Class");
            return;
        }
        if(ImageFile == null)
        {
            Toast.makeText(AddToppers.this, "Please select image", Toast.LENGTH_SHORT).show();
            return;
        }
        final LovelyProgressDialog progressDialog = new LovelyProgressDialog(this);
        progressDialog.setTitle("Adding Image");
        progressDialog.show();
        StorageReference storage = FirebaseStorage.getInstance().getReference("StudyTips");
        if(ImageFile != null)
        {
            final StorageReference reference = storage.child(System.currentTimeMillis() + "." + getExt(ImageFile));
            UploadTask uploadTask = reference.putFile(ImageFile);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw Objects.requireNonNull(task.getException());
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        String url;
                        if(downloadUrl != null)
                        {
                            url = downloadUrl.toString();
                        }
                        else{
                            progressDialog.dismiss();
                            return;
                        }
                        FirebaseFirestore.getInstance().collection("topper" + class_name)
                                .document("studytips").set(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddToppers.this, "Data saved", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(AddToppers.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(AddToppers.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(AddToppers.this, "All Fields are required", Toast.LENGTH_SHORT).show();
        }
    }
}