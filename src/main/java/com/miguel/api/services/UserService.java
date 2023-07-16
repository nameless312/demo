package com.miguel.api.services;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.Firestore;
import com.miguel.api.dto.UserDTO;
import com.miguel.api.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private final Firestore firestore;

    @Autowired
    public UserService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Optional<UserDTO> getUser(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("Users").document(id);
        DocumentSnapshot snapshot = docRef.get().get();

        if (snapshot.exists()) {
            UserDTO user = snapshot.toObject(UserDTO.class);
            return Optional.ofNullable(user);
        } else {
            return Optional.empty(); // User not found
        }
    }

    public boolean createUser(UserDTO userDTO) {
        DocumentReference docRef = firestore.collection("Users").document();

        // Set the fields of the User object
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setCreatedAt(Timestamp.now());

        // Save the User object to Firestore
        try {
            docRef.set(user).get();
            return true; // Document write was successful
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false; // Document write failed
        }
    }

    public boolean updatePassword(String userId, String password) {
        DocumentReference docRef = firestore.collection("Users").document(userId);

        // Update the user document in Firestore
        try {
            docRef.update(
                    FieldPath.of("password"), password,
                    FieldPath.of("updatedAt"), Timestamp.now()
            ).get();
            return true; // Document update was successful
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false; // Document update failed
        }
    }

}
