package com.example.prueba;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class AnalisisActivity extends AppCompatActivity {

    // Navigation items
    private LinearLayout navInicio;
    private LinearLayout navAnalisis;
    private LinearLayout navSuenos;
    private LinearLayout navCoach;
    private LinearLayout navPresupuesto;
    
    // Tab items
    private LinearLayout tabCategorias;
    private LinearLayout tabHistorial;
    private LinearLayout tabComparacion;
    
    // Analysis data and chart
    private AnalysisDataManager analysisDataManager;
    private PieChartView pieChart;
    private LinearLayout categoriesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analisis);
        
        // Initialize analysis data manager
        analysisDataManager = AnalysisDataManager.getInstance();
        
        // Initialize views
        initializeViews();
        
        // Setup navigation
        setupNavigation();
        
        // Setup tabs
        setupTabs();
        
        // Load analysis data
        loadAnalysisData();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void initializeViews() {
        try {
            navInicio = findViewById(R.id.nav_inicio);
            navAnalisis = findViewById(R.id.nav_analisis);
            navSuenos = findViewById(R.id.nav_suenos);
            navCoach = findViewById(R.id.nav_coach);
            navPresupuesto = findViewById(R.id.nav_presupuesto);
            
            // Tab views
            tabCategorias = findViewById(R.id.tab_categorias);
            tabHistorial = findViewById(R.id.tab_historial);
            tabComparacion = findViewById(R.id.tab_comparacion);
            
            // Chart and container views
            pieChart = findViewById(R.id.pie_chart);
            categoriesContainer = findViewById(R.id.categories_container);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupNavigation() {
        try {
            if (navInicio != null) {
                navInicio.setOnClickListener(v -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
            
            if (navAnalisis != null) {
                navAnalisis.setOnClickListener(v -> {
                    // Already on analysis screen
                    setSelectedNavItem(navAnalisis);
                });
            }
            
            if (navSuenos != null) {
                navSuenos.setOnClickListener(v -> {
                    Intent intent = new Intent(this, MisSueñosActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
            
            if (navCoach != null) {
                navCoach.setOnClickListener(v -> {
                    Intent intent = new Intent(this, CoachActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
            
            if (navPresupuesto != null) {
                navPresupuesto.setOnClickListener(v -> {
                    Intent intent = new Intent(this, PresupuestoActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
            
            // Set initial selection
            if (navAnalisis != null) {
                setSelectedNavItem(navAnalisis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setSelectedNavItem(LinearLayout selectedItem) {
        try {
            // Reset all navigation items
            if (navInicio != null) navInicio.setBackground(null);
            if (navAnalisis != null) navAnalisis.setBackground(null);
            if (navSuenos != null) navSuenos.setBackground(null);
            if (navCoach != null) navCoach.setBackground(null);
            if (navPresupuesto != null) navPresupuesto.setBackground(null);
            
            // Set selected item background
            if (selectedItem != null) {
                selectedItem.setBackgroundResource(R.drawable.nav_item_selected_background);
                updateNavTextColors(selectedItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateNavTextColors(LinearLayout selectedItem) {
        try {
            // Reset all text colors to default
            if (navInicio != null && navInicio.getChildCount() > 1) {
                TextView textInicio = (TextView) navInicio.getChildAt(1);
                textInicio.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            if (navAnalisis != null && navAnalisis.getChildCount() > 1) {
                TextView textAnalisis = (TextView) navAnalisis.getChildAt(1);
                textAnalisis.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            if (navSuenos != null && navSuenos.getChildCount() > 1) {
                TextView textSuenos = (TextView) navSuenos.getChildAt(1);
                textSuenos.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            if (navCoach != null && navCoach.getChildCount() > 1) {
                TextView textCoach = (TextView) navCoach.getChildAt(1);
                textCoach.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            if (navPresupuesto != null && navPresupuesto.getChildCount() > 1) {
                TextView textPresupuesto = (TextView) navPresupuesto.getChildAt(1);
                textPresupuesto.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            
            // Set selected text color
            if (selectedItem != null && selectedItem.getChildCount() > 1) {
                TextView selectedText = (TextView) selectedItem.getChildAt(1);
                selectedText.setTextColor(getResources().getColor(R.color.primary_blue));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupTabs() {
        try {
            if (tabCategorias != null) {
                tabCategorias.setOnClickListener(v -> {
                    setSelectedTab(tabCategorias);
                });
            }
            
            if (tabHistorial != null) {
                tabHistorial.setOnClickListener(v -> {
                    setSelectedTab(tabHistorial);
                });
            }
            
            if (tabComparacion != null) {
                tabComparacion.setOnClickListener(v -> {
                    setSelectedTab(tabComparacion);
                });
            }
            
            // Set initial selection
            if (tabCategorias != null) {
                setSelectedTab(tabCategorias);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setSelectedTab(LinearLayout selectedTab) {
        try {
            // Reset all tabs
            if (tabCategorias != null) {
                tabCategorias.setBackground(null);
                TextView textCategorias = (TextView) tabCategorias.getChildAt(0);
                textCategorias.setTextColor(getResources().getColor(android.R.color.darker_gray));
                textCategorias.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14);
                // Remove underline
                if (tabCategorias.getChildCount() > 1) {
                    tabCategorias.removeViewAt(1);
                }
            }
            if (tabHistorial != null) {
                tabHistorial.setBackground(null);
                TextView textHistorial = (TextView) tabHistorial.getChildAt(0);
                textHistorial.setTextColor(getResources().getColor(android.R.color.darker_gray));
                textHistorial.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14);
                // Remove underline
                if (tabHistorial.getChildCount() > 1) {
                    tabHistorial.removeViewAt(1);
                }
            }
            if (tabComparacion != null) {
                tabComparacion.setBackground(null);
                TextView textComparacion = (TextView) tabComparacion.getChildAt(0);
                textComparacion.setTextColor(getResources().getColor(android.R.color.darker_gray));
                textComparacion.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14);
                // Remove underline
                if (tabComparacion.getChildCount() > 1) {
                    tabComparacion.removeViewAt(1);
                }
            }
            
            // Set selected tab
            if (selectedTab != null) {
                selectedTab.setBackgroundResource(R.drawable.tab_selected_background);
                TextView selectedText = (TextView) selectedTab.getChildAt(0);
                selectedText.setTextColor(getResources().getColor(android.R.color.black));
                selectedText.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14);
                selectedText.setTypeface(null, android.graphics.Typeface.BOLD);
                
                // Add underline
                View underline = new View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 2);
                params.topMargin = 4;
                underline.setLayoutParams(params);
                underline.setBackgroundColor(getResources().getColor(R.color.primary_blue));
                selectedTab.addView(underline);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadAnalysisData() {
        try {
            // Update pie chart
            updatePieChart();
            
            // Load categories list
            loadCategoriesList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updatePieChart() {
        try {
            if (pieChart != null) {
                float[] percentages = analysisDataManager.getPercentages();
                int[] colors = analysisDataManager.getColors();
                pieChart.setData(percentages, colors);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadCategoriesList() {
        try {
            if (categoriesContainer != null) {
                categoriesContainer.removeAllViews();
                
                List<AnalysisDataManager.CategoryData> categories = analysisDataManager.getCategories();
                for (int i = 0; i < categories.size(); i++) {
                    AnalysisDataManager.CategoryData category = categories.get(i);
                    LinearLayout categoryItem = createCategoryItem(category, i);
                    categoriesContainer.addView(categoryItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private LinearLayout createCategoryItem(AnalysisDataManager.CategoryData category, int index) {
        try {
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
            itemLayout.setPadding(60, 40, 60, 40);
            
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            itemParams.bottomMargin = 16;
            itemLayout.setLayoutParams(itemParams);
            
            // Color dot
            View colorDot = new View(this);
            colorDot.setBackgroundColor(category.getColor());
            LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(24, 24);
            dotParams.rightMargin = 24;
            colorDot.setLayoutParams(dotParams);
            itemLayout.addView(colorDot);
            
            // Category name
            TextView nameView = new TextView(this);
            nameView.setText(category.getName());
            nameView.setTextColor(getResources().getColor(android.R.color.black));
            nameView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 16);
            LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            );
            nameView.setLayoutParams(nameParams);
            itemLayout.addView(nameView);
            
            // Amount
            TextView amountView = new TextView(this);
            amountView.setText(String.format("Q %,.2f", category.getAmount()));
            amountView.setTextColor(getResources().getColor(android.R.color.black));
            amountView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 16);
            LinearLayout.LayoutParams amountParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            amountParams.rightMargin = 24;
            amountView.setLayoutParams(amountParams);
            itemLayout.addView(amountView);
            
            // Percentage
            TextView percentageView = new TextView(this);
            percentageView.setText(String.format("%d%%", category.getPercentage()));
            percentageView.setTextColor(getResources().getColor(android.R.color.darker_gray));
            percentageView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 16);
            itemLayout.addView(percentageView);
            
            // Make the item clickable for editing
            itemLayout.setClickable(true);
            itemLayout.setFocusable(true);
            itemLayout.setBackgroundResource(R.drawable.category_item_background);
            itemLayout.setOnClickListener(v -> showEditCategoryDialog(category, index));
            
            return itemLayout;
        } catch (Exception e) {
            e.printStackTrace();
            return new LinearLayout(this);
        }
    }
    
    private void showEditCategoryDialog(AnalysisDataManager.CategoryData category, int index) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_category, null);
            builder.setView(dialogView);
            
            AlertDialog dialog = builder.create();
            
            // Get views
            Button closeButton = dialogView.findViewById(R.id.btn_close_edit_dialog);
            View colorDot = dialogView.findViewById(R.id.category_color_dot);
            TextView categoryName = dialogView.findViewById(R.id.txt_category_name);
            TextView currentAmount = dialogView.findViewById(R.id.txt_current_amount);
            EditText newAmount = dialogView.findViewById(R.id.et_new_amount);
            TextView newPercentage = dialogView.findViewById(R.id.txt_new_percentage);
            Button cancelButton = dialogView.findViewById(R.id.btn_cancel_edit);
            Button saveButton = dialogView.findViewById(R.id.btn_save_edit);
            
            // Setup dialog content
            colorDot.setBackgroundColor(category.getColor());
            categoryName.setText(category.getName());
            currentAmount.setText(String.format("Q %,.2f", category.getAmount()));
            newAmount.setText(String.valueOf(category.getAmount()));
            newPercentage.setText(String.format("%d%%", category.getPercentage()));
            
            // Setup text watcher for real-time percentage update
            newAmount.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        if (!s.toString().isEmpty()) {
                            double newAmountValue = Double.parseDouble(s.toString());
                            double total = analysisDataManager.getTotalAmount() - category.getAmount() + newAmountValue;
                            if (total > 0) {
                                int newPercentageValue = (int) Math.round((newAmountValue / total) * 100);
                                newPercentage.setText(String.format("%d%%", newPercentageValue));
                            }
                        }
                    } catch (NumberFormatException e) {
                        newPercentage.setText("0%");
                    }
                }
                
                @Override
                public void afterTextChanged(android.text.Editable s) {}
            });
            
            // Setup close button
            closeButton.setOnClickListener(v -> dialog.dismiss());
            
            // Setup cancel button
            cancelButton.setOnClickListener(v -> dialog.dismiss());
            
            // Setup save button
            saveButton.setOnClickListener(v -> {
                try {
                    String newAmountStr = newAmount.getText().toString().trim();
                    if (newAmountStr.isEmpty()) {
                        Toast.makeText(this, "Por favor, ingresa un monto válido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    double newAmountValue = Double.parseDouble(newAmountStr);
                    if (newAmountValue < 0) {
                        Toast.makeText(this, "El monto no puede ser negativo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    // Update the category amount
                    analysisDataManager.updateCategoryAmount(index, newAmountValue);
                    
                    // Refresh the display
                    loadAnalysisData();
                    
                    dialog.dismiss();
                    Toast.makeText(this, "Categoría actualizada correctamente", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Por favor, ingresa un monto válido", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error al actualizar la categoría", Toast.LENGTH_SHORT).show();
                }
            });
            
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
