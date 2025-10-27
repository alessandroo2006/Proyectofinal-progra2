package com.example.prueba;

import android.content.Context;
import android.view.View;
import android.view.autofill.AutofillManager;
import androidx.autofill.HintConstants;

public class AutofillHelper {
    
    public static void setupAutofillHints(View view, String hint) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AutofillManager autofillManager = view.getContext().getSystemService(AutofillManager.class);
            if (autofillManager != null && autofillManager.isEnabled()) {
                switch (hint) {
                    case "username":
                        view.setAutofillHints(HintConstants.AUTOFILL_HINT_USERNAME);
                        break;
                    case "password":
                        view.setAutofillHints(HintConstants.AUTOFILL_HINT_PASSWORD);
                        break;
                    case "email":
                        view.setAutofillHints(HintConstants.AUTOFILL_HINT_EMAIL_ADDRESS);
                        break;
                    default:
                        view.setAutofillHints(hint);
                        break;
                }
            }
        }
    }
    
    public static void setupAutofillForLogin(View usernameView, View passwordView) {
        setupAutofillHints(usernameView, "username");
        setupAutofillHints(passwordView, "password");
    }
    
    public static void setupAutofillForRegister(View usernameView, View emailView, View passwordView, View confirmPasswordView) {
        setupAutofillHints(usernameView, "username");
        setupAutofillHints(emailView, "email");
        setupAutofillHints(passwordView, "password");
        setupAutofillHints(confirmPasswordView, "password");
    }
}
