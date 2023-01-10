package hugo.monton.blanco.e09_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hugo.monton.blanco.e09_firebase.modelos.Persona;

public class MainActivity extends AppCompatActivity {

    private TextView lblFrase;
    private EditText txtFrase;
    private Button btnGuardar;

    private List<Persona> personas;

    private FirebaseDatabase database;
    private DatabaseReference refFrase;
    private DatabaseReference refPersona;
    private DatabaseReference refPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializaVistas();

        personas = new ArrayList<>();
        crearPersonas();

        database = FirebaseDatabase.getInstance("https://e09-firebase-default-rtdb.europe-west1.firebasedatabase.app/"); // Poniendo la URL conseguimos controlar el error de que pueda no encontrar la base de datos dependiendo de la ubicación de su servidor.

        // Referencias
        refFrase = database.getReference("frase"); // Si no se pone nada en el 'path' irá a la raiz, si pones algo, irá al nodo con ese nombre. Y si la ruta de nodo no existe, lo creará.
        refPersona = database.getReference("persona"); // Si no se pone nada en el 'path' irá a la raiz, si pones algo, irá al nodo con ese nombre. Y si la ruta de nodo no existe, lo creará.
        refPersonas = database.getReference("personas");

        // Escribir PERSONA.
        Persona p = new Persona("Edu", 20);
        refPersona.setValue(p);
        // refPersonas.setValue(personas); // -> Lo comentamos para que no vuelva a subir los usuarios.


        // Escritura en la base de datos.
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refFrase.setValue(txtFrase.getText().toString());
                personas.remove(9);
                refPersonas.setValue(personas);
            }
        });

        // Lectura del modelo de datos: 'Persona'.
        refPersona.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Persona p = snapshot.getValue(Persona.class);
                    Toast.makeText(MainActivity.this, "Nombre: " + p.getNombre(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "El nodo no existe.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // LECTURA ARRAY PERSONAS
        refPersonas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    // El siguiente objeto es el que nos da Firebase para poder mapear Arraylist<> de nuestros modelos de datos o arrays.
                    GenericTypeIndicator<ArrayList<Persona>> gti = new GenericTypeIndicator<ArrayList<Persona>>() {};
                    ArrayList<Persona> personasTemp = snapshot.getValue((gti));
                    Toast.makeText(MainActivity.this, "Descargdoss " + personasTemp.size() + " usuarios", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "No existe el nodo.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // El siguiente método es el que da el tiempo real siempre.
        refFrase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String frase = snapshot.getValue(String.class);
                    lblFrase.setText(frase);
                }else{
                    Toast.makeText(MainActivity.this, "El nodo: '" + snapshot.getRef() + "',' no existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void crearPersonas() {
        for (int i = 0; i < 1000; i++) {
            personas.add(new Persona("Nombre " + i, 20 + i));
        }
    }

    private void inicializaVistas() {
        lblFrase = findViewById(R.id.lblFrase);
        txtFrase = findViewById(R.id.txtFrase);
        btnGuardar = findViewById(R.id.btnGuardar);
    }
}