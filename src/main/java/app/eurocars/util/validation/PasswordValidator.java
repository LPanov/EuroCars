package app.eurocars.util.validation;

import app.eurocars.util.annotations.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private int minLength;
    private int maxLength;
    private boolean required;
    private boolean containsDigit;
    private boolean containsLowerCase;
    private boolean containsUpperCase;
    private boolean containsSpecialSymbol;

    @Override
    public void initialize(Password constraintAnnotation) {
        // Retrieve the values from the @Password annotation
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.required = constraintAnnotation.required();
        this.containsDigit = constraintAnnotation.containsDigit();
        this.containsLowerCase = constraintAnnotation.containsLowerCase();
        this.containsUpperCase = constraintAnnotation.containsUpperCase();
        this.containsSpecialSymbol = constraintAnnotation.containsSpecialSymbol();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        boolean isValid = true; // Assume valid until a rule fails

        // 1. Handle 'required' and null/blank checks first
        if (required && (password == null || password.isBlank())) {
            return false;
        }

        if (!required && (password == null || password.isBlank())) {
            return true;
        }

        if (password.length() < minLength || password.length() > maxLength) {
            isValid = false; // Mark as invalid, but continue checking other rules if desired
        }

        if (containsDigit && !password.matches(".*\\d.*")) {
            isValid = false;
        }

        if (containsLowerCase && !password.matches(".*[a-z].*")) {
            isValid = false;
        }

        if (containsUpperCase && !password.matches(".*[A-Z].*")) {
            isValid = false;
        }

        if (containsSpecialSymbol && !password.matches(".*[!@#$%^&*()_+<>?].*")) {
            isValid = false;
        }

        if (password.matches(".*\\s.*")) { // \\s matches any whitespace character (space, tab, newline, etc.)
            isValid = false;
        }

        return isValid;
    }
}
