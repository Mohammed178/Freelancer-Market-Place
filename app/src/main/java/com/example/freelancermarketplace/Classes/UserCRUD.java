package com.example.freelancermarketplace.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserCRUD {
    private DatabaseReference databaseReference;

    public UserCRUD() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    // Create or Update User
    public void saveUser(User user) {
        databaseReference.child(user.getUserId()).setValue(user);
    }

    // Read User by ID
    public void getUser(String userId, final ValueEventListener listener) {
        databaseReference.child(userId).addListenerForSingleValueEvent(listener);
    }

    // Update User
    public void updateUser(String userId, User updatedUser) {
        databaseReference.child(userId).setValue(updatedUser);
    }

    // Delete User
    public void deleteUser(String userId) {
        databaseReference.child(userId).removeValue();
    }

    public void getAllUsers(final ValueEventListener listener) {
        databaseReference.addListenerForSingleValueEvent(listener);
    }
}
