/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcd.calculator.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Iterator;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author Ivan
 */
public class CalculatorImplementationNGTest {
    
    public CalculatorImplementationNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    @DataProvider(name = "calculations")
    public Iterator<Object[]> setupJSONTasks() {
        ArrayList<Object[]> cases = new ArrayList<>();
        
        String def_1_1 = "Case 1. All fields are presented";
        ObjectNode task_1_1 = new ObjectNode(JsonNodeFactory.instance);
        task_1_1.put("id", 1)
                .put("first", 18)
                .put("second", 2)
                .put("status", "NOT_COMPLETED")
                .put("result", 0)
                .put("error", "");
        ObjectNode expected_1_1 = new ObjectNode(JsonNodeFactory.instance);
        expected_1_1.put("id", 1)
                .put("first", 18)
                .put("second", 2)
                .put("status", "COMPLETED")
                .put("result", (long)2)
                .put("error", "");
        cases.add(new Object[]{def_1_1, task_1_1, expected_1_1});
        
        String def_1_2 = "Case 2. Only required fields are presented. It's possible to decrease volume of incoming payload,by sending only required fields in message";
        ObjectNode task_1_2 = new ObjectNode(JsonNodeFactory.instance);
        task_1_2.put("id", 1)
                .put("first", 18)
                .put("second", 2);
        ObjectNode expected_1_2 = new ObjectNode(JsonNodeFactory.instance);
        expected_1_2.put("id", 1)
                .put("first", 18)
                .put("second", 2)
                .put("result", (long)2)
                .put("status", "COMPLETED")
                .put("error", "");
        cases.add(new Object[]{def_1_2, task_1_2, expected_1_2});
        
        String def_1_3 = "Case 3.";
        ObjectNode task_1_3 = new ObjectNode(JsonNodeFactory.instance);
        task_1_3.put("id", 1)
                .put("first", 3)
                .put("second", 2)
                .put("status", "NOT_COMPLETED")
                .put("result", 0)
                .put("error", "");
        ObjectNode expected_1_3 = new ObjectNode(JsonNodeFactory.instance);
        expected_1_3.put("id", 1)
                .put("first", 3)
                .put("second", 2)
                .put("status", "COMPLETED")
                .put("result", (long)1)
                .put("error", "");
        cases.add(new Object[]{def_1_3, task_1_3, expected_1_3});
        return cases.iterator();        
    }
    
    /**
     * Тест метод calculate, класса CalculatorImplementation.
     * 
     * @param description
     * @param task
     * @param expResult
     */
    @Test(dataProvider = "calculations")
    public void testCalculate(String description, ObjectNode task, ObjectNode expResult) {
        System.out.println("Calculate. " + description);
        CalculatorImplementation instance = new CalculatorImplementation();
        ObjectNode result = instance.calculate(task);
        assertEquals(result, expResult);
    }
    
    @DataProvider(name = "calculationsWithExceptions")
    public Iterator<Object[]> setupJSONTasksWithExceptions() {
        ArrayList<Object[]> cases = new ArrayList<>();
        
        String def_1_1 = "Case 1. Numbers should be only positive and non zero values.";
        ObjectNode task_1_1 = new ObjectNode(JsonNodeFactory.instance);
        task_1_1.put("id", 1)
                .put("first", 0)
                .put("second", 2)
                .put("status", "NOT_COMPLETED")
                .put("result", 0)
                .put("error", "");
        cases.add(new Object[]{def_1_1, task_1_1});
        
        String def_1_2 = "Case 2. Numbers mus be positive. Exception from Guava gcd implementation";
        ObjectNode task_1_2 = new ObjectNode(JsonNodeFactory.instance);
        task_1_2.put("id", 1)
                .put("first", -13)
                .put("second", 2);
        cases.add(new Object[]{def_1_2, task_1_2});
        
        String def_1_3 = "Case 3.";
        ObjectNode task_1_3 = new ObjectNode(JsonNodeFactory.instance);
        task_1_3.put("id", 1)
                .put("second", 2)
                .put("status", "NOT_COMPLETED")
                .put("result", 0)
                .put("error", "");
        cases.add(new Object[]{def_1_3, task_1_3});
        return cases.iterator();        
    }
    
    /**
     * Тест метод calculate, класса CalculatorImplementation.
     * 
     * @param description
     * @param task
     */
    @Test(dataProvider = "calculationsWithExceptions",
            expectedExceptions = {IllegalArgumentException.class, NullPointerException.class})
    public void testCalculationsWithExc(String description, ObjectNode task) {
        System.out.println("Calculations with exceptions. " + description);
        CalculatorImplementation instance = new CalculatorImplementation();
        ObjectNode result = instance.calculate(task);
    }
    
}
