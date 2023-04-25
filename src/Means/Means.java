/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Means;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author OIM
 */
public class Means {

    public static String returnStructClass(String stringPackage, String nameClass, Map<String, String> contenido) {
        String struct = "package " + stringPackage + ";";
        if (contenido != null) {
            if (!contenido.isEmpty()) {
                struct += "\npublic class " + nameClass.trim() + " {";
                struct = contenido
                        .keySet()
                        .stream()
                        .map(key -> "\n\t" + returnVariable(key, contenido.get(key)))
                        .reduce(struct, String::concat);

                struct += "\n}";
            }
        }
        return struct;
    }

    public static String returnVariable(String nameVariable, String contenidoVariable) {
//        return "public static String " + nameVariable.trim() + " = \"" + contenidoVariable.trim().replaceAll("\"", "\\\\\"") + "\";";
//        Map<String, String> mapIcon = new LinkedHashMap<>();
//        mapIcon.put("\"name\"", "\""+nameVariable.trim()+"\"");
//        mapIcon.put("\"body\"", "\""+contenidoVariable.trim().replaceAll("\"", "\\\\\"")+"\"");
//        JSONObject jsonIcon = new JSONObject(mapIcon);
//        String jsonIconString = jsonIcon.toString();
        return "public static String " + nameVariable.trim() + " = \"{\\\"name\\\": \\\""+nameVariable.trim()+"\\\",\\\"body\\\": \\\""+contenidoVariable.trim().replaceAll("\"", "\\\\\\\\\\\\\"")+"\\\"}\";";
    }

    public static String returnStructClassByImport(String stringPackage, String nameClass, Map<String, String> contenido, List<String> imports) {
        String struct = "package " + stringPackage + ";";
        if (imports != null) {
            if (!imports.isEmpty()) {
                struct += "\n" + imports.stream().reduce("", (acumulador, item) -> {
                    return acumulador.trim() + (acumulador.trim().isEmpty() ? "" : "\n") + "import " + item.trim() + ";";
                });
            }
        }
        if (contenido != null) {
            if (!contenido.isEmpty()) {
                struct += "\npublic class " + nameClass.trim() + " {";
                struct = contenido
                        .keySet()
                        .stream()
                        .map(key -> "\n\t" + returnVariableImportada(key, contenido.get(key)))
                        .reduce(struct, String::concat);

                struct += "\n}";
            }
        }
        return struct;
    }

    public static String returnVariableImportada(String nameVariable, String contenidoVariable) {
        return "public static String " + nameVariable.trim() + " = " + contenidoVariable.trim() + ";";
    }
}
