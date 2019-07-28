/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.joarchitectus.demoniohojasvidatxt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

/**
 *
 * @author julian
 */
public class ProcesadorMain {

    /**
     * Palabras clave
     */
    protected static final String KEY_APPROVED = "Approved";
    protected static final String KEY_REJECTED = "Rejected";
    protected static final String KEY_EMAIL = "Email";
    protected static final String KEY_PHONE = "Phone";
    protected static final String KEY_RESIDENCE = "Residence";
    protected static final String KEY_AGE = "Age";
    protected static final String KEY_EDUCATION = "Education";
    protected static final String KEY_CAREER = "Career";
    protected static final String KEY_UNIVERSITY = "University";
    protected static final String KEY_TOTAL_YEARS_TECEXPERIENCE = "Total numbers of years of tech experience:";
    protected static final String KEY_PROFILE = "Profile:";
    protected static final String KEY_PROFICIENCY_IN = "Proficiency in";
    protected static final String KEY_PROGRAMING = "programming languages";
    protected static final String KEY_ENGLISH_LANGUAGE = "English language";
    protected static final String KEY_ENGLISH_LANGUAGE_EXTRA1 = "Knowledge in the  Verbal  Written";
    protected static final String KEY_ENGLISH_LANGUAGE_EXTRA2 = "Lenguage communication communication";
    protected static final String KEY_SOFT_SKILLS = "Soft skills";
    protected static final String KEY_WORK_EXPERIENCE = "Work Experience";

    private static final Map<String, String> mapHabilidadesBlandas;
    private static final Map<String, String> mapHabilidadesTecnicas;

