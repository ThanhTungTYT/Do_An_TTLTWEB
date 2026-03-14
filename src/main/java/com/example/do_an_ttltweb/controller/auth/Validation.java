package com.example.do_an_ttltweb.controller.auth;

public class Validation {

    public static boolean isEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public static boolean rePass(String pass, String rePass) {
        return pass != null && pass.equals(rePass);
    }

    public static boolean passLength(String pass, int length) {
        return pass != null && pass.length() >= length;
    }

    public static boolean containChar(String pass) {
        if (pass == null) return false;

        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : pass.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasUpper && hasDigit && hasSpecial;
    }
}