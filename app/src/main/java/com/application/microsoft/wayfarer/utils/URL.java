package com.application.microsoft.wayfarer.utils;

public class URL {
    public static String URLify(String text) {

        if (!text.contains(" ")) {
            return text;
        }

        // Use urlifiedText for building the result text
        StringBuilder urlifiedText  = new StringBuilder();

        // Replace spaces with %20, after trimming leading and trailing spaces
        for (char currentChar : text.trim().toCharArray()) {

            if (currentChar == ' ') {
                urlifiedText.append("%20");
            } else {
                urlifiedText.append(currentChar);
            }
        }

        return urlifiedText.toString();
    }

    public static void main(String[] args) {
        System.out.println(URLify("    my example text    "));
    }
}