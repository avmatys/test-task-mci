package com.task.mci.command.impl;

import java.util.Map;
import java.util.function.Predicate;

public class ParamSpec {

    private final String key;
    private final String prompt;
    private final Predicate<Map<String,String>> requiredIf;
    private final Predicate<String> validator;
    private final String errorMessage;

    public ParamSpec(String key,
                     String prompt,
                     Predicate<Map<String,String>> requiredIf,
                     Predicate<String> validator,
                     String errorMessage) {
        this.key          = key;
        this.prompt       = prompt;
        this.requiredIf   = requiredIf;
        this.validator    = validator;
        this.errorMessage = errorMessage;
    }

    public ParamSpec(String key, String prompt, boolean required) {
        this(key, prompt,
             map -> required,
             val -> val != null && !val.isBlank(),
             "Parameter " + key + " must not be blank");
    }

    public String key() {
        return key;
    }

    public String prompt() {
        return prompt;
    }

    public Predicate<Map<String,String>> requiredIf() {
        return requiredIf;
    }

    public Predicate<String> validator() {
        return validator;
    }

    public String errorMessage() {
        return errorMessage;
    }
}
