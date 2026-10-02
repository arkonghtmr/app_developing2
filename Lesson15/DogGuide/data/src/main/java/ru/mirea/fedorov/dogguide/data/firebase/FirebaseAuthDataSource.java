package ru.mirea.fedorov.dogguide.data.firebase;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

public class FirebaseAuthDataSource {
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthDataSource() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser login(String email, String password) throws Exception {
        AuthResult result = Tasks.await(
                firebaseAuth.signInWithEmailAndPassword(email, password),
                15,
                TimeUnit.SECONDS
        );
        return result.getUser();
    }

    public FirebaseUser register(String email, String password) throws Exception {
        AuthResult result = Tasks.await(
                firebaseAuth.createUserWithEmailAndPassword(email, password),
                15,
                TimeUnit.SECONDS
        );
        return result.getUser();
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
}
