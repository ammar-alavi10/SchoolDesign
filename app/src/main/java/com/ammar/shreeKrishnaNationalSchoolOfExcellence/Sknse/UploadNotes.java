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

public class UploadNotes extends AppCompatActivity {

    private EditText noteTitle;
    private FirebaseFirestore db;
    private StorageReference storage;
    private NotesModel notesModel;
    private Uri pdfFile;
    private String subject, class_name, chapter_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes);

        notesModel = new NotesModel();
        subject = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");
        chapter_no = getIntent().getStringExtra("chapter_no");
        noteTitle = findViewById(R.id.notes_title);
        storage = FirebaseStorage.getInstance().getReference("Notes");
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

    public void ShowNotes(View view) {
        Intent intent = new Intent(UploadNotes.this, ShowNotesList.class);
        intent.putExtra("subject_name", subject);
        intent.putExtra("class_name", class_name);
        intent.putExtra("chapter_no", chapter_no);
        startActivity(intent);
    }

    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void OnUploadPressed(View view) {
        final String title = noteTitle.getText().toString();
        if(pdfFile != null && !title.equals(""))
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
                        notesModel.setSubject(subject + class_name + chapter_no);
                        notesModel.setName(title);
                        db.collection("notes").add(notesModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(UploadNotes.this, "Data saved", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(UploadNotes.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(UploadNotes.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(UploadNotes.this, "All Fields are required", Toast.LENGTH_SHORT).show();
        }
    }
}