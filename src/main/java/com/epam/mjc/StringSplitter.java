package com.epam.mjc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringSplitter {

    /**
     * Splits given string applying all delimeters to it. Keeps order of result substrings as in source string.
     *
     * @param source source string
     * @param delimiters collection of delimiter strings
     * @return List of substrings
     */
    public List<String> splitByDelimiters(String source, Collection<String> delimiters) {
        String regex = "[" + Pattern.quote(String.join("", delimiters)) + "]";
        return List.of(source.split(regex)).stream()
                .filter(item -> !item.trim().equals(""))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        StringSplitter splitter = new StringSplitter();
        splitter.splitByDelimiters("we094utpisjrgokhstowu459wu45-28wfioghe586sdfsdf", List.of("0", "4", "2", "6"));
    }
}
