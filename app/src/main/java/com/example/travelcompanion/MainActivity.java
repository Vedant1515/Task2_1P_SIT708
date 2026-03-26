package com.example.travelcompanion;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnCurrency, btnFuel, btnTemperature, btnConvert;
    private Spinner spinnerFrom, spinnerTo;
    private TextInputEditText etInputValue;
    private TextInputLayout inputLayout;
    private TextView tvResult, tvCategoryLabel;

    private String currentCategory = "CURRENCY";

    private final List<String> currencyUnits = Arrays.asList(
            "USD", "AUD", "EUR", "JPY", "GBP"
    );

    private final List<String> fuelUnits = Arrays.asList(
            "mpg", "km/L", "L/100km",
            "Gallon(US)", "Litre",
            "Mile", "Kilometre", "Nautical Mile"
    );

    private final List<String> tempUnits = Arrays.asList(
            "Celsius (°C)", "Fahrenheit (°F)", "Kelvin (K)"
    );

    private double getToUSD(String currency) {
        switch (currency) {
            case "USD": return 1.0;
            case "AUD": return 1.0 / 1.55;
            case "EUR": return 1.0 / 0.92;
            case "JPY": return 1.0 / 148.50;
            case "GBP": return 1.0 / 0.78;
            default:    return 1.0;
        }
    }

    private double getFromUSD(String currency) {
        switch (currency) {
            case "USD": return 1.0;
            case "AUD": return 1.55;
            case "EUR": return 0.92;
            case "JPY": return 148.50;
            case "GBP": return 0.78;
            default:    return 1.0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupCategoryButtons();
        setupConvertButton();
        selectCategory("CURRENCY");
    }

    private void initViews() {
        btnCurrency     = findViewById(R.id.btnCurrency);
        btnFuel         = findViewById(R.id.btnFuel);
        btnTemperature  = findViewById(R.id.btnTemperature);
        btnConvert      = findViewById(R.id.btnConvert);
        spinnerFrom     = findViewById(R.id.spinnerFrom);
        spinnerTo       = findViewById(R.id.spinnerTo);
        etInputValue    = findViewById(R.id.etInputValue);
        inputLayout     = findViewById(R.id.inputLayout);
        tvResult        = findViewById(R.id.tvResult);
        tvCategoryLabel = findViewById(R.id.tvCategoryLabel);
    }

    private void setupCategoryButtons() {
        btnCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory("CURRENCY");
            }
        });

        btnFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory("FUEL");
            }
        });

        btnTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory("TEMPERATURE");
            }
        });
    }

    private void selectCategory(String category) {
        currentCategory = category;

        btnCurrency.setBackgroundTintList(getColorStateList(R.color.colorInactive));
        btnFuel.setBackgroundTintList(getColorStateList(R.color.colorInactive));
        btnTemperature.setBackgroundTintList(getColorStateList(R.color.colorInactive));

        tvResult.setText("—");
        etInputValue.setText("");
        inputLayout.setErrorEnabled(false);
        inputLayout.setError(null);

        switch (category) {
            case "CURRENCY":
                btnCurrency.setBackgroundTintList(getColorStateList(R.color.colorActive));
                tvCategoryLabel.setText("Currency Conversion");
                tvCategoryLabel.setTextColor(0xFF212121);
                setupSpinner(spinnerFrom, currencyUnits, 0);
                setupSpinner(spinnerTo, currencyUnits, 1);
                break;

            case "FUEL":
                btnFuel.setBackgroundTintList(getColorStateList(R.color.colorActive));
                tvCategoryLabel.setText("Fuel & Distance Conversion");
                tvCategoryLabel.setTextColor(0xFF212121);
                setupSpinner(spinnerFrom, fuelUnits, 0);
                setupSpinner(spinnerTo, fuelUnits, 1);
                break;

            case "TEMPERATURE":
                btnTemperature.setBackgroundTintList(getColorStateList(R.color.colorActive));
                tvCategoryLabel.setText("Temperature Conversion");
                tvCategoryLabel.setTextColor(0xFF212121);
                setupSpinner(spinnerFrom, tempUnits, 0);
                setupSpinner(spinnerTo, tempUnits, 1);
                break;
        }
    }

    private void setupSpinner(Spinner spinner, List<String> items, int defaultIndex) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(defaultIndex);
    }

    private void setupConvertButton() {
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performConversion();
            }
        });
    }

    private void performConversion() {
        String inputText = "";
        if (etInputValue.getText() != null) {
            inputText = etInputValue.getText().toString().trim();
        }

        // Validation 1: Empty input
        if (inputText.isEmpty()) {
            inputLayout.setErrorEnabled(true);
            inputLayout.setError("Please enter a value to convert");
            tvResult.setText("No input provided");
            Toast.makeText(this, "Please enter a value!", Toast.LENGTH_LONG).show();
            return;
        }

        // Validation 2: Non-numeric input
        double inputValue;
        try {
            inputValue = Double.parseDouble(inputText);
        } catch (NumberFormatException e) {
            inputLayout.setErrorEnabled(true);
            inputLayout.setError("Only numbers are allowed (e.g. 100, 3.5)");
            tvResult.setText("Invalid input");
            Toast.makeText(this, "\"" + inputText + "\" is not a valid number!", Toast.LENGTH_LONG).show();
            return;
        }

        inputLayout.setErrorEnabled(false);
        inputLayout.setError(null);

        String fromUnit = spinnerFrom.getSelectedItem().toString();
        String toUnit   = spinnerTo.getSelectedItem().toString();

        // Validation 3: Identity conversion (same unit)
        if (fromUnit.equals(toUnit)) {
            tvResult.setText(inputValue + " " + toUnit
                    + "\n(Same unit — no conversion needed)");
            Toast.makeText(this, "Same unit selected — no conversion needed!", Toast.LENGTH_LONG).show();
            return;
        }

        // Validation 4: Negative fuel/distance values
        if (currentCategory.equals("FUEL") && inputValue < 0) {
            inputLayout.setErrorEnabled(true);
            inputLayout.setError("Fuel/distance values cannot be negative");
            tvResult.setText("Invalid input");
            Toast.makeText(this, "Fuel/distance values cannot be negative!", Toast.LENGTH_LONG).show();
            return;
        }

        // Validation 5: Negative Kelvin
        if (currentCategory.equals("TEMPERATURE")
                && fromUnit.equals("Kelvin (K)") && inputValue < 0) {
            inputLayout.setErrorEnabled(true);
            inputLayout.setError("Kelvin cannot be below absolute zero (0 K)");
            tvResult.setText("Invalid input");
            Toast.makeText(this, "Kelvin cannot be negative!", Toast.LENGTH_LONG).show();
            return;
        }

        Double result = null;

        switch (currentCategory) {
            case "CURRENCY":
                result = convertCurrency(fromUnit, toUnit, inputValue);
                break;
            case "FUEL":
                result = convertFuel(fromUnit, toUnit, inputValue);
                break;
            case "TEMPERATURE":
                result = convertTemperature(fromUnit, toUnit, inputValue);
                break;
        }

        if (result == null) {
            tvResult.setText("Conversion not supported between these units");
            Toast.makeText(this, "This conversion is not supported!", Toast.LENGTH_LONG).show();
        } else {
            String formattedResult = String.format("%.4f", result);
            tvResult.setText(inputValue + " " + fromUnit
                    + "\n= " + formattedResult + " " + toUnit);
        }
    }

    private Double convertCurrency(String from, String to, double value) {
        double toUSD   = getToUSD(from);
        double fromUSD = getFromUSD(to);
        return value * toUSD * fromUSD;
    }

    private Double convertFuel(String from, String to, double value) {
        List<String> efficiencyUnits = Arrays.asList("mpg", "km/L", "L/100km");
        List<String> volumeUnits     = Arrays.asList("Gallon(US)", "Litre");
        List<String> distanceUnits   = Arrays.asList("Mile", "Kilometre", "Nautical Mile");

        if (efficiencyUnits.contains(from) && efficiencyUnits.contains(to)) {
            return convertEfficiency(from, to, value);
        } else if (volumeUnits.contains(from) && volumeUnits.contains(to)) {
            return convertVolume(from, to, value);
        } else if (distanceUnits.contains(from) && distanceUnits.contains(to)) {
            return convertDistance(from, to, value);
        } else {
            return null;
        }
    }

    private double convertEfficiency(String from, String to, double value) {
        double kmL;
        switch (from) {
            case "mpg":     kmL = value * 0.425; break;
            case "km/L":    kmL = value; break;
            case "L/100km": kmL = (value != 0) ? 100.0 / value : 0; break;
            default:        kmL = value; break;
        }
        switch (to) {
            case "mpg":     return kmL / 0.425;
            case "km/L":    return kmL;
            case "L/100km": return (kmL != 0) ? 100.0 / kmL : 0;
            default:        return kmL;
        }
    }

    private double convertVolume(String from, String to, double value) {
        double litres;
        switch (from) {
            case "Gallon(US)": litres = value * 3.785; break;
            case "Litre":      litres = value; break;
            default:           litres = value; break;
        }
        switch (to) {
            case "Gallon(US)": return litres / 3.785;
            case "Litre":      return litres;
            default:           return litres;
        }
    }

    private double convertDistance(String from, String to, double value) {
        double km;
        switch (from) {
            case "Mile":          km = value * 1.60934; break;
            case "Kilometre":     km = value; break;
            case "Nautical Mile": km = value * 1.852; break;
            default:              km = value; break;
        }
        switch (to) {
            case "Mile":          return km / 1.60934;
            case "Kilometre":     return km;
            case "Nautical Mile": return km / 1.852;
            default:              return km;
        }
    }

    private Double convertTemperature(String from, String to, double value) {
        double celsius;
        switch (from) {
            case "Celsius (°C)":    celsius = value; break;
            case "Fahrenheit (°F)": celsius = (value - 32) / 1.8; break;
            case "Kelvin (K)":      celsius = value - 273.15; break;
            default: return null;
        }
        switch (to) {
            case "Celsius (°C)":    return celsius;
            case "Fahrenheit (°F)": return (celsius * 1.8) + 32;
            case "Kelvin (K)":      return celsius + 273.15;
            default: return null;
        }
    }
}