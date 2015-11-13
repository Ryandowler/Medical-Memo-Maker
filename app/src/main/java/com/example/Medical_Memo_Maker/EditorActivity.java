package com.example.Medical_Memo_Maker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditorActivity extends ActionBarActivity {
    public String myImageLink ="";
    private Integer images [] = {R.drawable.pic2, R.drawable.pic3};

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  //System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
    Date date = new Date();

    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldText;

    private EditText editorName; //name
    private String oldName; //name

    private EditText editorAge; //Age
    private String oldAge; //Age

    private TextView editorLastUpdated; //LastUpdated
    private String oldLastUpdated; //LastUpdated

    private TextView editorFee; //fee
    private String oldFee; //fee

    private ImageView editorImage; //image
    private byte[] oldImage; //image


    private String imageLocation;

    private File imageFile;


    private static int RESULT_LOAD_IMAGE = 1;
    String MyFileName = dateFormat.format(date); //name of the files will be the date and time it was taken for uniqueness


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        addImagesToThegallery(); //adds images

        editor = (EditText) findViewById(R.id.editText);
        editorName = (EditText) findViewById(R.id.patientName);//name
        editorAge = (EditText) findViewById(R.id.patientAge);//age
        editorLastUpdated = (TextView) findViewById(R.id.TV_lastUpdated);//age
        editorFee = (TextView) findViewById(R.id.patientFee);//age


        //final Intent intent = getIntent();
        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_note));
        } else {
            action = Intent.ACTION_EDIT;
            noteFilter = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri,
                    DBOpenHelper.ALL_COLUMNS, noteFilter, null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            editor.setText(oldText);
            editor.requestFocus();

            oldName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_NAME));//name
            editorName.setText(oldName);                                              //name

            oldAge = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_AGE));//age
            editorAge.setText(oldAge);                                              //age

            oldLastUpdated = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_LAST_UPDATED));
            editorLastUpdated.setText(oldLastUpdated);

            oldFee = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_FEE));
            String euroSign = "€";
            // editorFee.setText(euroSign + oldFee);
            editorFee.setText(oldFee);




            //editorLastUpdated.setText(dateFormat.format(date));

        }

        Button buttonLoadImg = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new
                        Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    private void addImagesToThegallery() {
        LinearLayout imageGallery = (LinearLayout)findViewById(R.id.imageGallery);
        for(Integer image : images) {
            imageGallery.addView(getImageView(image));
        }

    }

    private View getImageView(Integer image) {
        ImageView imageView = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 10, 0);
        imageView.setLayoutParams(lp);
        imageView.setImageResource(image);
        return imageView;
    }

//    protected void onActivityResult2(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//            ImageView imageView = (ImageView) findViewById(R.id.symptoms);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//        }
//    }

    //takes photo
    public void process(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ryansImage");// directory where to store file and file name
        Uri tempUri = Uri.fromFile(imageFile);
        myImageLink = tempUri.toString();


        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            switch (requestCode) {
                case Activity.RESULT_OK:
                    if (imageFile.exists()) //checking if image exists
                    {
                        Toast.makeText(this, "The file was saved at " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "There was an error saving the file", Toast.LENGTH_LONG).show();
                    }

                    break;
                case Activity.RESULT_CANCELED:


            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        switch (id) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteNote();
                break;
        }
        return true;
    }

    private void deleteNote() {
        getContentResolver().delete(NotesProvider.CONTENT_URI,
                noteFilter, null);
        Toast.makeText(this, getString(R.string.note_deleted),
                Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }


    private void finishEditing() {
        String newText = editor.getText().toString().trim();
        String newName = editorName.getText().toString().trim();
        String newLastUpdated = dateFormat.format(date);
        //String newLastUpdated = editorLastUpdated.getText().toString();
        String ageString = editorAge.getText().toString();
        String feeString = editorFee.getText().toString();


        int newAge = 0;

        boolean ageValid = true; //age variable is an Int
        boolean finished = true; //everthings validated and perfect to be saved
        try {
            newAge = Integer.parseInt(ageString);
        } catch (NumberFormatException e) {
            ageValid = false;
        }

        double newfee = 0;
      boolean feeValid = true; //fee variable is an Int
        try {
            newfee = Double.parseDouble(feeString);
        } catch (NumberFormatException e) {
            feeValid = false;
        }
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0 && newName.length() == 0 && ageString.length() == 0 && feeString.length() == 0) {//if nothing is in the fields
                    //Toast.makeText(this, "Age must be an Integer", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                } else if (newText.length() == 0 || newName.length() == 0 || ageString.length() == 0 || feeString.length() == 0) { //if all fields arent filled in
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    finished = false;
                } else if (!newName.matches("[a-zA-Z]+") && newName.length() != 0) { // check if numbers are in name field
                    editorName.setError(Html.fromHtml("<font color='red'>Name must only contain letters</font>"));

                    finished = false;
                } else if (ageString.length() > 0 && !ageValid) {
                    Toast.makeText(this, "Age must be a number", Toast.LENGTH_SHORT).show();            //checking if age is an integer
                    editorAge.setError(Html.fromHtml("<font color='red'>Must be a number</font>"));
                    finished = false;
                } else {
                    //Toast.makeText(this, "The file was saved at " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    insertNote(newText, newName, newAge, newLastUpdated, newfee, myImageLink);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText.length() == 0 && newName.length() == 0 && ageString.length() == 0 && feeString.length() == 0) {// if nothing is in fields
                    deleteNote();
                    setResult(RESULT_OK);
                    Log.v("myAgeTest", "hh: " + newAge);
                } else if (newText.length() == 0 || newName.length() == 0 || ageString.length() == 0 || feeString.length() == 0) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    finished = false;
                } else if (oldText.equals(newText) && oldName.equals(newName) && oldAge.equals(ageString) && oldFee.equals(feeString)) { //if the text is same as it already was
                    setResult(RESULT_CANCELED);
                    Log.v("myAgeTest", "bb: " + newAge);
                    Toast.makeText(this, "nothing changed", Toast.LENGTH_SHORT).show();
                } else if (!newName.matches("[a-zA-Z]+") && newName.length() != 0) { // check if numbers are in name field
                    editorName.setError(Html.fromHtml("<font color='red'>Name must only contain letters</font>"));
                    finished = false;
                } else {
                    updateNote(newText, newName, newAge, newLastUpdated, newfee);
                    setResult(RESULT_OK);
                }
        }
        if (finished) finish();
    }


    private void updateNote(String noteText, String noteName, int noteAge, String noteLastUpdated, Double noteFee) {

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        values.put(DBOpenHelper.NOTE_NAME, noteName);
        values.put(DBOpenHelper.NOTE_AGE, noteAge);
        values.put(DBOpenHelper.NOTE_LAST_UPDATED, noteLastUpdated);
        values.put(DBOpenHelper.NOTE_FEE, noteFee);

        getContentResolver().update(NotesProvider.CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String noteText, String newName, int newAge, String newLastUpdated, Double newFee, String newMyImageLink) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        values.put(DBOpenHelper.NOTE_NAME, newName);
        values.put(DBOpenHelper.NOTE_AGE, newAge);
        values.put(DBOpenHelper.NOTE_LAST_UPDATED, newLastUpdated);
        values.put(DBOpenHelper.NOTE_FEE, newFee);
        values.put(DBOpenHelper.NOTE_MYIMAGELINK, newMyImageLink);

        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }


}
