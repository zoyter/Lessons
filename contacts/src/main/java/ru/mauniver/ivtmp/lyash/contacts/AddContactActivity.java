package ru.mauniver.ivtmp.lyash.contacts;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/** @noinspection deprecation*/
public class AddContactActivity extends AppCompatActivity {
    private ImageView mContactImage;
    private Uri mContactImageUri;

    private static final int IMAGE_CAPTURE_REQUEST_CODE = 7777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWindow();
        mContactImage = findViewById(R.id.profile_image);

        EditText nameEditText = findViewById(R.id.name_et);
        EditText EmailEditText = findViewById(R.id.email_et);
        EditText PhoneEditText = findViewById(R.id.phone_et);

        Button addContactButton = findViewById(R.id.button_add);
        addContactButton.setOnClickListener(v -> {
            if (mContactImageUri == null) {
                Toast.makeText(this, "Contact image not set!", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = nameEditText.getText().toString();
            String email = EmailEditText.getText().toString();
            String phone = PhoneEditText.getText().toString();

            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_USER, name);
            i.putExtra(Intent.EXTRA_EMAIL, email);
            i.putExtra(Intent.EXTRA_PHONE_NUMBER, phone);
            i.putExtra(Intent.EXTRA_ORIGINATING_URI, mContactImageUri.toString());

            setResult(RESULT_OK, i);
            finish();
        });

        Button takePhotoButton = findViewById(R.id.button_camera);
        takePhotoButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Error while trying to open camera app", Toast.LENGTH_SHORT).show();
            }
        });

        Button cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Меняем ImageView с изображением контакта
            mContactImage.setImageBitmap(imageBitmap);

            String filename = "contact_" + System.currentTimeMillis() + ".png";

            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(outputFile);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                // Сохраняем путь к файлу в формате Uri
                mContactImageUri = Uri.fromFile(outputFile);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(RESULT_CANCELED);
    }

    private void setWindow() {
        // Метод устанавливает StatusBar в цвет фона
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.activity_background));

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
}