package com.zaiika.gateway.gateway.validator;

public interface Validator<T> {
    boolean validate(T value);
}
