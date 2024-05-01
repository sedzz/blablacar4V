package org.cuatrovientos.blabla4v.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.cuatrovientos.blabla4v.R;

import java.util.HashMap;
import java.util.Map;

public class UserSettingsFragment extends Fragment {

    public UserSettingsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        EditText userEmailEditText = view.findViewById(R.id.userEmail);
        EditText userHomeEditText = view.findViewById(R.id.userHome);
        EditText userDniEditText = view.findViewById(R.id.userDni);
        TextView userNameEditText = view.findViewById(R.id.userName);
        TextView editProfileTextView = view.findViewById(R.id.editProfile);
        Button btnSaveChanges = view.findViewById(R.id.btnSaveChanges);

        // Make btnSaveChanges invisible initially
        btnSaveChanges.setVisibility(View.INVISIBLE);

        // Disable editing of the TextViews
        userEmailEditText.setEnabled(false);
        userHomeEditText.setEnabled(false);
        userDniEditText.setEnabled(false);

        final String[] uid = new String[1];
        if (currentUser != null) {
            // The user is authenticated
            uid[0] = currentUser.getUid();
            String email = currentUser.getEmail();

            userEmailEditText.setText(email);

            Toast.makeText(getContext(), "UID: " + uid[0], Toast.LENGTH_SHORT).show();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("user").document(uid[0])
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                // Error getting the document
                                return;
                            }

                            if (snapshot != null && snapshot.exists()) {
                                String home = snapshot.getString("home");
                                String dni = snapshot.getString("dni");
                                String name = snapshot.getString("name");

                                userHomeEditText.setText(home);
                                userDniEditText.setText(dni);
                                userNameEditText.setText(name);
                            } else {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            btnSaveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Recoger los datos de los EditText
                    String email = userEmailEditText.getText().toString();
                    String home = userHomeEditText.getText().toString();
                    String dni = userDniEditText.getText().toString();
                    String name = userNameEditText.getText().toString();

                    // Crear un nuevo objeto de usuario
                    Map<String, Object> user = new HashMap<>();
                    user.put("email", email);
                    user.put("home", home);
                    user.put("dni", dni);
                    user.put("name", name);

                    // Actualizar los datos del usuario en Firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("user").document(uid[0])
                            .update(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Datos actualizados", Toast.LENGTH_SHORT).show();

                                    // Deshabilitar la edición de los EditText
                                    userEmailEditText.setEnabled(false);
                                    userHomeEditText.setEnabled(false);
                                    userDniEditText.setEnabled(false);
                                    userNameEditText.setEnabled(false);

                                    // Ocultar el botón de guardar cambios
                                    btnSaveChanges.setVisibility(View.INVISIBLE);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

            editProfileTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Enable editing of the TextViews when the "Edit" TextView is clicked
                    userEmailEditText.setEnabled(true);
                    userHomeEditText.setEnabled(true);
                    userDniEditText.setEnabled(true);
                    userNameEditText.setEnabled(true);

                    // Make btnSaveChanges visible
                    btnSaveChanges.setVisibility(View.VISIBLE);
                }
            });
        } else {
            // The user is not authenticated
        }

        return view;
    }

}