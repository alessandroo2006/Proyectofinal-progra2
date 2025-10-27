package com.example.prueba;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class VoiceInputBottomSheet extends BottomSheetDialogFragment {
    
    public enum VoiceState {
        LISTENING,
        PROCESSING,
        CONFIRMATION
    }
    
    private VoiceState currentState = VoiceState.LISTENING;
    private TextView txtStatus;
    private TextView txtAmount;
    private TextView txtMerchant;
    private TextView txtCategory;
    private Button btnConfirm;
    private Button btnCancel;
    private Button btnStopRecording;
    
    private ExpenseVoiceRecognizer voiceRecognizer;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    
    private VoiceInputCallback callback;
    
    public interface VoiceInputCallback {
        void onVoiceExpenseConfirmed(String amount, String merchant, String category);
        void onVoiceInputCancelled();
    }
    
    public VoiceInputBottomSheet() {}
    
    public static VoiceInputBottomSheet newInstance() {
        return new VoiceInputBottomSheet();
    }
    
    public void setCallback(VoiceInputCallback callback) {
        this.callback = callback;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_voice_input, container, false);
        
        initializeViews(view);
        setupClickListeners();
        initializeVoiceRecorder();
        updateUI();
        
        return view;
    }
    
    private void initializeViews(View view) {
        txtStatus = view.findViewById(R.id.txt_voice_status);
        txtAmount = view.findViewById(R.id.txt_voice_amount);
        txtMerchant = view.findViewById(R.id.txt_voice_merchant);
        txtCategory = view.findViewById(R.id.txt_voice_category);
        btnConfirm = view.findViewById(R.id.btn_confirm_voice_expense);
        btnCancel = view.findViewById(R.id.btn_cancel_voice_expense);
        btnStopRecording = view.findViewById(R.id.btn_stop_recording);
    }
    
    private void setupClickListeners() {
        btnConfirm.setOnClickListener(v -> {
            if (callback != null) {
                String amount = txtAmount.getText().toString();
                String merchant = txtMerchant.getText().toString();
                String category = txtCategory.getText().toString();
                callback.onVoiceExpenseConfirmed(amount, merchant, category);
            }
            dismiss();
        });
        
        btnCancel.setOnClickListener(v -> {
            if (callback != null) {
                callback.onVoiceInputCancelled();
            }
            dismiss();
        });
        
        btnStopRecording.setOnClickListener(v -> {
            if (voiceRecognizer != null && voiceRecognizer.isListening()) {
                voiceRecognizer.stopListening();
                setState(VoiceState.PROCESSING);
                txtStatus.setText("Procesando...");
            }
        });
    }
    
    private void initializeVoiceRecorder() {
        if (getContext() == null) {
            Log.e("VoiceInputBottomSheet", "Context is null, cannot initialize voice recognizer");
            return;
        }
        
        try {
            voiceRecognizer = new ExpenseVoiceRecognizer(getContext(), new ExpenseVoiceRecognizer.ExpenseVoiceCallback() {
                @Override
                public void onListeningStarted() {
                    if (getActivity() != null && isAdded()) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                setState(VoiceState.LISTENING);
                                if (txtStatus != null) {
                                    txtStatus.setText("🎤 Escuchando... Di tu gasto");
                                }
                            } catch (Exception e) {
                                Log.e("VoiceInputBottomSheet", "Error in onListeningStarted", e);
                            }
                        });
                    }
                }
                
                @Override
                public void onSpeechDetected(String partialText) {
                    if (getActivity() != null && isAdded()) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                if (txtStatus != null && partialText != null) {
                                    txtStatus.setText("🎤 Escuchando: \"" + partialText + "\"");
                                }
                            } catch (Exception e) {
                                Log.e("VoiceInputBottomSheet", "Error in onSpeechDetected", e);
                            }
                        });
                    }
                }
                
                @Override
                public void onExpenseRecognized(ExpenseVoiceRecognizer.ExpenseData expenseData) {
                    if (getActivity() != null && isAdded()) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                if (expenseData != null && expenseData.isValid()) {
                                    setExpenseData(
                                        expenseData.amount != null ? expenseData.amount : "Q 0.00",
                                        expenseData.merchant != null ? expenseData.merchant : "Desconocido",
                                        expenseData.category != null ? expenseData.category : "Otros"
                                    );
                                    if (getContext() != null) {
                                        Toast.makeText(getContext(), "✅ Gasto reconocido", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (getContext() != null) {
                                        Toast.makeText(getContext(), 
                                            "⚠️ No pude entender el monto. Intenta de nuevo.", 
                                            Toast.LENGTH_SHORT).show();
                                    }
                                    dismiss();
                                }
                            } catch (Exception e) {
                                Log.e("VoiceInputBottomSheet", "Error in onExpenseRecognized", e);
                                try { dismiss(); } catch (Exception ex) { }
                            }
                        });
                    }
                }
                
                @Override
                public void onError(String error) {
                    if (getActivity() != null && isAdded()) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                if (getContext() != null) {
                                    Toast.makeText(getContext(), "❌ Error: " + error, Toast.LENGTH_SHORT).show();
                                }
                                dismiss();
                            } catch (Exception e) {
                                Log.e("VoiceInputBottomSheet", "Error in onError", e);
                            }
                        });
                    }
                }
                
                @Override
                public void onNoSpeechDetected() {
                    if (getActivity() != null && isAdded()) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                if (getContext() != null) {
                                    Toast.makeText(getContext(), 
                                        "⚠️ No detecté ninguna voz. Intenta de nuevo.", 
                                        Toast.LENGTH_SHORT).show();
                                }
                                dismiss();
                            } catch (Exception e) {
                                Log.e("VoiceInputBottomSheet", "Error in onNoSpeechDetected", e);
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            Log.e("VoiceInputBottomSheet", "Error initializing voice recognizer", e);
            if (getContext() != null) {
                Toast.makeText(getContext(), "Error al inicializar reconocimiento de voz", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void requestAudioPermission() {
        try {
            if (getContext() != null && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) 
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {
            Log.e("VoiceInputBottomSheet", "Error requesting audio permission", e);
            if (getContext() != null) {
                Toast.makeText(getContext(), "Error al solicitar permiso de micrófono", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start recording
                startVoiceRecording();
            } else {
                // Permission denied
                Toast.makeText(getContext(), "Permiso de micrófono denegado", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }
    
    private void startVoiceRecording() {
        try {
            if (voiceRecognizer != null && getContext() != null) {
                voiceRecognizer.startListening();
            } else {
                Log.e("VoiceInputBottomSheet", "Cannot start recording: recognizer or context is null");
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error al iniciar grabación", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        } catch (Exception e) {
            Log.e("VoiceInputBottomSheet", "Error starting voice recording", e);
            if (getContext() != null) {
                Toast.makeText(getContext(), "Error al iniciar reconocimiento", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        }
    }
    
    private void updateUI() {
        try {
            if (txtStatus == null || txtAmount == null || txtMerchant == null || 
                txtCategory == null || btnConfirm == null || btnCancel == null || 
                btnStopRecording == null) {
                Log.w("VoiceInputBottomSheet", "Views are null, skipping UI update");
                return;
            }
            
            switch (currentState) {
                case LISTENING:
                    txtStatus.setText("Escuchando...");
                    txtAmount.setVisibility(View.GONE);
                    txtMerchant.setVisibility(View.GONE);
                    txtCategory.setVisibility(View.GONE);
                    btnConfirm.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                    btnStopRecording.setVisibility(View.VISIBLE);
                    break;
                    
                case PROCESSING:
                    txtStatus.setText("Procesando audio...");
                    txtAmount.setVisibility(View.GONE);
                    txtMerchant.setVisibility(View.GONE);
                    txtCategory.setVisibility(View.GONE);
                    btnConfirm.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                    btnStopRecording.setVisibility(View.GONE);
                    break;
                    
                case CONFIRMATION:
                    txtStatus.setText("Esto es lo que entendí:");
                    txtAmount.setVisibility(View.VISIBLE);
                    txtMerchant.setVisibility(View.VISIBLE);
                    txtCategory.setVisibility(View.VISIBLE);
                    btnConfirm.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    btnStopRecording.setVisibility(View.GONE);
                    break;
            }
        } catch (Exception e) {
            Log.e("VoiceInputBottomSheet", "Error updating UI", e);
        }
    }
    
    public void setState(VoiceState state) {
        try {
            this.currentState = state;
            updateUI();
        } catch (Exception e) {
            Log.e("VoiceInputBottomSheet", "Error setting state", e);
        }
    }
    
    public void setExpenseData(String amount, String merchant, String category) {
        try {
            if (txtAmount != null && amount != null) {
                txtAmount.setText(amount);
            }
            if (txtMerchant != null && merchant != null) {
                txtMerchant.setText(merchant);
            }
            if (txtCategory != null && category != null) {
                txtCategory.setText(category);
            }
            setState(VoiceState.CONFIRMATION);
        } catch (Exception e) {
            Log.e("VoiceInputBottomSheet", "Error setting expense data", e);
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        // Check permission and start recording
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) 
                == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecording();
        } else {
            requestAudioPermission();
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (voiceRecognizer != null) {
                voiceRecognizer.destroy();
                voiceRecognizer = null;
            }
        } catch (Exception e) {
            Log.e("VoiceInputBottomSheet", "Error destroying voice recognizer", e);
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cleanup para evitar memory leaks
        txtStatus = null;
        txtAmount = null;
        txtMerchant = null;
        txtCategory = null;
        btnConfirm = null;
        btnCancel = null;
        btnStopRecording = null;
    }
}
