/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    public void Regex1() throws IOException {
        Pattern p = Pattern.compile("[a-zA-Z#\\s\\+]+\\s\\d+");
        Matcher m = p.matcher("C# 5 Sql 3 C++ 5 Visual Basic 4 AWS 4");
        
        System.out.println("Buscando...");

        /*FileWriter writer = new FileWriter("C:\\RPA\\hv_plano\\demo.txt", true);
        writer.append("Algo para escribir");
        writer.flush();
        writer.close();
        
        // APPEND MODE SET HERE
        BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\RPA\\hv_plano\\demo2.txt", true));
	 bw.write("400:08311998:Inprise Corporation:249.95");
	 bw.newLine();
         bw.write("400:08311998:Inprise Corporation:249.95");
	 bw.newLine();
	 bw.flush();
         
         bw.close();*/
            
        //System.out.println(":"+m.groupCount());
        while (m.find()) {
            System.out.println(m.group());
        }

        assertTrue(true);
    }
}
