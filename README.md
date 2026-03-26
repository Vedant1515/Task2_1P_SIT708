
# Travel Companion App

An Android application built in Java that helps international travellers 
convert essential values across three categories: Currency, Fuel and Distance, 
and Temperature.

---

## Student Details

- Name: Vedant Vedant Ashishkumar Pandya
- Student ID: s224698427
- Unit: SIT708
- University: Deakin University
- Task: Pass Task 2.1

---

## App Features

- Currency Conversion (USD, AUD, EUR, JPY, GBP)
- Fuel and Distance Conversion (mpg, km/L, L/100km, Gallon, Litre, Mile, Kilometre, Nautical Mile)
- Temperature Conversion (Celsius, Fahrenheit, Kelvin)
- Input validation and error handling
- Identity conversion detection
- Negative value prevention

---

## Conversion Rates Used

### Currency - Fixed 2026 Rates

| From      | To        | Rate    |
|-----------|-----------|---------|
| 1 USD     | AUD       | 1.55    |
| 1 USD     | EUR       | 0.92    |
| 1 USD     | JPY       | 148.50  |
| 1 USD     | GBP       | 0.78    |

### Fuel and Distance

| Conversion            | Factor          |
|-----------------------|-----------------|
| 1 mpg                 | 0.425 km/L      |
| 1 Gallon (US)         | 3.785 Litres    |
| 1 Nautical Mile       | 1.852 km        |
| 1 Mile                | 1.60934 km      |

### Temperature Formulas

| Conversion                  | Formula                  |
|-----------------------------|--------------------------|
| Celsius to Fahrenheit       | F = (C x 1.8) + 32       |
| Fahrenheit to Celsius       | C = (F - 32) / 1.8       |
| Celsius to Kelvin           | K = C + 273.15           |

---

## Validation and Error Handling

- Empty input shows an error message
- Non-numeric input shows an invalid number error
- Same unit selected shows a toast notification
- Negative values for fuel and distance are rejected
- Negative Kelvin values are rejected

---

## Tech Stack

- Language: Java
- IDE: Android Studio
- Minimum SDK: API 24 (Android 7.0)
- Target SDK: API 34
- UI Components: Material Components, CardView, ScrollView, Spinner

---

# Travel-Companion-App
