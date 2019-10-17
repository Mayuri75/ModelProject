package simple.android.example.activities;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mahu.example.R;
import com.google.zxing.Result;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class LoginActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;

    private FingerprintHandler fingerprintHandler;

    private static final String FINGERPRINT_KEY = "key_name";

    private static final int REQUEST_USE_FINGERPRINT = 300;

  //  ActivityLoginBinding binding;

    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    String testBarCode = "Z123456";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       /* binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        binding.scanner.setResultHandler(LoginActivity.this);
        binding.scanner.startCamera(camId);*/
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            try{
                fingerprintHandler = new FingerprintHandler(this);


                fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            }catch (Exception e){
                e.printStackTrace();
            }

            // check support for android fingerprint on device
            try{
                checkDeviceFingerprintSupport();
                //generate fingerprint keystore
                generateFingerprintKeyStore();
                //instantiate Cipher class
                Cipher mCipher = instantiateCipher();
                if(mCipher != null){
                    cryptoObject = new FingerprintManager.CryptoObject(mCipher);
                }
                fingerprintHandler.completeFingerAuthentication(fingerprintManager, cryptoObject);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkDeviceFingerprintSupport() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, REQUEST_USE_FINGERPRINT);
        } else {
            if (!fingerprintManager.isHardwareDetected()) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.alrt_fingerprint), Toast.LENGTH_LONG).show();
            }
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.alrt_fingerprint_configured), Toast.LENGTH_LONG).show();
            }
            if (!keyguardManager.isKeyguardSecure()) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.alrt_screen_lock), Toast.LENGTH_LONG).show();
            }
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateFingerprintKeyStore(){
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            keyGenerator.init(new KeyGenParameterSpec.Builder(FINGERPRINT_KEY, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        try{
            keyGenerator.generateKey();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private Cipher instantiateCipher(){
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyStore.load(null);
            SecretKey secretKey = (SecretKey)keyStore.getKey(FINGERPRINT_KEY, null);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnrecoverableKeyException |
                CertificateException | IOException | KeyStoreException | InvalidKeyException e) {
            throw new RuntimeException("Failed to instantiate Cipher class");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_USE_FINGERPRINT){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // check support for android fingerprint on device
                checkDeviceFingerprintSupport();
                //generate fingerprint keystore
                generateFingerprintKeyStore();
                //instantiate Cipher class
                Cipher mCipher = instantiateCipher();
                if(mCipher != null){
                    cryptoObject = new FingerprintManager.CryptoObject(mCipher);
                }
            }
            else{
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);


            }
        }else{
            Toast.makeText(this,"Error", Toast.LENGTH_LONG).show();
            finish();
            overridePendingTransition( 0, 0);
            startActivity(getIntent());
            overridePendingTransition( 0, 0);

        }
    }

    @Override
    public void handleResult(Result result) {
        if(testBarCode.equalsIgnoreCase(result.getText().toString())) {
            Toast.makeText(this, "Perfect", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public class FingerprintHandler extends FingerprintManager.AuthenticationCallback{

        private final String TAG = FingerprintHandler.class.getSimpleName();

        private Context context;

        public FingerprintHandler(Context context){
            this.context = context;
        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            Log.d(TAG, "Error message " + errorCode + ": " + errString);
            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();




        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);

            // Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
            //  mPref.saveOriginalUserpwd(mUser.getEmail());

            // userString = mPref.getUserData();
            //mUser = mGson.fromJson(userString, UserObject.class);
            //if(mUser != null){
            //  Toast.makeText(context, context.getString(R.string.auth_successful), Toast.LENGTH_LONG).show();
//                if(mUser.isLoginOption()){
//                    // login with fingerprint and password
//                    showPasswordAuthentication(context);
//                }
//                else{
            // login with only fingerprint
               /* Intent userIntent = new Intent(context, UserProfileActivity.class);
                userIntent.putExtra("USER_BIO", mUser.getUsername());
                context.startActivity(userIntent);*/


        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void completeFingerAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try{
                fingerprintManager.authenticate(cryptoObject, new CancellationSignal(), 0, this, null);
            }catch (SecurityException ex) {
                Log.d(TAG, "An error occurred:\n" + ex.getMessage());
            } catch (Exception ex) {
                Log.d(TAG, "An error occurred\n" + ex.getMessage());
            }
        }
    }

}
