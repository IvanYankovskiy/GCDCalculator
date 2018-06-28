package com.gcd.calculator.taskvalidation;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Validator {
    ObjectNode validateTask(ObjectNode task);
}
