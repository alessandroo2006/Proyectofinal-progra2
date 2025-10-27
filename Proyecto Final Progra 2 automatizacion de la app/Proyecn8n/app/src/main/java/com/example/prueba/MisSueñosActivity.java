package com.example.prueba;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MisSueñosActivity extends AppCompatActivity {

    // Navigation items - no existen en el layout actual
    // private LinearLayout navInicio;
    // private LinearLayout navAnalisis;
    // private LinearLayout navSuenos;
    // private LinearLayout navCoach;
    // private LinearLayout navPresupuesto;

    // Main views
    private TextView txtTotalSaved;
    private ProgressBar progressBarTotal;
    private ProgressBar progressBarCasaNueva;
    private ProgressBar progressBarFondoEmergencia;
    private TextView txtCasaNuevaTime;
    private TextView txtCasaNuevaMonthly;
    private TextView txtFondoEmergenciaTime;
    private TextView txtFondoEmergenciaMonthly;
    private LinearLayout dreamsContainer;
    // private ScrollView scrollView; // No se usa en el layout actual

    // Dream manager and data
    private DreamManager dreamManager;
    private List<Dream> dreams;
    private DreamData casaNueva;
    private DreamData fondoEmergencia;
    private ImageView loadingAnimationBottom;
    private double totalSaved = 26300;
    private double totalTarget = 70000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_suenos);
        
        initializeViews();
        initializeDreamData();
        setupNavigation();
        setupClickListeners();
        loadDreamsData();
        updateAllDisplays();
    }

    private void initializeViews() {
        // Navigation views - estos elementos no existen en el layout actual
        // navInicio = findViewById(R.id.nav_inicio);
        // navAnalisis = findViewById(R.id.nav_analisis);
        // navSuenos = findViewById(R.id.nav_suenos);
        // navCoach = findViewById(R.id.nav_coach);
        // navPresupuesto = findViewById(R.id.nav_presupuesto);

        // Main views
        txtTotalSaved = findViewById(R.id.txt_total_saved);
        progressBarTotal = findViewById(R.id.progress_bar_total);
        progressBarCasaNueva = findViewById(R.id.progress_bar_casa_nueva);
        progressBarFondoEmergencia = findViewById(R.id.progress_bar_fondo_emergencia);
        
        // Initialize loading animation
        loadingAnimationBottom = findViewById(R.id.loading_animation_bottom);
        txtCasaNuevaTime = findViewById(R.id.txt_casa_nueva_time);
        txtCasaNuevaMonthly = findViewById(R.id.txt_casa_nueva_monthly);
        txtFondoEmergenciaTime = findViewById(R.id.txt_fondo_emergencia_time);
        txtFondoEmergenciaMonthly = findViewById(R.id.txt_fondo_emergencia_monthly);
        dreamsContainer = findViewById(R.id.dreams_container);
        // scrollView no tiene ID en el layout actual

        // Initialize dream manager
        dreamManager = DreamManager.getInstance();
        dreams = dreamManager.getDreams();
    }

    private void initializeDreamData() {
        // Casa Nueva: Q 15,000 / Q 50,000 (30%)
        casaNueva = new DreamData(
            "Casa Nueva",
            15000,
            50000,
            3 // meses restantes
        );

        // Fondo de Emergencia: Q 8,500 / Q 15,000 (57%)
        fondoEmergencia = new DreamData(
            "Fondo de Emergencia",
            8500,
            15000,
            1 // mes restante
        );
    }

    private void setupNavigation() {
        // Navigation no implementada en este layout
        // Los elementos de navegación no existen en activity_mis_suenos.xml
        // La navegación se maneja desde otras activities
    }

    private void setupClickListeners() {
        // Add Dream Button
        ImageView btnAddDream = findViewById(R.id.btn_add_dream);
        btnAddDream.setOnClickListener(v -> showAddDreamDialog());

        // Casa Nueva buttons
        Button btnAdjustCasaNueva = findViewById(R.id.btn_adjust_casa_nueva);
        Button btnAddFundsCasaNueva = findViewById(R.id.btn_add_funds_casa_nueva);
        
        btnAdjustCasaNueva.setOnClickListener(v -> showAdjustDialog(casaNueva, "Casa Nueva"));
        btnAddFundsCasaNueva.setOnClickListener(v -> showAddFundsDialogForDreamData(casaNueva, "Casa Nueva"));

        // Fondo de Emergencia buttons
        Button btnAdjustFondoEmergencia = findViewById(R.id.btn_adjust_fondo_emergencia);
        Button btnAddFundsFondoEmergencia = findViewById(R.id.btn_add_funds_fondo_emergencia);
        
        btnAdjustFondoEmergencia.setOnClickListener(v -> showAdjustDialog(fondoEmergencia, "Fondo de Emergencia"));
        btnAddFundsFondoEmergencia.setOnClickListener(v -> showAddFundsDialogForDreamData(fondoEmergencia, "Fondo de Emergencia"));
    }

    private void loadDreamsData() {
        // Clear existing dynamic dreams
        dreamsContainer.removeAllViews();
        
        // Add dynamic dreams from DreamManager
        for (int i = 0; i < dreams.size(); i++) {
            Dream dream = dreams.get(i);
            View dreamCard = createDreamCard(dream);
            dreamsContainer.addView(dreamCard);
            
            // Add staggered animation
            dreamCard.setAlpha(0f);
            dreamCard.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .setStartDelay(i * 100)
                    .start();
        }
    }

    private View createDreamCard(Dream dream) {
        try {
            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.HORIZONTAL);
            cardLayout.setBackgroundResource(R.drawable.rounded_card_background);
            cardLayout.setPadding(40, 40, 40, 40);

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(32, 0, 32, 32);
            cardLayout.setLayoutParams(cardParams);

            // Icon section
            LinearLayout iconLayout = new LinearLayout(this);
            iconLayout.setOrientation(LinearLayout.VERTICAL);
            iconLayout.setGravity(android.view.Gravity.CENTER);
            LinearLayout.LayoutParams iconLayoutParams = new LinearLayout.LayoutParams(120, 120);
            iconLayoutParams.rightMargin = 32;
            iconLayout.setLayoutParams(iconLayoutParams);

            // Set icon based on category
            String iconText = getIconForCategory(dream.getCategory());
            TextView iconView = new TextView(this);
            iconView.setText(iconText);
            iconView.setTextSize(32);
            iconView.setGravity(android.view.Gravity.CENTER);
            iconView.setBackgroundResource(getBackgroundForCategory(dream.getCategory()));
            iconView.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
            iconLayout.addView(iconView);

            // Content section
            LinearLayout contentLayout = new LinearLayout(this);
            contentLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            );
            contentLayout.setLayoutParams(contentParams);

            // Title and priority
            LinearLayout titleLayout = new LinearLayout(this);
            titleLayout.setOrientation(LinearLayout.HORIZONTAL);
            titleLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);

            TextView titleView = new TextView(this);
            titleView.setText(dream.getName());
            titleView.setTextColor(getResources().getColor(android.R.color.black));
            titleView.setTextSize(36);
            titleView.setTypeface(null, android.graphics.Typeface.BOLD);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            titleParams.rightMargin = 16;
            titleView.setLayoutParams(titleParams);
            titleLayout.addView(titleView);

            // Priority tag
            TextView priorityTag = new TextView(this);
            priorityTag.setText(dream.getPriority().toLowerCase());
            priorityTag.setTextColor(getResources().getColor(android.R.color.white));
            priorityTag.setTextSize(20);
            priorityTag.setPadding(8, 4, 8, 4);
            priorityTag.setBackgroundResource(R.drawable.priority_tag_background);
            titleLayout.addView(priorityTag);

            LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            titleLayoutParams.bottomMargin = 16;
            titleLayout.setLayoutParams(titleLayoutParams);
            contentLayout.addView(titleLayout);

            // Amount and percentage
            LinearLayout amountLayout = new LinearLayout(this);
            amountLayout.setOrientation(LinearLayout.HORIZONTAL);
            amountLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);

            TextView amountView = new TextView(this);
            amountView.setText(String.format("Q %,.0f / Q %,.0f", dream.getCurrentAmount(), dream.getTargetAmount()));
            amountView.setTextColor(getResources().getColor(android.R.color.black));
            amountView.setTextSize(28);
            LinearLayout.LayoutParams amountParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            );
            amountView.setLayoutParams(amountParams);
            amountLayout.addView(amountView);

            TextView percentageView = new TextView(this);
            percentageView.setText(String.format("%.0f%%", dream.getProgressPercentage()));
            percentageView.setTextColor(getResources().getColor(R.color.primary_blue));
            percentageView.setTextSize(28);
            percentageView.setTypeface(null, android.graphics.Typeface.BOLD);
            amountLayout.addView(percentageView);

            LinearLayout.LayoutParams amountLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            amountLayoutParams.bottomMargin = 16;
            amountLayout.setLayoutParams(amountLayoutParams);
            contentLayout.addView(amountLayout);

            // Progress bar
            ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setMax(100);
            progressBar.setProgress((int) dream.getProgressPercentage());
            progressBar.getProgressDrawable().setColorFilter(
                getResources().getColor(R.color.primary_blue),
                android.graphics.PorterDuff.Mode.SRC_IN
            );
            LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                6
            );
            progressParams.bottomMargin = 24;
            progressBar.setLayoutParams(progressParams);
            contentLayout.addView(progressBar);

            // Timeline and monthly amount
            LinearLayout timelineLayout = new LinearLayout(this);
            timelineLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout timeLayout = new LinearLayout(this);
            timeLayout.setOrientation(LinearLayout.HORIZONTAL);
            timeLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            );
            timeLayout.setLayoutParams(timeParams);

            TextView timeIcon = new TextView(this);
            timeIcon.setText("⏰");
            timeIcon.setTextSize(28);
            timeIcon.setPadding(0, 0, 8, 0);
            timeLayout.addView(timeIcon);

            TextView timeView = new TextView(this);
            long daysRemaining = dream.getDaysRemaining();
            if (daysRemaining > 30) {
                timeView.setText(String.format("%d meses restantes", daysRemaining / 30));
            } else if (daysRemaining > 0) {
                timeView.setText(String.format("%d días restantes", daysRemaining));
            } else {
                timeView.setText("0 meses restantes");
            }
            timeView.setTextColor(getResources().getColor(android.R.color.darker_gray));
            timeView.setTextSize(24);
            timeLayout.addView(timeView);

            LinearLayout monthlyLayout = new LinearLayout(this);
            monthlyLayout.setOrientation(LinearLayout.HORIZONTAL);
            monthlyLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams monthlyParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            );
            monthlyLayout.setLayoutParams(monthlyParams);

            TextView monthlyIcon = new TextView(this);
            monthlyIcon.setText("🎯");
            monthlyIcon.setTextSize(28);
            monthlyIcon.setPadding(0, 0, 8, 0);
            monthlyLayout.addView(monthlyIcon);

            TextView monthlyView = new TextView(this);
            double monthlyAmount = dream.getRemainingAmount() / Math.max(daysRemaining / 30.0, 1);
            monthlyView.setText(String.format("Q %,.0f/mes", monthlyAmount));
            monthlyView.setTextColor(getResources().getColor(android.R.color.darker_gray));
            monthlyView.setTextSize(24);
            monthlyLayout.addView(monthlyView);

            timelineLayout.addView(timeLayout);
            timelineLayout.addView(monthlyLayout);

            LinearLayout.LayoutParams timelineParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            timelineParams.bottomMargin = 32;
            timelineLayout.setLayoutParams(timelineParams);
            contentLayout.addView(timelineLayout);

            // Action buttons
            LinearLayout buttonsLayout = new LinearLayout(this);
            buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);

            Button adjustButton = new Button(this);
            adjustButton.setText("Ajustar");
            adjustButton.setTextColor(getResources().getColor(android.R.color.white));
            adjustButton.setTextSize(24);
            adjustButton.setBackgroundResource(R.drawable.adjust_button_background);
            LinearLayout.LayoutParams adjustParams = new LinearLayout.LayoutParams(
                0, 72, 1f
            );
            adjustParams.rightMargin = 16;
            adjustButton.setLayoutParams(adjustParams);
            adjustButton.setOnClickListener(v -> showAdjustDreamDialog(dream));
            buttonsLayout.addView(adjustButton);

            Button addFundsButton = new Button(this);
            addFundsButton.setText("Agregar");
            addFundsButton.setTextColor(getResources().getColor(android.R.color.white));
            addFundsButton.setTextSize(24);
            addFundsButton.setBackgroundResource(R.drawable.add_funds_button_background);
            LinearLayout.LayoutParams addParams = new LinearLayout.LayoutParams(
                0, 72, 1f
            );
            addFundsButton.setLayoutParams(addParams);
            addFundsButton.setOnClickListener(v -> showAddFundsDialog(dream));
            buttonsLayout.addView(addFundsButton);

            contentLayout.addView(buttonsLayout);

            // Add layouts to card
            cardLayout.addView(iconLayout);
            cardLayout.addView(contentLayout);

            return cardLayout;
        } catch (Exception e) {
            e.printStackTrace();
            return new LinearLayout(this);
        }
    }

    private String getIconForCategory(String category) {
        switch (category.toLowerCase()) {
            case "casa": return "🏠";
            case "auto": return "🚗";
            case "vacaciones": return "✈️";
            case "emergencia": return "💼";
            default: return "🎯";
        }
    }

    private int getBackgroundForCategory(String category) {
        switch (category.toLowerCase()) {
            case "casa": return R.drawable.icon_background_blue;
            case "auto": return R.drawable.icon_background_green;
            case "vacaciones": return R.drawable.icon_background_blue;
            case "emergencia": return R.drawable.icon_background_green;
            default: return R.drawable.icon_background_blue;
        }
    }

    private void showAddDreamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_new_dream, null);
        
        builder.setView(dialogView);
        
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        
        // Get views
        Button closeButton = dialogView.findViewById(R.id.btn_close_dialog);
        EditText etDreamName = dialogView.findViewById(R.id.et_dream_name);
        EditText etTargetAmount = dialogView.findViewById(R.id.et_target_amount);
        EditText etDeadline = dialogView.findViewById(R.id.et_deadline);
        Button btnCalendar = dialogView.findViewById(R.id.btn_calendar);
        Spinner spinnerPriority = dialogView.findViewById(R.id.spinner_priority);
        Button btnCreateDream = dialogView.findViewById(R.id.btn_create_dream);
        
        // Setup spinner
        String[] priorities = {"Alta", "Media", "Baja"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);
        
        // Calendar button
        btnCalendar.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                etDeadline.setText(sdf.format(selectedDate.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        
        // Close button
        closeButton.setOnClickListener(v -> dialog.dismiss());
        
        // Create dream button
        btnCreateDream.setOnClickListener(v -> {
            String name = etDreamName.getText().toString().trim();
            String targetAmountText = etTargetAmount.getText().toString().trim();
            String deadlineText = etDeadline.getText().toString().trim();
            String priority = priorities[spinnerPriority.getSelectedItemPosition()];
            
            // Validation
            if (name.length() < 3) {
                Toast.makeText(this, "El nombre debe tener al menos 3 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (targetAmountText.isEmpty()) {
                Toast.makeText(this, "Ingresa un monto objetivo", Toast.LENGTH_SHORT).show();
                return;
            }
            
            try {
                double targetAmount = Double.parseDouble(targetAmountText);
                if (targetAmount <= 0) {
                    Toast.makeText(this, "El monto objetivo debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (deadlineText.isEmpty()) {
                    Toast.makeText(this, "Selecciona una fecha límite", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Parse deadline
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date deadline = sdf.parse(deadlineText);
                
                if (deadline.before(new Date())) {
                    Toast.makeText(this, "La fecha límite debe ser futura", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Create new dream
                Dream newDream = new Dream(name, targetAmount, deadline, priority);
                dreamManager.addDream(newDream);
                
                Toast.makeText(this, "Sueño creado exitosamente", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                
                // Reload dreams data
                dreams = dreamManager.getDreams();
                loadDreamsData();
                updateAllDisplays();
                
            } catch (Exception e) {
                Toast.makeText(this, "Error al crear el sueño", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
        
        dialog.show();
    }

    private void showAdjustDreamDialog(Dream dream) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_adjust_dream, null);
        
        builder.setView(dialogView);
        
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        
        // Get views
        Button closeButton = dialogView.findViewById(R.id.btn_close_dialog);
        Button cancelButton = dialogView.findViewById(R.id.btn_cancel);
        Button saveButton = dialogView.findViewById(R.id.btn_save_changes);
        EditText etDreamName = dialogView.findViewById(R.id.et_dream_name);
        EditText etTargetAmount = dialogView.findViewById(R.id.et_target_amount);
        EditText etDeadline = dialogView.findViewById(R.id.et_deadline);
        Button btnCalendar = dialogView.findViewById(R.id.btn_calendar);
        Spinner spinnerPriority = dialogView.findViewById(R.id.spinner_priority);
        
        // Populate current values
        etDreamName.setText(dream.getName());
        etTargetAmount.setText(String.format("%.2f", dream.getTargetAmount()));
        etDeadline.setText(formatDate(dream.getDeadline()));
        
        // Setup priority spinner
        String[] priorities = {"Alta", "Media", "Baja"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);
        
        // Set current priority
        for (int i = 0; i < priorities.length; i++) {
            if (priorities[i].equals(dream.getPriority())) {
                spinnerPriority.setSelection(i);
                break;
            }
        }
        
        // Calendar button
        btnCalendar.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                etDeadline.setText(sdf.format(selectedDate.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        
        // Button listeners
        closeButton.setOnClickListener(v -> dialog.dismiss());
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        
        saveButton.setOnClickListener(v -> {
            String name = etDreamName.getText().toString().trim();
            String targetAmountText = etTargetAmount.getText().toString().trim();
            String deadlineText = etDeadline.getText().toString().trim();
            String priority = priorities[spinnerPriority.getSelectedItemPosition()];
            
            // Validation
            if (name.length() < 3) {
                Toast.makeText(this, "El nombre debe tener al menos 3 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (targetAmountText.isEmpty()) {
                Toast.makeText(this, "Ingresa un monto objetivo", Toast.LENGTH_SHORT).show();
                return;
            }
            
            try {
                double targetAmount = Double.parseDouble(targetAmountText);
                if (targetAmount <= 0) {
                    Toast.makeText(this, "El monto objetivo debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (deadlineText.isEmpty()) {
                    Toast.makeText(this, "Selecciona una fecha límite", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Parse deadline
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date deadline = sdf.parse(deadlineText);
                
                if (deadline.before(new Date())) {
                    Toast.makeText(this, "La fecha límite debe ser futura", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Update dream
                dream.setName(name);
                dream.setTargetAmount(targetAmount);
                dream.setDeadline(deadline);
                dream.setPriority(priority);
                
                dreamManager.updateDream(dream);
                
                Toast.makeText(this, "Sueño actualizado exitosamente", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                
                // Reload dreams data
                dreams = dreamManager.getDreams();
                loadDreamsData();
                updateAllDisplays();
                
            } catch (Exception e) {
                Toast.makeText(this, "Error al actualizar el sueño", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
        
        dialog.show();
    }

    private void showAddFundsDialogForDreamData(DreamData dream, String dreamName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_funds, null);
        
        builder.setView(dialogView);
        
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        
        // Get views
        EditText etAmountToAdd = dialogView.findViewById(R.id.et_amount_to_add);
        TextView txtDreamName = dialogView.findViewById(R.id.txt_dream_name);
        TextView txtCurrentProgress = dialogView.findViewById(R.id.txt_current_progress);
        
        // Quick amount buttons
        Button btnQuick50 = dialogView.findViewById(R.id.btn_quick_50);
        Button btnQuick100 = dialogView.findViewById(R.id.btn_quick_100);
        Button btnQuick200 = dialogView.findViewById(R.id.btn_quick_200);
        Button btnAddAmount = dialogView.findViewById(R.id.btn_add_amount);
        
        // Populate dream info
        txtDreamName.setText(dreamName);
        double percentage = (dream.currentAmount / dream.targetAmount) * 100;
        txtCurrentProgress.setText(String.format("Q %,.0f / Q %,.0f (%.0f%%)", 
            dream.currentAmount, dream.targetAmount, percentage));
        
        // Setup quick amount buttons
        btnQuick50.setOnClickListener(v -> etAmountToAdd.setText("50"));
        btnQuick100.setOnClickListener(v -> etAmountToAdd.setText("100"));
        btnQuick200.setOnClickListener(v -> etAmountToAdd.setText("200"));
        
        // Input validation
        etAmountToAdd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etAmountToAdd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        
        // Setup add button
        btnAddAmount.setOnClickListener(v -> {
            String amountText = etAmountToAdd.getText().toString().trim();
            if (!amountText.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountText);
                    if (amount > 0) {
                        addFundsToDreamData(dream, amount);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Ingresa una cantidad válida", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Ingresa una cantidad", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Remove default buttons since we have our own
        builder.setNegativeButton("", null);
        
        dialog.show();
    }

    private void showAddFundsDialog(Dream dream) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_funds, null);
        
        builder.setView(dialogView);
        
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        
        // Get views
        EditText etAmountToAdd = dialogView.findViewById(R.id.et_amount_to_add);
        TextView txtDreamName = dialogView.findViewById(R.id.txt_dream_name);
        TextView txtCurrentProgress = dialogView.findViewById(R.id.txt_current_progress);
        
        // Quick amount buttons
        Button btnQuick50 = dialogView.findViewById(R.id.btn_quick_50);
        Button btnQuick100 = dialogView.findViewById(R.id.btn_quick_100);
        Button btnQuick200 = dialogView.findViewById(R.id.btn_quick_200);
        Button btnAddAmount = dialogView.findViewById(R.id.btn_add_amount);
        
        // Populate dream info
        txtDreamName.setText(dream.getName());
        txtCurrentProgress.setText(String.format("Q %,.0f / Q %,.0f (%.0f%%)", 
            dream.getCurrentAmount(), dream.getTargetAmount(), dream.getProgressPercentage()));
        
        // Setup quick amount buttons
        btnQuick50.setOnClickListener(v -> etAmountToAdd.setText("50"));
        btnQuick100.setOnClickListener(v -> etAmountToAdd.setText("100"));
        btnQuick200.setOnClickListener(v -> etAmountToAdd.setText("200"));
        
        // Input validation
        etAmountToAdd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etAmountToAdd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        
        // Setup add button
        btnAddAmount.setOnClickListener(v -> {
            String amountText = etAmountToAdd.getText().toString().trim();
            if (!amountText.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountText);
                    if (amount > 0) {
                        addFundsToDream(dream, amount);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Ingresa una cantidad válida", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Ingresa una cantidad", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Remove default buttons since we have our own
        builder.setNegativeButton("", null);
        
        dialog.show();
    }

    private void addFundsToDreamData(DreamData dream, double amount) {
        // Show loading animation
        showLoadingAnimation();
        
        // Simulate processing time (1.5 seconds)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Add funds
            dream.currentAmount += amount;
            totalSaved += amount;
            
            // Update displays with animation
            updateAllDisplays();
            
            // Hide loading animation
            hideLoadingAnimation();
            
            // Show success message
            NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
            String formattedAmount = formatter.format(amount);
            Toast.makeText(this, "¡Q " + formattedAmount + " agregados exitosamente!", Toast.LENGTH_SHORT).show();
            
            // Check if dream is completed
            if (dream.currentAmount >= dream.targetAmount) {
                Toast.makeText(this, "¡Felicitaciones! Has completado tu sueño: " + dream.name, Toast.LENGTH_LONG).show();
            }
        }, 1500);
    }

    private void addFundsToDream(Dream dream, double amount) {
        // Add funds
        dream.setCurrentAmount(dream.getCurrentAmount() + amount);
        dreamManager.updateDream(dream);
        
        // Update displays
        updateAllDisplays();
        loadDreamsData();
        
        // Show success message
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        String formattedAmount = formatter.format(amount);
        Toast.makeText(this, "¡Q " + formattedAmount + " agregados exitosamente!", Toast.LENGTH_SHORT).show();
        
        // Check if dream is completed
        if (dream.getCurrentAmount() >= dream.getTargetAmount()) {
            Toast.makeText(this, "¡Felicitaciones! Has completado tu sueño: " + dream.getName(), Toast.LENGTH_LONG).show();
        }
    }

    private void showAdjustDialog(DreamData dream, String dreamName) {
        String[] options = {
            "Cambiar nombre",
            "Modificar objetivo",
            "Cambiar prioridad",
            "Eliminar meta"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajustar " + dreamName)
               .setItems(options, (dialog, which) -> {
                   switch (which) {
                       case 0:
                           showEditNameDialog(dream);
                           break;
                       case 1:
                           showEditTargetDialog(dream);
                           break;
                       case 2:
                           showEditPriorityDialog(dream);
                           break;
                       case 3:
                           showDeleteConfirmationDialog(dream, dreamName);
                           break;
                   }
               })
               .show();
    }

    private void showEditNameDialog(DreamData dream) {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(dream.name);
        input.setHint("Nombre del sueño");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar Nombre")
               .setView(input)
               .setPositiveButton("Guardar", (dialog, which) -> {
                   String newName = input.getText().toString().trim();
                   if (!newName.isEmpty()) {
                       dream.name = newName;
                       Toast.makeText(this, "Nombre actualizado", Toast.LENGTH_SHORT).show();
                   } else {
                       Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                   }
               })
               .setNegativeButton("Cancelar", null)
               .show();
    }

    private void showEditTargetDialog(DreamData dream) {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(String.valueOf((int) dream.targetAmount));
        input.setHint("Monto objetivo");
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar Objetivo")
               .setView(input)
               .setPositiveButton("Guardar", (dialog, which) -> {
                   try {
                       double newTarget = Double.parseDouble(input.getText().toString());
                       if (newTarget > 0) {
                           dream.targetAmount = newTarget;
                           updateTotalTarget();
                           updateAllDisplays();
                           Toast.makeText(this, "Objetivo actualizado", Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(this, "El objetivo debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                       }
                   } catch (NumberFormatException e) {
                       Toast.makeText(this, "Ingresa un monto válido", Toast.LENGTH_SHORT).show();
                   }
               })
               .setNegativeButton("Cancelar", null)
               .show();
    }

    private void showEditPriorityDialog(DreamData dream) {
        String[] priorities = {"Alta", "Media", "Baja"};
        int currentPriority = 0; // Default to "Alta"
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar Prioridad")
               .setSingleChoiceItems(priorities, currentPriority, null)
               .setPositiveButton("Guardar", (dialog, which) -> {
                   int selectedPriority = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                   dream.priority = priorities[selectedPriority];
                   Toast.makeText(this, "Prioridad actualizada", Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("Cancelar", null)
               .show();
    }

    private void showDeleteConfirmationDialog(DreamData dream, String dreamName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Meta")
               .setMessage("¿Estás seguro de que deseas eliminar \"" + dreamName + "\"?")
               .setPositiveButton("Eliminar", (dialog, which) -> {
                   // Remove from total calculations
                   totalSaved -= dream.currentAmount;
                   totalTarget -= dream.targetAmount;
                   updateAllDisplays();
                   Toast.makeText(this, "Meta eliminada", Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("Cancelar", null)
               .show();
    }

    private void updateTotalTarget() {
        totalTarget = casaNueva.targetAmount + fondoEmergencia.targetAmount;
    }

    private void updateAllDisplays() {
        updateGlobalSummary();
        updateDreamDetails(casaNueva, progressBarCasaNueva, txtCasaNuevaTime, txtCasaNuevaMonthly);
        updateDreamDetails(fondoEmergencia, progressBarFondoEmergencia, txtFondoEmergenciaTime, txtFondoEmergenciaMonthly);
    }

    private void updateGlobalSummary() {
        // Update total saved amount
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        txtTotalSaved.setText("Q " + formatter.format(totalSaved));
        
        // Animate global progress bar
        double globalPercentage = (totalSaved / totalTarget) * 100;
        animateProgressBar(progressBarTotal, (int) globalPercentage);
    }

    private void updateDreamDetails(DreamData dream, ProgressBar progressBar, TextView timeText, TextView monthlyText) {
        // Calculate percentage
        double percentage = (dream.currentAmount / dream.targetAmount) * 100;
        
        // Animate progress bar
        animateProgressBar(progressBar, (int) percentage);
        
        // Update time remaining
        timeText.setText(dream.monthsRemaining + (dream.monthsRemaining == 1 ? " mes restante" : " meses restantes"));
        
        // Calculate monthly amount needed
        double remainingAmount = dream.targetAmount - dream.currentAmount;
        double monthlyAmount = remainingAmount / Math.max(dream.monthsRemaining, 1);
        
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        monthlyText.setText("Q " + formatter.format(Math.ceil(monthlyAmount)) + "/mes");
    }

    private void animateProgressBar(ProgressBar progressBar, int targetProgress) {
        ValueAnimator animator = ValueAnimator.ofInt(progressBar.getProgress(), targetProgress);
        animator.setDuration(500);
        animator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            progressBar.setProgress(progress);
        });
        animator.start();
    }

    private void showLoadingAnimation() {
        if (loadingAnimationBottom != null) {
            loadingAnimationBottom.setVisibility(View.VISIBLE);
            loadingAnimationBottom.animate()
                .alpha(1.0f)
                .setDuration(300)
                .start();
        }
    }

    private void hideLoadingAnimation() {
        if (loadingAnimationBottom != null) {
            loadingAnimationBottom.animate()
                .alpha(0.0f)
                .setDuration(300)
                .withEndAction(() -> loadingAnimationBottom.setVisibility(View.GONE))
                .start();
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    // Inner class to hold dream data
    private static class DreamData {
        String name;
        double currentAmount;
        double targetAmount;
        int monthsRemaining;
        String priority;

        DreamData(String name, double currentAmount, double targetAmount, int monthsRemaining) {
            this.name = name;
            this.currentAmount = currentAmount;
            this.targetAmount = targetAmount;
            this.monthsRemaining = monthsRemaining;
            this.priority = "Alta";
        }
    }
}