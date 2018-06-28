package com.gcd.calculator.taskvalidation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

public class JSONSchemaValidator implements Validator {
    private static Logger logger = LoggerFactory.getLogger(JSONSchemaValidator.class);
    private JsonSchema schema;
    
    public JSONSchemaValidator(JsonSchema validationSchema) throws ProcessingException {
        this.schema = validationSchema;
    }  

    public JSONSchemaValidator() {
    }
    
    @Override
    public ObjectNode validateTask(ObjectNode task) {
        ProcessingReport report;
        try {
            report = schema.validate(task);
            if (!report.isSuccess())                
                throw new AmqpRejectAndDontRequeueException("Incorrect incoming message: json object violates validation schema");
        } catch (ProcessingException ex) {
            logger.error(Errors.UNSUPPORTED_JSON_SCHEMA_FORMAT.getMessage(), ex);
            setErrorStateWithMessage(task, Errors.UNSUPPORTED_JSON_SCHEMA_FORMAT.getMessage());
        }
        return task;
    }
    
    private ObjectNode setErrorStateWithMessage(ObjectNode task, String msg) {
        task.replace("status", new TextNode("ERROR"));
        task.replace("error", new TextNode(msg));
        return task;
    }
}
