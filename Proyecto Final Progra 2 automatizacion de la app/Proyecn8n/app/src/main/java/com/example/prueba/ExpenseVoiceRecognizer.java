package com.example.prueba;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase para reconocimiento de voz y extracción de datos de gastos
 * Usa el SpeechRecognizer de Android para convertir voz a texto
 * y luego procesa el texto para extraer monto, comercio y categoría
 */
public class ExpenseVoiceRecognizer {
    
    private static final String TAG = "ExpenseVoiceRecognizer";
    
    private Context context;
    private SpeechRecognizer speechRecognizer;
    private ExpenseVoiceCallback callback;
    private boolean isListening = false;
    
    public interface ExpenseVoiceCallback {
        void onListeningStarted();
        void onSpeechDetected(String partialText);
        void onExpenseRecognized(ExpenseData expenseData);
        void onError(String error);
        void onNoSpeechDetected();
    }
    
    public static class ExpenseData {
        public String amount;
        public String merchant;
        public String category;
        public String originalText;
        
        public ExpenseData(String amount, String merchant, String category, String originalText) {
            this.amount = amount;
            this.merchant = merchant;
            this.category = category;
            this.originalText = originalText;
        }
        
        public boolean isValid() {
            return amount != null && !amount.isEmpty();
        }
    }
    
