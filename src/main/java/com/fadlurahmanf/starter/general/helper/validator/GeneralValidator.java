package com.fadlurahmanf.starter.general.helper.validator;

import org.apache.commons.validator.routines.EmailValidator;
public class GeneralValidator {
    public static Boolean isValidEmail(String email){
        try {
            return EmailValidator.getInstance().isValid(email);
        }catch (Exception e){
            return false;
        }
    }
}
