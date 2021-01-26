package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.NotesModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class AddBook extends AppCompatActivity {

    private EditText noteTitle, notesClass;
    private FirebaseFirestore db;
    private StorageReference storage;
    private NotesModel notesModel;
    private Uri pdfFile;
    private String class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        notesModel = new NotesModel();
        noteTitle = findViewById(R.id.notes_title);
        notesClass = findViewById(R.id.notes_class);
        storage = FirebaseStorage.getInstance().getReference("Library");
        db = FirebaseFirestore.getInstance();
    }

    public void ChooseFile(View view) {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF File"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            pdfFile = data.getData();
        }
    }

    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void OnUploadPressed(View view) {
        final String title = noteTitle.getText().toString();
        class_name = notesClass.getText().toString();
        if(pdfFile != null && !title.equals("") && !class_name.equals(""))
        {
            final StorageReference reference = storage.child(System.currentTimeMillis() + "." + getExt(pdfFile));
            UploadTask uploadTask = reference.putFile(pdfFile);
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
                        notesModel.setNotesurl(downloadUrl != null ? downloadUrl.toString() : null);
                        notesModel.setSubject(class_name);
                        notesModel.setName(title);
                        db.collection("library").add(notesModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(AddBook.this, "Data saved", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(AddBook.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(AddBook.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(AddBook.this, "All Fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(AddBook.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}