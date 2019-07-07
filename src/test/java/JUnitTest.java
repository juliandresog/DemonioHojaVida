/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author julia
 */
public class JUnitTest {

    public JUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void Regex1() {
        Pattern p = Pattern.compile("[a-zA-Z#\\s\\+]+\\s\\d+");
        Matcher m = p.matcher("C# 5 Sql 3 C++ 5 Visual Basic 4 AWS 4");
        
        System.out.println("Buscando...");

        //System.out.println(":"+m.groupCount());
        while (m.find()) {
            System.out.println(m.group());
        }

        assertTrue(true);
    }
}
