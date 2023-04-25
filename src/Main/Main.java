/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author OIM
 */
public class Main {

    public static void main(String[] args) {
        //package material.design.icons.master;
        recepcionarRutas();
    }

    public static void recepcionarRutas() {
        String rutaO = javax.swing.JOptionPane.showInputDialog(null, "Ruta:", "RUTA ORIGEN", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        String rutaD = javax.swing.JOptionPane.showInputDialog(null, "Ruta:", "RUTA DESTINO", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        verificarRutas("fonts.google.com.icon.icons", rutaO, rutaD);
    }

    public static void verificarRutas(String principal, String rutaOrigen, String rutaDestino) {
        File fileOrigen = new File(rutaOrigen);
        File fileDestino = new File(rutaDestino);
        Map<String, String> icons = new LinkedHashMap<>();
        List<String> imports = new ArrayList<>();

        if (!fileDestino.exists()) {
            fileDestino.mkdirs();
        }

        List<File> filesOrigen = Arrays.asList(fileOrigen.listFiles());
//        int contador = 0;
        for (File file : filesOrigen) {
            List<String> camino = new ArrayList<>();
            Map<String, String> contenido = new HashMap<>();
            camino.add(file.getName());
            verificarArchivo(file, fileDestino, camino, contenido);
//            if (contador == 2) {break;}contador++;
            procesarClase(fileOrigen, fileDestino, file, principal, camino, contenido);
            imports.add(principal + "." + file.getName().trim());
            procesarIcons(file.getName(), contenido, icons);
        }
        principal = principal.replaceAll("."+principal.split("\\.")[principal.split("\\.").length-1],"");
        procesarClaseIcons("Icons",fileDestino, principal, icons, imports);
    }

    public static void verificarArchivo(File file, File fileDestino, List<String> camino, Map<String, String> contenido) {
        if (file.isDirectory()) {
            List<File> filesOrigen = Arrays.asList(file.listFiles());
            for (File file1 : filesOrigen) {
//                if(file1.isDirectory()){
                camino.add(file1.getName());
//               }
                verificarArchivo(file1, fileDestino, camino, contenido);
                camino.remove(camino.size() - 1);
            }
        } else if (file.getName().contains("24px")) {
            String name = camino.stream()
                    .filter(item -> !item.trim().equalsIgnoreCase(file.getName()))
                    .collect(Collectors.toList()).stream()
                    .reduce("",
                            (acumulador, item) -> acumulador.trim() + (acumulador.trim().isEmpty() ? "" : "__") + item.trim()
                    );
            String container = getContenido(file);

            contenido.put(name, container);
        }
    }

    public static String getContenido(File fileSVG) {
        String contenido = "";
        FileReader f = null;
        BufferedReader b = null;
        String cadena = null;
        try {
            f = new FileReader(fileSVG);
            b = new BufferedReader(f);
            cadena = b.readLine();
            while (cadena != null) {
                contenido = contenido.trim() + cadena.trim();
                cadena = b.readLine();
            }
            contenido = contenido.replaceAll("fill=\"#FFFFFF\"", "")
                    .replaceAll("fill=\"#000000\"", "")
                    .replaceFirst("viewBox=\"0 0 24 24\"", " fill=\"#000000\" viewBox=\"0 0 24 24\"")
                    .replaceFirst("height=\"24\"", "height=\"20\"")
                    .replaceFirst("width=\"24\"", "width=\"20\"");

        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                if (b != null) {
                    b.close();
                }
                if (f != null) {
                    f.close();
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        return contenido;
    }

    public static void procesarClase(File fileOrigen, File fileDestino, File file, String principal, List<String> camino, Map<String, String> contenido) {
        String stringPackage = principal.replaceAll("\\.", "\\\\");
//                    + File.separator
//                    + file.getName().trim();
        File fileNewDir = new File(fileDestino.getAbsolutePath() + File.separator + stringPackage);
        if (!fileNewDir.exists()) {
            fileNewDir.mkdirs();
        }
        String claseName = camino.isEmpty() ? fileOrigen.getName().trim() : camino.get(0);
        String claseString = Means.Means.returnStructClass(stringPackage.replaceAll("\\\\", "."), claseName, contenido);
        createClase(fileNewDir.getAbsolutePath() + File.separator + file.getName().trim() + ".java", claseString);
    }

    public static void createClase(String ruta, String contenido) {
        PrintWriter write = null;
        try {
            write = new PrintWriter(ruta);
            write.print(contenido);
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } finally {
            try {
                if (write != null) {
                    write.close();
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void procesarIcons(String name, Map<String, String> contenido, Map<String, String> icons) {
        if (contenido == null) {
            return;
        }
        if (contenido.isEmpty()) {
            return;
        }

        contenido.keySet().forEach(key -> {
            icons.put(key, name + "." + key);
        });
    }

    public static void procesarClaseIcons(String claseName,File fileDestino, String principal, Map<String, String> contenido, List<String> imports) {
        String stringPackage = principal.replaceAll("\\.", "\\\\");
//                    + File.separator
//                    + file.getName().trim();
        File fileNewDir = new File(fileDestino.getAbsolutePath() + File.separator + stringPackage);
        if (!fileNewDir.exists()) {
            fileNewDir.mkdirs();
        }
        String claseString = Means.Means.returnStructClassByImport(stringPackage.replaceAll("\\\\", "."), claseName, contenido, imports);
        createClase(fileNewDir.getAbsolutePath() + File.separator + claseName + ".java", claseString);
    }
}
