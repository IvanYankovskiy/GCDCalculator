package com.gcd.calculator.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.math.LongMath;
import javax.validation.constraints.NotNull;

public class CalculatorImplementation implements GCDCalculator {
    
    @Override
    public ObjectNode calculate(@NotNull ObjectNode task) throws IllegalArgumentException {
        long first = task.get("first").asLong();
        long second = task.get("second").asLong();
        if (first == 0 || second == 0)
            throw new IllegalArgumentException("first number or second must not be equals 0");
        long result = LongMath.gcd(first, second);
        task.put("result", result)
            .put("status", "COMPLETED")
            .put("error", "");
        return task;
    }
}