    //inicializo variables
    static {
        mapHabilidadesBlandas = new LinkedHashMap<>();
        mapHabilidadesTecnicas = new HashMap<>();

        List<String> palabrasClave = new ArrayList<>();
        palabrasClave.add("Creative thinking".toLowerCase());
        palabrasClave.add("Curtomer management".toLowerCase());
        palabrasClave.add("Leadership".toLowerCase());
        palabrasClave.add("Self learning".toLowerCase());
        palabrasClave.add("Project Management".toLowerCase());

        for (String palabra : palabrasClave) {
            mapHabilidadesBlandas.put(palabra, palabra);
        }

//        palabrasClave.clear();
//        palabrasClave.add("Angular".toLowerCase());
//        palabrasClave.add("Sql".toLowerCase());
//        palabrasClave.add("C#".toLowerCase());
//        palabrasClave.add("Visual Basic".toLowerCase());
//        palabrasClave.add("JavaScript".toLowerCase());
//        palabrasClave.add("Node".toLowerCase());
//        palabrasClave.add("C++".toLowerCase());
//        palabrasClave.add("Python".toLowerCase());
//        palabrasClave.add("AWS".toLowerCase());
//        palabrasClave.add("GCP".toLowerCase());
//        palabrasClave.add("RPA".toLowerCase());
//        palabrasClave.add("Tensorflow".toLowerCase());
//
//        for (String palabra : palabrasClave) {
//            mapHabilidadesTecnicas.put(palabra, palabra);
//        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println("Iniciando procesamiento");
            System.out.println("Argumentos: " + args);
            String carpetaPlanos = "C:\\RPA\\hv_plano\\";
            String carpetaSalida = "C:\\RPA\\hv_salida\\";
            //miro si me mandaron el nombre de la carpeta por argumento
            if (args != null && args.length == 1) {
                carpetaPlanos = args[0];
            }

            String csvFile = carpetaSalida + "csv_generado_" + new Date().getTime() + ".csv";
            FileWriter writer = new FileWriter(csvFile);
            CSVUtils.writeLine(writer, Arrays.asList("NombreCandidato", "Email", "Telefono", "Residencia", "Edad", "ExperienciaTI", "Carrera",
                    "Universidad", "Perfil", "HabilidadTecnica", "HabilidadTecnicaValor", "InglesConocimiento", "InglesVerbal", "InglesEscrito",
                    //"HabilidadesBlandas", 
                    "Creative thinking", "Curtomer management", "Leadership", "Self learning", "Project Management",
                    "ExperienciaLaboral", "EstadoCandidato"), ',', '"');

            File folder = new File(carpetaPlanos);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                //for (int i = 0; i < 2; i++) {
                if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith("txt")) {
                    //System.out.println("************************************");
                    //System.out.println(listOfFiles[i].getName());

                    Map<String, Object> persona = procesarDocumento(leerArchivo(listOfFiles[i]));
                    //System.out.println("Persona: " + persona);
                    if (persona != null) {
                        if (persona.containsKey("habilidades_tecnicas")
                                && persona.get("habilidades_tecnicas") != null
                                && !((Map) persona.get("habilidades_tecnicas")).isEmpty()) {
                            //debo repetir los registros segun las habilidades tecnicas del candidato
                            Iterator it = ((Map) persona.get("habilidades_tecnicas")).entrySet().iterator();

                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry) it.next();
                                CSVUtils.writeLine(writer, Arrays.asList(
                                        persona.get("nombre_candidato").toString(),
                                        persona.get("email").toString(),
                                        persona.get("telefono").toString(),
                                        persona.get("residencia").toString(),
                                        persona.get("edad").toString(),
                                        persona.get("experiencia_ti").toString(),
                                        persona.get("carrera").toString(),
                                        persona.get("universidad").toString(),
                                        persona.get("perfil").toString(),
                                        pair.getKey().toString(),
                                        pair.getValue().toString(),
                                        persona.get("ingles_conocimiento").toString(),
                                        persona.get("ingles_verbal").toString(),
                                        persona.get("ingles_escrito").toString(),
                                        //persona.get("habilidades_blandas").toString(),
                                        persona.get("habilidades_blandas_creativethinking").toString(),
                                        persona.get("habilidades_blandas_curtomermanagement").toString(),
                                        persona.get("habilidades_blandas_leadership").toString(),
                                        persona.get("habilidades_blandas_selflearning").toString(),
                                        persona.get("habilidades_blandas_proyectmanagement").toString(),
                                        persona.get("experiencia_laboral").toString(),
                                        persona.get("candidato_estado").toString()),
                                        ',', '"');
                            }
                        } else {
                            CSVUtils.writeLine(writer, Arrays.asList(
                                    persona.get("nombre_candidato").toString(),
                                    persona.get("email").toString(),
                                    persona.get("telefono").toString(),
                                    persona.get("residencia").toString(),
                                    persona.get("edad").toString(),
                                    persona.get("experiencia_ti").toString(),
                                    persona.get("carrera").toString(),
                                    persona.get("universidad").toString(),
                                    persona.get("perfil").toString(),
                                    "",
                                    "",
                                    persona.get("ingles_conocimiento").toString(),
                                    persona.get("ingles_verbal").toString(),
                                    persona.get("ingles_escrito").toString(),
                                    //persona.get("habilidades_blandas").toString(),
                                    persona.get("habilidades_blandas_creativethinking").toString(),
                                    persona.get("habilidades_blandas_curtomermanagement").toString(),
                                    persona.get("habilidades_blandas_leadership").toString(),
                                    persona.get("habilidades_blandas_selflearning").toString(),
                                    persona.get("habilidades_blandas_proyectmanagement").toString(),
                                    persona.get("experiencia_laboral").toString(),
                                    persona.get("candidato_estado").toString()),
                                    ',', '"');
                        }

