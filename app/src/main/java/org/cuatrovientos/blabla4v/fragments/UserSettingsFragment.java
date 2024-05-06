package org.cuatrovientos.blabla4v.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import org.cuatrovientos.blabla4v.activities.LoginActivity;
import org.cuatrovientos.blabla4v.activities.MainActivity;

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


        TextView userEmailTextView = view.findViewById(R.id.userEmail);
        EditText userHomeEditText = view.findViewById(R.id.userHome);
        EditText userDniEditText = view.findViewById(R.id.userDni);
        TextView userNameEditText = view.findViewById(R.id.userName);
        TextView editProfileTextView = view.findViewById(R.id.editProfile);

        Button btnSaveChanges = view.findViewById(R.id.btnSaveChanges);
        Button btnChangePassword = view.findViewById(R.id.btnChangePassword);
        // Make btnSaveChanges invisible initially
        btnSaveChanges.setVisibility(View.INVISIBLE);

        userHomeEditText.setEnabled(false);
        userDniEditText.setEnabled(false);

        final String[] uid = new String[1];
        if (currentUser != null) {
            // The user is authenticated
            uid[0] = currentUser.getUid();
            String email = currentUser.getEmail();

            userEmailTextView.setText(email);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("user").document(uid[0])
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            if (snapshot != null && snapshot.exists()) {
                                String home = snapshot.getString("home");
                                String dni = snapshot.getString("dni");
                                String name = snapshot.getString("name");

                                userNameEditText.setText(name);
                                userHomeEditText.setText(home);
                                userDniEditText.setText(dni);
                            } else {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            btnSaveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = userEmailTextView.getText().toString();
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
                                    userEmailTextView.setEnabled(false);
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
                    boolean isEditing = userHomeEditText.isEnabled();

                    if (isEditing) {
                        userHomeEditText.setEnabled(false);
                        userDniEditText.setEnabled(false);

                        editProfileTextView.setText("Editar");

                        btnSaveChanges.setVisibility(View.INVISIBLE);
                    } else {
                        userHomeEditText.setEnabled(true);
                        userDniEditText.setEnabled(true);

                        editProfileTextView.setText("Cancelar");

                        // Mostrar el botón de guardar cambios
                        btnSaveChanges.setVisibility(View.VISIBLE);
                    }
                }
            });

            btnChangePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = userEmailTextView.getText().toString();

                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Correo de restablecimiento de contraseña enviado", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Error al enviar el correo de restablecimiento de contraseña", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }

        return view;
    }

}