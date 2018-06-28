package com.gcd.calculator.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.validation.constraints.NotNull;

public interface GCDCalculator {
    ObjectNode calculate(@NotNull final ObjectNode task);
}