                        //muevo el archivo que ya se procesó
                        File dest = new File(carpetaSalida + listOfFiles[i].getName());
                        if(dest.exists()){
                            dest.delete();
                        }
                        FileUtils.moveFile(listOfFiles[i], dest);
                    }
                } else if (listOfFiles[i].isDirectory()) {
                    System.out.println("Directory " + listOfFiles[i].getName());
                }

                System.out.println((i + 1) + "/" + listOfFiles.length);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Lero archivo plano
     *
     * @param archivo
     * @return
     * @throws FileNotFoundException
     */
    private static String leerArchivo(File archivo) throws FileNotFoundException, IOException {
        StringBuilder txt = new StringBuilder();

        LineIterator it = IOUtils.lineIterator(
                new BufferedReader(new FileReader(archivo)));
        for (int lineNumber = 0; it.hasNext(); lineNumber++) {
            String line = (String) it.next();
            txt.append(line).append("\n");
        }

        it.close();

        return txt.toString();
    }

    /**
     * Leer documento e interpretar su contenido
     *
     * @param txt
     */
    private static Map<String, Object> procesarDocumento(String txt) {
        Map<String, Object> persona = new HashMap<>();

        StringTokenizer tokens = new StringTokenizer(txt, "\n");//deliminta por nueva linea por defecto
        int nroLinea = 0;
        String linea;
        String lineaB;
        String lineaAux;
        int ciclomaximo = 500;
        int ciclo = 0;
        while (tokens.hasMoreTokens()) {
            nroLinea++;
            linea = tokens.nextToken();
            //System.out.println("tk:" + linea);
            if (!linea.trim().isEmpty()) {//descarto lineas vacias
                if (nroLinea == 1) {//la pimera linea es el nombre del candidato
                    persona.put("nombre_candidato", linea);
                }

                if (linea.trim().startsWith(KEY_EMAIL)) {
                    persona.put("email", linea.trim().replace(KEY_EMAIL, "").trim());
                }

                if (linea.trim().startsWith(KEY_PHONE)) {
                    persona.put("telefono", linea.trim().replace(KEY_PHONE, "").trim());
                }

                if (linea.trim().startsWith(KEY_RESIDENCE)) {
                    if (tokens.hasMoreTokens()) {
                        linea = tokens.nextToken();//esta en la siguiente linea
                    }
                    persona.put("residencia", linea.trim().replace(KEY_RESIDENCE, "").trim());
                }

                if (linea.trim().startsWith(KEY_AGE)) {
                    if (tokens.hasMoreTokens()) {
                        linea = tokens.nextToken();//esta en la siguiente linea
                    }
                    persona.put("edad", linea.trim().replace(KEY_AGE, "").trim());
                }

                if (linea.trim().startsWith(KEY_TOTAL_YEARS_TECEXPERIENCE)) {
                    persona.put("experiencia_ti", linea.trim().replace(KEY_TOTAL_YEARS_TECEXPERIENCE, "").trim());
                }

                if (linea.trim().startsWith(KEY_EDUCATION + " " + KEY_CAREER)) {
                    persona.put("carrera", linea.trim().replace(KEY_EDUCATION + " " + KEY_CAREER, "").trim());
                }

                if (linea.trim().startsWith(KEY_UNIVERSITY)) {
                    persona.put("universidad", linea.trim().replace(KEY_UNIVERSITY, "").trim());
                }

                if (linea.trim().startsWith(KEY_PROFILE)) {//pueden ser varias lineas
                    StringBuilder perfil = new StringBuilder();
                    perfil.append(linea.trim().replace(KEY_PROFILE, "").trim());
                    if (tokens.hasMoreTokens()) {
                        linea = tokens.nextToken();
                    }

                    ciclo = 0;
                    while (!linea.trim().startsWith(KEY_PROFICIENCY_IN) && ciclo < ciclomaximo) {
                        perfil.append(" ").append(linea);
                        if (tokens.hasMoreTokens()) {
                            linea = tokens.nextToken();
                        }
                        ciclo++;
                    }
                    if (ciclo >= ciclomaximo) {
                        System.err.println("Error en ciclo de perfil");
                    }
                    persona.put("perfil", perfil.toString());
                }

                if (linea.trim().startsWith(KEY_PROFICIENCY_IN)) {//Bloque con las habilidades que tiene el candidato
                    if (tokens.hasMoreTokens()) {
                        linea = tokens.nextToken();
                    }
                    Map<String, String> habilidades = new HashMap<String, String>();
                    ciclo = 0;
                    while (!linea.trim().startsWith(KEY_ENGLISH_LANGUAGE_EXTRA1) && ciclo < ciclomaximo) {
                        if (!linea.trim().startsWith(KEY_PROGRAMING) && !linea.trim().isEmpty()) {//ignoro la linea donde dice: programming languages
                            //a veces la informacion de la habilidad esta en una sola linea en otras esta en la linea siguiente
                            //tambien en algunos en una sola linea hay varias habilidades calificadas
                            if (Character.isDigit(linea.trim().charAt(linea.trim().length() - 1))) {
                                //uso expresion regular por si hay varias habilidades en la linea
                                Pattern p = Pattern.compile("[a-zA-Z#\\s\\+]+\\s\\d+");
                                Matcher m = p.matcher(linea.trim());
                                while (m.find()) {
                                    //System.out.println(m.group());
                                    lineaAux = m.group();
                                    habilidades.put(lineaAux.trim().replace(lineaAux.trim().charAt(lineaAux.trim().length() - 1) + "", ""), lineaAux.trim().charAt(lineaAux.trim().length() - 1) + "");
                                }
                            } else {
                                if (tokens.hasMoreTokens() && !linea.trim().isEmpty()) {
                                    lineaB = tokens.nextToken();
                                    habilidades.put(linea.trim(), lineaB.trim());
                                }
                            }
                        }
                        if (tokens.hasMoreTokens()) {
                            linea = tokens.nextToken();
                        }
                        ciclo++;
                    }
                    if (ciclo >= ciclomaximo) {
                        System.err.println("Error en ciclo de habilidades tecnicas");
                    }

                    persona.put("habilidades_tecnicas", habilidades);
                }

                if (linea.trim().startsWith(KEY_ENGLISH_LANGUAGE_EXTRA1)) {//Bloque con las habilidades que tiene el candidato
                    if (tokens.hasMoreTokens()) {
                        linea = tokens.nextToken();
                    }
                    if (linea.trim().startsWith(KEY_ENGLISH_LANGUAGE)) {//despues de English language normalmente esta los numeros                        
                        if (tokens.hasMoreTokens()) {
                            linea = tokens.nextToken();
                        }
                        String[] ingles = linea.trim().split(" ", -1);
                        if (ingles.length == 3) {
                            persona.put("ingles_conocimiento", ingles[0]);
                            persona.put("ingles_verbal", ingles[1]);
                            persona.put("ingles_escrito", ingles[2]);
                        }
                    }
                    //despues dice normalmente: Lenguage communication communication
                    if (tokens.hasMoreTokens()) {
                        linea = tokens.nextToken();
                    }
                    if (tokens.hasMoreTokens()) {
                        linea = tokens.nextToken();
                    }
                }

                if (linea.trim().startsWith(KEY_SOFT_SKILLS)) {//Bloque con las habilidades que tiene el candidato
//                    List<String> habilidades = new ArrayList<>();
//                    habilidades.addAll(buscarHabilidades(linea.trim().replace(KEY_SOFT_SKILLS, "").trim()));
                    //StringBuilder habilidadesBlandas = new StringBuilder();
                    //habilidadesBlandas.append(linea.trim().replace(KEY_SOFT_SKILLS, "").trim());
                    linea = linea.trim().replace(KEY_SOFT_SKILLS, "").trim();
                    //"Creative thinking", "Curtomer management", "Leadership", "Self learning", "Project Management"
                    persona.put("habilidades_blandas_creativethinking", 0);
                    persona.put("habilidades_blandas_curtomermanagement", 0);
                    persona.put("habilidades_blandas_leadership", 0);
                    persona.put("habilidades_blandas_selflearning", 0);
                    persona.put("habilidades_blandas_proyectmanagement", 0);

                    if (linea.trim().toLowerCase().contains("Creative thinking".toLowerCase())) {
                        persona.put("habilidades_blandas_creativethinking", 1);
                    }
                    if (linea.trim().toLowerCase().contains("Curtomer management".toLowerCase())) {
                        persona.put("habilidades_blandas_curtomermanagement", 1);
                    }
                    if (linea.trim().toLowerCase().contains("Leadership".toLowerCase())) {
                        persona.put("habilidades_blandas_leadership", 1);
                    }
                    if (linea.trim().toLowerCase().contains("Self learning".toLowerCase())) {
                        persona.put("habilidades_blandas_selflearning", 1);
                    }
                    if (linea.trim().toLowerCase().contains("Project Management".toLowerCase())) {
                        persona.put("habilidades_blandas_proyectmanagement", 1);
                    }

                    if (tokens.hasMoreTokens()) {
                        linea = tokens.nextToken();
                    }
                    ciclo = 0;

                    //leo las demas lineas
                    while (!linea.trim().startsWith(KEY_WORK_EXPERIENCE) && ciclo < ciclomaximo) {
                        if (!linea.trim().isEmpty()) {
                            //habilidadesBlandas.append(" ").append(linea).append(",");
                            //habilidades.addAll(buscarHabilidades(linea.trim()));
                            if (linea.trim().toLowerCase().contains("Creative thinking".toLowerCase())) {
                                persona.put("habilidades_blandas_creativethinking", 1);
                            }
                            if (linea.trim().toLowerCase().contains("Curtomer management".toLowerCase())) {
                                persona.put("habilidades_blandas_curtomermanagement", 1);
                            }
                            if (linea.trim().toLowerCase().contains("Leadership".toLowerCase())) {
                                persona.put("habilidades_blandas_leadership", 1);
                            }
                            if (linea.trim().toLowerCase().contains("Self learning".toLowerCase())) {
                                persona.put("habilidades_blandas_selflearning", 1);
                            }
                            if (linea.trim().toLowerCase().contains("Project Management".toLowerCase())) {
                                persona.put("habilidades_blandas_proyectmanagement", 1);
                            }
                        }
                        if (tokens.hasMoreTokens()) {
                            linea = tokens.nextToken();
                        }
                        ciclo++;
                    }
                    if (ciclo >= ciclomaximo) {
                        System.err.println("Error en ciclo de habilidades blandas");
                    }
                    //persona.put("habilidades_blandas", habilidades);
                }

                if (linea.trim().startsWith(KEY_WORK_EXPERIENCE)) {//Bloque con la experiencia del candidato
                    StringBuilder experienciaLaboral = new StringBuilder();
                    experienciaLaboral.append(linea.trim().replace(KEY_WORK_EXPERIENCE, "").trim());
                    if (tokens.hasMoreTokens()) {
                        linea = tokens.nextToken();
                    }
                    ciclo = 0;
                    while (!linea.trim().startsWith(KEY_APPROVED) && !linea.trim().startsWith(KEY_REJECTED) && ciclo < ciclomaximo) {
                        if (!linea.trim().isEmpty()) {
                            experienciaLaboral.append(" ").append(linea).append(",");
                        }
                        if (tokens.hasMoreTokens()) {
                            linea = tokens.nextToken();
                        }
                        ciclo++;
                    }
                    if (ciclo >= ciclomaximo) {
                        System.err.println("Error en ciclo de experiencia");
                    }
                    persona.put("experiencia_laboral", experienciaLaboral.toString());
                }

                //La ultima linea normalmente dice aprovado o rechazado, aunque en el nombre del archivo tambien lo dice
                if (linea.trim().startsWith(KEY_APPROVED)) {
                    persona.put("candidato_estado", "Aprobado");
                }

                if (linea.trim().startsWith(KEY_REJECTED)) {
                    persona.put("candidato_estado", "Rechazado");
                }
            }
        }

        return persona;
    }

    /**
     * Buscar habilidades blandas del mapa "Creative thinking", "Curtomer
     * management", "Leadership", "Self learning", "Project Management",
     *
     * @param txtHabilidades
     * @return
     */
    private static Collection<String> buscarHabilidades(String txtHabilidades) {
        Collection<String> retorno = new ArrayList<>();
        Collection<String> lista = mapHabilidadesBlandas.values();
        for (String valor : lista) {
            if (txtHabilidades.contains(valor.toLowerCase())) {
                retorno.add(valor);
            }
        }
        return retorno;
    }

}
