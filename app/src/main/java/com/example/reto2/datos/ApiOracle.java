package com.example.reto2.datos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.reto2.adaptadores.SucursalAdapter;
import com.example.reto2.adaptadores.ServicioAdapter;
import com.example.reto2.adaptadores.ProductoAdapter;
import com.example.reto2.casos_uso.CasoUsoSucursal;
import com.example.reto2.casos_uso.CasoUsoProducto;
import com.example.reto2.casos_uso.CasoUsoServicio;
import com.example.reto2.modelos.Sucursal;
import com.example.reto2.modelos.Servicio;
import com.example.reto2.modelos.Producto;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiOracle {
    private RequestQueue queue;
    private Context context;
    private CasoUsoSucursal casoUsoSucursal;
    private CasoUsoProducto casoUsoProducto;
    private CasoUsoServicio casoUsoServicio;
    private String urlSucursales = "https://g40a375a88f472a-db202111090751.adb.ca-montreal-1.oraclecloudapps.com/ords/admin/offices/offices";
    private String urlProductos = "https://g40a375a88f472a-db202111090751.adb.ca-montreal-1.oraclecloudapps.com/ords/admin/products/products";
    private String urlServicios = "https://g40a375a88f472a-db202111090751.adb.ca-montreal-1.oraclecloudapps.com/ords/admin/services/services";

    public ApiOracle(Context context) {
        this.queue = Volley.newRequestQueue(context);
        this.context = context;
        casoUsoSucursal = new CasoUsoSucursal();
        casoUsoServicio = new CasoUsoServicio();
        casoUsoProducto = new CasoUsoProducto();
    }

    //  1.Request.Method(POST,PUT,DELETE,GET), 2.URL, 3.JSON, 4.REQUEST, 5.ERROR

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertSucursal(String name, String description, String location, ImageView imageView){
        JSONObject json = new JSONObject();
        String image = casoUsoSucursal.imageViewToString(imageView);
        try {
            json.put("name", name);
            json.put("description", description);
            json.put("location", location);
            json.put("image",image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlSucursales, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertServicios(String name, String description, String price, ImageView imageView){
        JSONObject json = new JSONObject();
        String image = casoUsoServicio.imageViewToString(imageView);
        try {
            json.put("name", name);
            json.put("description", description);
            json.put("price", price);
            json.put("image",image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlServicios, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertProductos(String name, String description, String price, ImageView imageView){
        JSONObject json = new JSONObject();
        String image = casoUsoProducto.imageViewToString(imageView);
        try {
            json.put("name", name);
            json.put("description", description);
            json.put("price", price);
            json.put("image",image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlProductos, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    public void getAllSucursales(GridView gridView, ProgressBar progressBar){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlSucursales, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    ArrayList<Sucursal> list = new ArrayList<>();
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        byte[] image = casoUsoSucursal.stringToByte(jsonObject.getString("image"));

                        Sucursal sucursal = new Sucursal(
                                jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("description"),
                                jsonObject.getString("location"),
                                image
                        );
                        list.add(sucursal);
                    }
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    SucursalAdapter sucursalAdapter = new SucursalAdapter(context, list);
                    gridView.setAdapter(sucursalAdapter);

                } catch (JSONException error) {
                    error.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }
    public void getAllServicios(GridView gridView, ProgressBar progressBar){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlServicios, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    ArrayList<Servicio> list = new ArrayList<>();
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        byte[] image = casoUsoServicio.stringToByte(jsonObject.getString("image"));

                        Servicio servicio = new Servicio(
                                jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("description"),
                                jsonObject.getString("price"),
                                image
                        );
                        list.add(servicio);
                    }
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    ServicioAdapter servicioAdapter = new ServicioAdapter(context, list);
                    gridView.setAdapter(servicioAdapter);

                } catch (JSONException error) {
                    error.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }
    public void getAllProductos(GridView gridView, ProgressBar progressBar){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlProductos, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    ArrayList<Producto> list = new ArrayList<>();
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        byte[] image = casoUsoProducto.stringToByte(jsonObject.getString("image"));

                        Producto producto = new Producto(
                                jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("description"),
                                jsonObject.getString("price"),
                                image
                        );
                        list.add(producto);
                    }
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    ProductoAdapter productoAdapter = new ProductoAdapter(context, list);
                    gridView.setAdapter(productoAdapter);

                } catch (JSONException error) {
                    error.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    public void deleteSucursal(String id){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, urlSucursales + "/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    public void deleteServicio(String id){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, urlServicios + "/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    public void deleteProducto(String id){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, urlProductos + "/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    public void getSucursalById(String id, ImageView imageView, EditText name, EditText description, TextView location, GoogleMap googleMap){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlSucursales + "/" +id, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    byte[] image = casoUsoSucursal.stringToByte(jsonObject.getString("image"));

                    Sucursal sucursal = new Sucursal(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("description"),
                            jsonObject.getString("location"),
                            image
                    );
                    byte[] imageSuc = sucursal.getImage();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageSuc, 0, imageSuc.length);
                    imageView.setImageBitmap(bitmap);
                    name.setText(sucursal.getName());
                    description.setText(sucursal.getDescription());
                    location.setText(sucursal.getLocation());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(sucursal.locationToCoord());
                    markerOptions.title(sucursal.getName());
                    googleMap.clear();
                    googleMap.addMarker(markerOptions);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }
    public void getServicioById(String id, ImageView imageView, EditText name, EditText description, TextView price){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlServicios + "/" +id, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    byte[] image = casoUsoServicio.stringToByte(jsonObject.getString("image"));

                    Servicio servicio = new Servicio(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("description"),
                            jsonObject.getString("price"),
                            image
                    );
                    byte[] imageSuc = servicio.getImage();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageSuc, 0, imageSuc.length);
                    imageView.setImageBitmap(bitmap);
                    name.setText(servicio.getName());
                    description.setText(servicio.getDescription());
                    price.setText(servicio.getPrice());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }
    public void getProductoById(String id, ImageView imageView, EditText name, EditText description, TextView price){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlProductos + "/" +id, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    byte[] image = casoUsoProducto.stringToByte(jsonObject.getString("image"));

                    Producto producto = new Producto(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("description"),
                            jsonObject.getString("price"),
                            image
                    );
                    byte[] imageSuc = producto.getImage();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageSuc, 0, imageSuc.length);
                    imageView.setImageBitmap(bitmap);
                    name.setText(producto.getName());
                    description.setText(producto.getDescription());
                    price.setText(producto.getPrice());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateSucursal(String id, String name, String description, String location, ImageView imageView){

        JSONObject json = new JSONObject();
        String image = casoUsoSucursal.imageViewToString(imageView);

        try {
            json.put("id", id);
            json.put("name", name);
            json.put("description", description);
            json.put("location", location);
            json.put("image", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlSucursales, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
            queue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateServicio(String id, String name, String description, String price, ImageView imageView){

        JSONObject json = new JSONObject();
        String image = casoUsoServicio.imageViewToString(imageView);

        try {
            json.put("id", id);
            json.put("name", name);
            json.put("description", description);
            json.put("price", price);
            json.put("image", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlServicios, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateProducto(String id, String name, String description, String price, ImageView imageView){

        JSONObject json = new JSONObject();
        String image = casoUsoProducto.imageViewToString(imageView);

        try {
            json.put("id", id);
            json.put("name", name);
            json.put("description", description);
            json.put("price", price);
            json.put("image", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlProductos, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

}