    public ExpenseVoiceRecognizer(Context context, ExpenseVoiceCallback callback) {
        this.context = context;
        this.callback = callback;
        initializeSpeechRecognizer();
    }
    
    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Log.d(TAG, "Ready for speech");
                    isListening = true;
                    if (callback != null) {
                        callback.onListeningStarted();
                    }
                }
                
                @Override
                public void onBeginningOfSpeech() {
                    Log.d(TAG, "Beginning of speech");
                }
                
                @Override
                public void onRmsChanged(float rmsdB) {
                    // Audio level changed
                }
                
                @Override
                public void onBufferReceived(byte[] buffer) {
                    // Audio buffer received
                }
                
                @Override
                public void onEndOfSpeech() {
                    Log.d(TAG, "End of speech");
                    isListening = false;
                }
                
                @Override
                public void onError(int error) {
                    Log.e(TAG, "Speech recognition error: " + error);
                    isListening = false;
                    
                    String errorMessage = getErrorMessage(error);
                    if (callback != null) {
                        if (error == SpeechRecognizer.ERROR_NO_MATCH || error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
                            callback.onNoSpeechDetected();
                        } else {
                            callback.onError(errorMessage);
                        }
                    }
                }
                
                @Override
                public void onResults(Bundle results) {
                    Log.d(TAG, "Speech results received");
                    isListening = false;
                    
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        String spokenText = matches.get(0);
                        Log.d(TAG, "Recognized text: " + spokenText);
                        
                        // Procesar el texto y extraer datos del gasto
                        ExpenseData expenseData = parseExpenseFromText(spokenText);
                        
                        if (callback != null) {
                            callback.onExpenseRecognized(expenseData);
                        }
                    }
                }
                
                @Override
                public void onPartialResults(Bundle partialResults) {
                    ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty() && callback != null) {
                        callback.onSpeechDetected(matches.get(0));
                    }
                }
                
                @Override
                public void onEvent(int eventType, Bundle params) {
                    // Event callback
                }
            });
        } else {
            Log.e(TAG, "Speech recognition not available");
            if (callback != null) {
                callback.onError("Reconocimiento de voz no disponible en este dispositivo");
            }
        }
    }
    
    public void startListening() {
        if (speechRecognizer == null) {
            if (callback != null) {
                callback.onError("Reconocimiento de voz no inicializado");
            }
            return;
        }
        
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es-ES");
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "es-ES");
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 2000);
        
        try {
            speechRecognizer.startListening(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error starting speech recognition", e);
            if (callback != null) {
                callback.onError("Error al iniciar reconocimiento: " + e.getMessage());
            }
        }
    }
    
    public void stopListening() {
        if (speechRecognizer != null && isListening) {
            speechRecognizer.stopListening();
            isListening = false;
        }
    }
    
    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
    }
    
    public boolean isListening() {
        return isListening;
    }
    
    private String getErrorMessage(int error) {
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                return "Error de audio";
            case SpeechRecognizer.ERROR_CLIENT:
                return "Error del cliente";
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                return "Permisos insuficientes";
            case SpeechRecognizer.ERROR_NETWORK:
                return "Error de red";
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                return "Tiempo de espera de red agotado";
            case SpeechRecognizer.ERROR_NO_MATCH:
                return "No se detectó ninguna coincidencia";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                return "Reconocedor ocupado";
            case SpeechRecognizer.ERROR_SERVER:
                return "Error del servidor";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                return "No se detectó voz";
            default:
                return "Error desconocido";
        }
    }
    
    /**
     * Procesa el texto capturado y extrae los datos del gasto
     * Soporta múltiples formatos:
     * - "Gasté 50 quetzales en Pollo Campero"
     * - "150 en gasolina"
     * - "Compré comida por 75 quetzales"
     * - "50 quetzales de transporte"
     */
    private ExpenseData parseExpenseFromText(String text) {
        String originalText = text;
        text = text.toLowerCase();
        
        String amount = null;
        String merchant = "";
        String category = "";
        
        // 1. Extraer el monto
        // Buscar patrones: "50 quetzales", "Q50", "50", "cincuenta"
        Pattern amountPattern = Pattern.compile("(\\d+\\.?\\d*)\\s*(quetzales?|quetzal|q)?");
        Matcher amountMatcher = amountPattern.matcher(text);
        
        if (amountMatcher.find()) {
            amount = "Q " + amountMatcher.group(1);
        }
        
        // 2. Identificar el comercio/lugar
        // Buscar después de "en", "de", "para"
        String[] merchantKeywords = {"en ", "de ", "para ", "en el ", "en la ", "del ", "de la "};
        for (String keyword : merchantKeywords) {
            int index = text.indexOf(keyword);
            if (index != -1) {
                String afterKeyword = text.substring(index + keyword.length());
                // Tomar las siguientes 2-4 palabras como comercio
                String[] words = afterKeyword.split("\\s+");
                StringBuilder merchantBuilder = new StringBuilder();
                for (int i = 0; i < Math.min(3, words.length); i++) {
                    if (words[i].matches("\\d+.*")) break; // Detener si encuentra números
                    merchantBuilder.append(words[i]).append(" ");
                }
                merchant = capitalize(merchantBuilder.toString().trim());
                if (!merchant.isEmpty()) {
                    break;
                }
            }
        }
        
        // 3. Identificar categoría por palabras clave
        category = identifyCategory(text);
        
        // Si no se encontró comercio, intentar extraer sustantivos
        if (merchant.isEmpty()) {
            String[] commonPlaces = {
                "super", "mercado", "gasolinera", "farmacia", "restaurante", 
                "pollo campero", "campero", "mcdonalds", "burger king", 
                "pizza", "tienda", "cafetería", "café", "bar"
            };
            
            for (String place : commonPlaces) {
                if (text.contains(place)) {
                    merchant = capitalize(place);
                    break;
                }
            }
        }
        
        // Si aún no hay comercio, usar la categoría o "Comercio desconocido"
        if (merchant.isEmpty()) {
            merchant = category.isEmpty() ? "Comercio desconocido" : category;
        }
        
        return new ExpenseData(amount, merchant, category, originalText);
    }
    
    /**
     * Identifica la categoría del gasto basado en palabras clave
     */
    private String identifyCategory(String text) {
        // Alimentación
        if (text.contains("comida") || text.contains("almuerzo") || text.contains("cena") || 
            text.contains("desayuno") || text.contains("restaurante") || text.contains("pollo") ||
            text.contains("pizza") || text.contains("hamburguesa") || text.contains("super")) {
            return "Alimentación";
        }
        
        // Transporte
        if (text.contains("gasolina") || text.contains("combustible") || text.contains("taxi") || 
            text.contains("uber") || text.contains("bus") || text.contains("transporte") ||
            text.contains("pasaje")) {
            return "Transporte";
        }
        
        // Entretenimiento
        if (text.contains("cine") || text.contains("película") || text.contains("juego") || 
            text.contains("diversión") || text.contains("bar") || text.contains("discoteca")) {
            return "Entretenimiento";
        }
        
        // Salud
        if (text.contains("farmacia") || text.contains("medicina") || text.contains("doctor") || 
            text.contains("hospital") || text.contains("salud")) {
            return "Salud";
        }
        
        // Servicios
        if (text.contains("luz") || text.contains("agua") || text.contains("internet") || 
            text.contains("teléfono") || text.contains("cable")) {
            return "Servicios";
        }
        
        // Educación
        if (text.contains("libro") || text.contains("escuela") || text.contains("universidad") || 
            text.contains("curso") || text.contains("estudio")) {
            return "Educación";
        }
        
        return "Otros";
    }
    
    /**
     * Capitaliza la primera letra de cada palabra
     */
    private String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        String[] words = text.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase())
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }
}

