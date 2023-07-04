package com.epam.mjc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.epam.mjc.MethodSignature.*;

public class MethodParser {

    /**
     * Parses string that represents a method signature and stores all it's members into a {@link MethodSignature} object.
     * signatureString is a java-like method signature with following parts:
     *      1. access modifier - optional, followed by space: ' '
     *      2. return type - followed by space: ' '
     *      3. method name
     *      4. arguments - surrounded with braces: '()' and separated by commas: ','
     * Each argument consists of argument type and argument name, separated by space: ' '.
     * Examples:
     *      accessModifier returnType methodName(argumentType1 argumentName1, argumentType2 argumentName2)
     *      private void log(String value)
     *      Vector3 distort(int x, int y, int z, float magnitude)
     *      public DateTime getCurrentDateTime()
     *
     * @param signatureString source string to parse
     * @return {@link MethodSignature} object filled with parsed values from source string
     */
    public MethodSignature parseFunction(String signatureString) {

        // make a copy of the original signature
        String signatureStringCopy = new String(signatureString);

        // extract and remove access modifier
        String accessModifier = null;
        Matcher accessModifierMatcher = Pattern.compile("^(private|protected|public)\\s+").matcher(signatureStringCopy);
        if (accessModifierMatcher.find()) {
            accessModifier = accessModifierMatcher.group(1);
            signatureStringCopy = signatureStringCopy.substring(accessModifier.length()).trim();
        }

        // extract and remove arguments area
        String argumentsArea = signatureStringCopy
                .substring(signatureStringCopy.indexOf("(") + 1, signatureStringCopy.length() - 1)
                .replace("[\\(\\)\\s+]", "");

        // parse arguments area
        StringSplitter splitter = new StringSplitter();
        List<String> arguments = splitter.splitByDelimiters(argumentsArea, List.of(",", " "));
        List<Argument> argumentList = IntStream.rangeClosed(0, arguments.size() - 1).filter(i -> i % 2 == 0)
                .mapToObj(i -> {
                    return new MethodSignature.Argument(arguments.get(i), arguments.get(i + 1));
                }).collect(Collectors.toList());

        signatureStringCopy = signatureStringCopy.substring(0, signatureStringCopy.indexOf("("));

        // parse definition area
        List<String> typeAndName = splitter.splitByDelimiters(signatureStringCopy, List.of(" "));

        // compose the method signature object to be returned
        MethodSignature methodSignature = new MethodSignature(typeAndName.get(1), argumentList);
        methodSignature.setReturnType(typeAndName.get(0));
        methodSignature.setAccessModifier(accessModifier);

        return methodSignature;
    }

    public static void main(String[] args) {
        MethodParser methodParser = new MethodParser();
        methodParser.parseFunction("public void parseMethod(String signature, int number)");
    }
}
