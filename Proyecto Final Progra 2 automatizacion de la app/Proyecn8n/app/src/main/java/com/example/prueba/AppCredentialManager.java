package com.example.prueba;

import android.content.Context;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.PasswordCredential;
import androidx.credentials.PublicKeyCredential;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.credentials.exceptions.NoCredentialException;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import java.util.concurrent.Executor;

/**
 * AppCredentialManager - Gestión moderna de credenciales con Credential Manager API 1.2.0+
 * Compatible con minSdk 23, targetSdk 34
 * 
 * NOTA: CredentialOption ha sido eliminado en la API moderna de Credential Manager.
 * Se usa directamente GetCredentialRequest.Builder() con instancias específicas de credenciales.
 */
public class AppCredentialManager {
    
    private static final String TAG = "AppCredentialManager";
    private final CredentialManager credentialManager;
    private final Context context;
    private final Executor executor;
    
    public interface CredentialCallback {
        void onCredentialSuccess(String credential);
        void onCredentialError(String error);
    }
    
    public interface UniversalCredentialCallback {
        void onCredentialSuccess(Credential credential);
        void onCredentialError(String error);
    }
    
    public AppCredentialManager(Context context) {
        this.context = context;
        this.credentialManager = CredentialManager.create(context);
        this.executor = ContextCompat.getMainExecutor(context);
    }
    
    /**
     * Autenticación con Google usando Credential Manager API moderna
     */
    public void signInWithGoogle(String clientId, CredentialCallback callback) {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(clientId)
                .build();
        
        // API moderna: GetCredentialRequest.Builder() con instancias directas
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();
        
        getCredential(request, new UniversalCredentialCallback() {
            @Override
            public void onCredentialSuccess(Credential credential) {
                if (credential instanceof GoogleIdTokenCredential) {
                    String idToken = ((GoogleIdTokenCredential) credential).getIdToken();
                    callback.onCredentialSuccess(idToken);
                } else {
                    callback.onCredentialError("Credencial no es de tipo GoogleIdTokenCredential");
                }
            }
            
            @Override
            public void onCredentialError(String error) {
                callback.onCredentialError(error);
            }
        });
    }
    
    /**
     * Autenticación con Passkey usando Credential Manager API moderna
     */
    public void signInWithPasskey(String rpId, UniversalCredentialCallback callback) {
        // Crear PublicKeyCredentialRequestOptions directamente
        String requestJson = createPasskeyRequestJson(rpId);
        
        // API moderna: GetCredentialRequest.Builder() con instancias directas
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(new androidx.credentials.GetPublicKeyCredentialOption(requestJson))
                .build();
        
        getCredential(request, callback);
    }
    
    /**
     * Autenticación con contraseña usando Credential Manager API moderna
     */
    public void signInWithPassword(String username, UniversalCredentialCallback callback) {
        // API moderna: GetPasswordOption sin parámetros (constructor vacío)
        androidx.credentials.GetPasswordOption passwordOption = new androidx.credentials.GetPasswordOption();
        
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(passwordOption)
                .build();
        
        getCredential(request, callback);
    }
    
