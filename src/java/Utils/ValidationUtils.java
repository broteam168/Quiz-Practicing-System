package Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    public boolean isValidEmail(String email) {
        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidInteger(String s, int min, int max) {
        try {
            int value = Integer.parseInt(s);
            return value >= min && value <= max;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNotNull(Object o) {
        return o != null;
    }

    public boolean isValidFullName(String fullname) {
        if (fullname.length() > 100) {
            return false;
        }
        String FULLNAME_REGEX = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$";
        Pattern pattern = Pattern.compile(FULLNAME_REGEX);
        Matcher matcher = pattern.matcher(fullname);
        return matcher.matches();
    }

    public boolean isValidGender(int gender) {
        if (gender != 1 && gender != 0) {
            return false;
        }
        return true;
    }

    public boolean isValidMobile(String mobile) {
        String MOBILE_REGEX = "^(09|03|05|07|08)\\d{8}$";
        Pattern pattern = Pattern.compile(MOBILE_REGEX);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    public boolean isValidPassword(String password) {
        String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\W]{8,}$";
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean isValidSubjectName(String subjectName) {
        //check if the subject name is not null and not empty
        if (subjectName == null || subjectName.isEmpty()) {
            return false;
        }
        //check if the subject has max length 50 chars
        if (subjectName.length() > 50) {
            return false;
        }
        //check if the subject name has at least 1 alphabetic char
        if (!subjectName.matches(".*[a-zA-Z].*")) {
            return false;
        }
        return true;
    }
}
