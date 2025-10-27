package com.example.prueba;

import android.content.Context;
import android.util.Log;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.PublicKeyCredential;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.core.content.ContextCompat;
import org.json.JSONException;
import org.json.JSONObject;

public class PasskeyManager {

    public interface PasskeyCallback {
        void onPasskeySuccess(String credentialJson);
        void onPasskeyError(String error);
    }

    private static final String TAG = "PasskeyManager";
    private final CredentialManager credentialManager;
    private final Context context;

    public PasskeyManager(Context context) {
        this.context = context;
        this.credentialManager = CredentialManager.create(context);
    }

    public void signInWithPasskey(PasskeyCallback callback) {
        // Crear JSON de solicitud para autenticación con Passkey
        String requestJson = createPasskeyRequestJson();
        
        // API moderna: GetPublicKeyCredentialOption con JSON de solicitud
        androidx.credentials.GetPublicKeyCredentialOption publicKeyOption = 
                new androidx.credentials.GetPublicKeyCredentialOption(requestJson);

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(publicKeyOption)
                .build();

        credentialManager.getCredentialAsync(
                context,
                request,
                null,
                ContextCompat.getMainExecutor(context),
                new androidx.credentials.CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handlePasskeySignIn(result, callback);
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        Log.e(TAG, "Error en autenticación con Passkey", e);
                        callback.onPasskeyError("Error: " + e.getMessage());
                    }
                }
        );
    }

    private void handlePasskeySignIn(GetCredentialResponse response, PasskeyCallback callback) {
        Credential credential = response.getCredential();

        if (credential instanceof PublicKeyCredential) {
            PublicKeyCredential pkCred = (PublicKeyCredential) credential;
            // API moderna: usar getAuthenticationResponseJson() para obtener datos de Passkey
            String responseJson = pkCred.getAuthenticationResponseJson();

            try {
                JSONObject json = new JSONObject(responseJson);
                if (json.has("signature") && json.has("authenticatorData")) {
                    callback.onPasskeySuccess(responseJson);
                } else {
                    callback.onPasskeyError("Respuesta de Passkey incompleta.");
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error al parsear respuesta", e);
                callback.onPasskeySuccess(responseJson);
            }
        } else {
            callback.onPasskeyError("Credencial no compatible. Solo se admiten Passkeys.");
        }
    }
    
    /**
     * Crea el JSON de solicitud para autenticación con Passkey
     */
    private String createPasskeyRequestJson() {
        // Este es un ejemplo básico. En una implementación real, necesitarías
        // generar un challenge aleatorio y configurar los parámetros específicos
        return "{\n" +
                "  \"challenge\": \"random-challenge-string\",\n" +
                "  \"timeout\": 60000,\n" +
                "  \"rpId\": \"example.com\",\n" +
                "  \"allowCredentials\": [],\n" +
                "  \"userVerification\": \"preferred\"\n" +
                "}";
    }
}