package de.spumf.spumf_android_lib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class RecallActivity extends AppCompatActivity {

    protected DatabaseManager dm;
    private final Integer SIGN_IN = 77;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm = DatabaseManager.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!dm.signedIn()){
            List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                signInResult(true, FirebaseAuth.getInstance().getCurrentUser());
            }else{
                signInResult(false, null);
            }
        }
    }

    public abstract void update(ArrayList<?> values, String name);

    public void signInResult(boolean success, FirebaseUser user){
        Toast.makeText(this, "Authentication" + (success ? " successfull" : " failed"),
                Toast.LENGTH_SHORT).show();
    }

}