    /**
     * Método universal para obtener credenciales que maneja GoogleIdTokenCredential, 
     * PublicKeyCredential y PasswordCredential
     */
    public void getCredential(GetCredentialRequest request, UniversalCredentialCallback callback) {
        // Firma correcta: getCredentialAsync(Context, GetCredentialRequest, CancellationSignal, Executor, Callback)
        credentialManager.getCredentialAsync(
                context,                    // Context como primer parámetro
                request,                    // GetCredentialRequest como segundo parámetro
                null,                       // CancellationSignal (puede ser null)
                executor,                   // Executor
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleUniversalCredential(result, callback);
                    }
                    
                    @Override
                    public void onError(GetCredentialException e) {
                        Log.e(TAG, "Error getting credential", e);
                        if (e instanceof NoCredentialException) {
                            callback.onCredentialError("No se encontraron credenciales");
                        } else {
                            callback.onCredentialError("Error de autenticación: " + e.getMessage());
                        }
                    }
                }
        );
    }
    
    /**
     * Maneja diferentes tipos de credenciales: GoogleIdTokenCredential, PublicKeyCredential, PasswordCredential
     */
    private void handleUniversalCredential(GetCredentialResponse response, UniversalCredentialCallback callback) {
        Credential credential = response.getCredential();
        
        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;
            
            // Manejar GoogleIdTokenCredential
            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(customCredential.getType())) {
                handleGoogleIdTokenCredential(customCredential, callback);
            } else {
                callback.onCredentialError("Tipo de credencial personalizada no soportado: " + customCredential.getType());
            }
        } 
        // Manejar PublicKeyCredential (Passkeys)
        else if (credential instanceof PublicKeyCredential) {
            handlePublicKeyCredential((PublicKeyCredential) credential, callback);
        } 
        // Manejar PasswordCredential
        else if (credential instanceof PasswordCredential) {
            handlePasswordCredential((PasswordCredential) credential, callback);
        } 
        else {
            callback.onCredentialError("Tipo de credencial no soportado: " + credential.getClass().getSimpleName());
        }
    }
    
    /**
     * Maneja GoogleIdTokenCredential - En la API moderna de Credential Manager,
     * GoogleIdTokenCredential.createFrom() NO lanza GoogleIdTokenParsingException
     */
    private void handleGoogleIdTokenCredential(CustomCredential customCredential, UniversalCredentialCallback callback) {
        // En la API moderna de Credential Manager, GoogleIdTokenCredential.createFrom() 
        // no lanza GoogleIdTokenParsingException, por lo que no necesita try-catch
        GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(customCredential.getData());
        String idToken = googleIdTokenCredential.getIdToken();
        Log.d(TAG, "Google ID Token obtenido exitosamente");
        callback.onCredentialSuccess(googleIdTokenCredential);
    }
    
    /**
     * Maneja PublicKeyCredential (Passkeys) - usar getAuthenticationResponseJson() en la API moderna
     */
    private void handlePublicKeyCredential(PublicKeyCredential publicKeyCredential, UniversalCredentialCallback callback) {
        try {
            // API moderna: usar getAuthenticationResponseJson() para obtener datos de Passkey
            String responseJson = publicKeyCredential.getAuthenticationResponseJson();
            
            Log.d(TAG, "PublicKeyCredential obtenido exitosamente");
            Log.d(TAG, "Authentication Response JSON: " + responseJson);
            
            // El JSON contiene toda la información de la autenticación WebAuthn:
            // - credentialId
            // - authenticatorData
            // - signature
            // - userHandle
            // - clientDataJSON
            
            callback.onCredentialSuccess(publicKeyCredential);
        } catch (Exception e) {
            Log.e(TAG, "Error procesando PublicKeyCredential", e);
            callback.onCredentialError("Error procesando Passkey: " + e.getMessage());
        }
    }
    
    /**
     * Maneja PasswordCredential
     */
    private void handlePasswordCredential(PasswordCredential passwordCredential, UniversalCredentialCallback callback) {
        try {
            String username = passwordCredential.getId();
            String password = passwordCredential.getPassword();
            Log.d(TAG, "PasswordCredential obtenido exitosamente para usuario: " + username);
            callback.onCredentialSuccess(passwordCredential);
        } catch (Exception e) {
            Log.e(TAG, "Error procesando PasswordCredential", e);
            callback.onCredentialError("Error procesando credencial de contraseña: " + e.getMessage());
        }
    }
    
    /**
     * Crea el JSON de solicitud para autenticación con Passkey
     */
    private String createPasskeyRequestJson(String rpId) {
        // Este es un ejemplo básico. En una implementación real, necesitarías
        // generar un challenge aleatorio y configurar los parámetros específicos
        return "{\n" +
                "  \"challenge\": \"random-challenge-string\",\n" +
                "  \"timeout\": 60000,\n" +
                "  \"rpId\": \"" + rpId + "\",\n" +
                "  \"allowCredentials\": [],\n" +
                "  \"userVerification\": \"preferred\"\n" +
                "}";
    }
    
    /**
     * Método de utilidad para crear GetCredentialRequest con múltiples opciones
     * Usando la API moderna con GetCredentialRequest.Builder()
     */
    public GetCredentialRequest createCredentialRequest(String clientId, String username, String rpId) {
        GetCredentialRequest.Builder builder = new GetCredentialRequest.Builder();
        
        // Agregar opción de Google
        if (clientId != null) {
            GetGoogleIdOption googleOption = new GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(clientId)
                    .build();
            builder.addCredentialOption(googleOption);
        }
        
        // Agregar opción de Passkey
        if (rpId != null) {
            String requestJson = createPasskeyRequestJson(rpId);
            builder.addCredentialOption(new androidx.credentials.GetPublicKeyCredentialOption(requestJson));
        }
        
        // Agregar opción de contraseña
        if (username != null) {
            builder.addCredentialOption(new androidx.credentials.GetPasswordOption());
        }
        
        return builder.build();
    }
    
    /**
     * Ejemplo completo de autenticación con múltiples opciones
     */
    public void signInWithMultipleOptions(String clientId, String username, String rpId, UniversalCredentialCallback callback) {
        // Crear request con múltiples opciones usando la API moderna
        GetCredentialRequest request = createCredentialRequest(clientId, username, rpId);
        
        getCredential(request, callback);
    }
    
    /**
     * Método de utilidad para extraer información específica de PublicKeyCredential
     */
    public static class PasskeyData {
        public final String responseJson;
        
        public PasskeyData(String responseJson) {
            this.responseJson = responseJson;
        }
        
        public String getResponseJson() {
            return responseJson;
        }
    }
    
    /**
     * Extrae los datos específicos de una PublicKeyCredential
     */
    public PasskeyData extractPasskeyData(PublicKeyCredential publicKeyCredential) {
        try {
            // API moderna: usar getAuthenticationResponseJson()
            String responseJson = publicKeyCredential.getAuthenticationResponseJson();
            Log.d(TAG, "Authentication Response JSON: " + responseJson);
            return new PasskeyData(responseJson);
        } catch (Exception e) {
            Log.e(TAG, "Error extrayendo datos de Passkey", e);
            return null;
        }
    }
}