package com.mapquizzes.testutils;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/*
Spring parses the cookie strings and then reformats them when adding to the response.
Because of that there is a mismatch in data formats.
This class allows to compare actual and expected cookies correctly.
*/
public class CookieMatcher extends TypeSafeMatcher<String> {
    private final String expectedCookie;

    public CookieMatcher(String expectedCookie) {
        this.expectedCookie = expectedCookie;
    }

    @Override
    protected boolean matchesSafely(String actualCookie) {
        String[] expectedParts = expectedCookie.split(";");
        String[] actualParts = actualCookie.split(";");

        if (expectedParts.length != actualParts.length) {
            return false;
        }

        for (int i = 0; i < expectedParts.length; i++) {
            String expectedPart = expectedParts[i].trim();
            String actualPart = actualParts[i].trim();

            if (expectedPart.startsWith("Expires=")) {
                if (actualPart.startsWith("Expires=")) {
                    return DateComparison.areDatesEqual(expectedPart, actualPart);
                } else return false;
            } else if (!expectedPart.equals(actualPart)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("cookie matching ").appendValue(expectedCookie);
    }

    public static CookieMatcher cookieMatching(String expectedCookie) {
        return new CookieMatcher(expectedCookie);
    }

    private static class DateComparison {
        static boolean areDatesEqual(String expectedDate, String actualDate) {
            int startIndex = "Expires=".length();
            expectedDate = expectedDate.substring(startIndex);
            actualDate = actualDate.substring(startIndex);

            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

            int comparison = -1;
            try {
                Date parsedDate1 = sdf1.parse(expectedDate);
                Date parsedDate2 = sdf2.parse(actualDate);

                comparison = parsedDate1.compareTo(parsedDate2);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return comparison == 0;
        }
    }
}
