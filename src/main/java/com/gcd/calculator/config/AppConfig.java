package com.gcd.calculator.config;

import com.gcd.calculator.service.CalculatorImplementation;
import com.gcd.calculator.service.GCDCalculator;
import com.gcd.calculator.taskvalidation.Errors;
import com.gcd.calculator.taskvalidation.JSONSchemaValidator;
import com.gcd.calculator.taskvalidation.Validator;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {
    private final static Logger logger = LoggerFactory.getLogger(AppConfig.class);
    
    @Value("${json.validation.schema}")
    private String validationSchemaName;
    
    @Value("${json.validation.subdirectory}")
    private String schemaResourceSubdirectory;
    
    @Bean
    @Scope
    public GCDCalculator calculator() {
        return new CalculatorImplementation();
    }
    
    @Bean
    public Validator taskValidator() {
        try {
            return new JSONSchemaValidator(
                    validationSchema(schemaResourceSubdirectory, validationSchemaName));
        } catch (ProcessingException ex) {
            logger.error("Json validation schema contains errors. Please? provide correct schema", ex);
            throw new BeanCreationException("Json validation schema contains errors.");
        }
    }
    
    @Bean
    public JsonSchema validationSchema(String schemaResourceSubdirectory, String validationSchemaName) {
        try {
            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            StringBuilder schemaPathBuilder = new StringBuilder("resource:");
            schemaPathBuilder.append(schemaResourceSubdirectory);
            schemaPathBuilder.append(validationSchemaName);
            JsonSchema schema = factory.getJsonSchema(schemaPathBuilder.toString());
            logger.info("Validation json schema loaded from : " + schemaPathBuilder.toString());
            return schema;
        } catch (ProcessingException ex) {
            logger.error(Errors.LOAD_SCHEMA_FAILURE.getMessage(), ex);
            throw new BeanCreationException("Can not load json validation schema.");
        }
    }
}
