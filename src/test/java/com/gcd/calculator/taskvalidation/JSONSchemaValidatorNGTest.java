package com.gcd.calculator.taskvalidation;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Iterator;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SpringBootTest
public class JSONSchemaValidatorNGTest extends AbstractTestNGSpringContextTests {
    
    @Autowired
    JSONSchemaValidator taskValidator;
    
    @BeforeClass
    public void beforeTests() {
        System.out.println(JSONSchemaValidatorNGTest.class.toString() + " starting");
    }
    
    @DataProvider(name = "jsonTasksValidation")
    public Iterator<Object[]> setupJSONTasks() {
        ArrayList<Object[]> cases = new ArrayList<>();
        String def_1_1 = "Valid JSON. Case 1. Valid JSON-object";
        ObjectNode json_1_1 = new ObjectNode(JsonNodeFactory.instance);
        json_1_1.put("id", 1)
                .put("first", 18)
                .put("second", 2)
                .put("status", "NOT_COMPLETED")
                .put("result", 0)
                .put("error", "");
        ObjectNode expected_1_1 = json_1_1;
        cases.add(new Object[]{def_1_1, json_1_1, expected_1_1});
        return cases.iterator();        
    }

    /**
     * Тест метод validateTask, класса JSONSchemaValidator.
     * @param defenition
     * @param input
     * @param expResult
     */
    @Test(dataProvider = "jsonTasksValidation")
    public void testValidateTask(String defenition, ObjectNode input, ObjectNode expResult) {
        System.out.println(defenition);
        ObjectNode result = taskValidator.validateTask(input);
        assertEquals(result, expResult, "Wrong validation");
        
    }
    
    @DataProvider(name = "invalidJsonTasks")
    public Iterator<Object[]> setupInvalidJsonTasks() {
        ArrayList<Object[]> cases = new ArrayList<>();
        
        String def_1_1 = "Invalid JSON. Case 1. \"id\" field is missed";
        ObjectNode json_1_1 = new ObjectNode(JsonNodeFactory.instance);
        json_1_1.put("first", 18)
                .put("second", 2)
                .put("status", "NOT_COMPLETED")
                .put("result", 0)
                .put("error", "");
        cases.add(new Object[]{def_1_1, json_1_1});
        
        String def_1_2 = "Invalid JSON. Case 2. \"first\" field is missed";
        ObjectNode json_1_2 = new ObjectNode(JsonNodeFactory.instance);
        json_1_2.put("id", 18)
                .put("second", 2)
                .put("status", "NOT_COMPLETED")
                .put("result", 0)
                .put("error", "");
        cases.add(new Object[]{def_1_2, json_1_2});
        
        String def_1_3 = "Invalid JSON. Case 3. \"second\" field is missed";
        ObjectNode json_1_3 = new ObjectNode(JsonNodeFactory.instance);
        json_1_3.put("id", 18)
                .put("first", 18)
                .put("status", "NOT_COMPLETED")
                .put("result", 0)
                .put("error", "");
        cases.add(new Object[]{def_1_3, json_1_3});
        
        return cases.iterator();        
    }
    
    /**
     * Test cases for invalid json messages, wich must be rejected and don't requeue 
     * @param defenition
     * @param input 
     */
    @Test(expectedExceptions = AmqpRejectAndDontRequeueException.class,
           dataProvider = "invalidJsonTasks")
    public void testOnInvalidJsonMessage(String defenition, ObjectNode input) {
        System.out.println(defenition);
        ObjectNode result = taskValidator.validateTask(input);
    }
}
