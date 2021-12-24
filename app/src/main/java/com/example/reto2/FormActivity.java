package com.example.reto2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reto2.datos.ApiOracle;
import com.example.reto2.datos.DBHelper;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class FormActivity extends AppCompatActivity {
    private final int REQUEST_CODE_GALLERY = 999;
    private TextView tvTitulo;
    private EditText campo1, campo2, campo3, editId;
    private Button btnChoose, btnInsertar, btnEliminar, btnConsultar, btnActualizar;
    private ImageView imgSelected;
    String name = "";
    private DBHelper dbHelper;
    private ApiOracle apiOracle;

    String campo1Insert;
    String campo2Insert;
    String campo3Insert;
    byte[] imageInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        tvTitulo = (TextView) findViewById(R.id.tvTitulo);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        editId = (EditText) findViewById(R.id.editIdItem);
        campo1 = (EditText) findViewById(R.id.editCampo1);
        campo2 = (EditText) findViewById(R.id.editCampo2);
        campo3 = (EditText) findViewById(R.id.editCampo3);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnInsertar = (Button) findViewById(R.id.btnInsertar);
        btnConsultar = (Button) findViewById(R.id.btnConsultar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        imgSelected = (ImageView) findViewById(R.id.imgSelected);
        dbHelper = new DBHelper(getApplicationContext());
        apiOracle = new ApiOracle(getApplicationContext());

        tvTitulo.setText(name);
        if(name.equals("PRODUCTOS")){
            campo1.setHint("Name");
            campo2.setHint("Description");
            campo3.setHint("Price");
        }else if(name.equals("SERVICIOS")){
            campo1.setHint("Name");
            campo2.setHint("Description");
            campo3.setHint("Price");
        }else if(name.equals("SUCURSALES")){
            campo1.setHint("Name");
            campo2.setHint("Description");
            campo3.setHint("Location");
            campo3.setInputType(InputType.TYPE_CLASS_TEXT);
        }


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        FormActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String idInsert = editId.getText().toString();
                String nameInsert = campo1.getText().toString();
                String descriptionInsert = campo2.getText().toString();
                String priceInsert = campo3.getText().toString();
                byte[] imageInsert = imageViewToByte(imgSelected);
                apiOracle.insertProductos(nameInsert, descriptionInsert, priceInsert, imgSelected);
                limpiarCampos();
                Toast.makeText(getApplicationContext(), "Insert Success", Toast.LENGTH_SHORT).show();

            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiOracle.getProductoById(
                        editId.getText().toString(),
                        imgSelected,
                        campo1,
                        campo2,
                        campo3

                );
                Toast.makeText(getApplicationContext(), "Consult Success", Toast.LENGTH_SHORT).show();

            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                apiOracle.updateProducto(
                        editId.getText().toString(),
                        campo1.getText().toString(),
                        campo2.getText().toString(),
                        campo3.getText().toString(),
                        imgSelected
                );
                limpiarCampos();
                Toast.makeText(getApplicationContext(), "Update Success", Toast.LENGTH_SHORT).show();

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiOracle.deleteProducto(editId.getText().toString());
                limpiarCampos();
                Toast.makeText(getApplicationContext(), "Delete Success", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public byte[] imageViewToByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void llenarCampos(){
        campo1Insert = campo1.getText().toString().trim();
        campo2Insert = campo2.getText().toString().trim();
        campo3Insert = campo3.getText().toString().trim();
        imageInsert = imageViewToByte(imgSelected);
    }

    public void limpiarCampos(){
        campo1.setText("");
        campo2.setText("");
        campo3.setText("");
        imgSelected.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }else{
                Toast.makeText(getApplicationContext(), "Sin Permisos", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgSelected.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}