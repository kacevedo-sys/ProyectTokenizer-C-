package com.unigal.lexer;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Path inputPath = null;
        if (args.length >= 1) {
            inputPath = Paths.get(args[0]);
            if (!Files.exists(inputPath)) {
                System.err.println("Archivo no encontrado: " + inputPath);
                inputPath = null;
            }
        }
        if (inputPath == null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Seleccione el archivo de entrada (una cadena por línea)");
            int ret = chooser.showOpenDialog(null);
            if (ret == JFileChooser.APPROVE_OPTION) {
                inputPath = chooser.getSelectedFile().toPath();
            } else {
                System.out.println("No se seleccionó archivo. Saliendo.");
                return;
            }
        }

        String content = Files.readString(inputPath);
        Lexer lexer = new Lexer(content);
        Lexer.LexResult res = lexer.tokenize();

        // Prepare Salida.txt
        Path salida = Paths.get("Salida.txt");
        try (BufferedWriter w = Files.newBufferedWriter(salida)) {
            w.write("=== REPORTE DE TOKENS ===\n\n");
            // Write tokens grouped by original lines: since input is multiline, print per-line summary
            String[] lines = content.split("\r?\n", -1);
            int lineNum = 1;
            int tokenIndex = 0;
            for (String line : lines) {
                w.write(String.format("Linea %d: %s\n", lineNum, line));
                w.write("  Tokens:\n");
                // list tokens whose token.line == lineNum
                for (Token t : res.tokens) {
                    if (t.line == lineNum && t.type != Token.Type.EOF) {
                        w.write("    " + t.toString() + "\n");
                    }
                }
                lineNum++;
            }
            w.write("\n=== ERRORES DETECTADOS ===\n");
            if (res.errors.isEmpty()) {
                w.write("No se detectaron errores.\n");
            } else {
                for (String e : res.errors) w.write(e + "\n");
            }
            w.write("\n=== RESUMEN POR TIPO ===\n");
            for (Map.Entry<Token.Type, Integer> e : res.counts.entrySet()) {
                w.write(String.format("%s : %d\n", e.getKey().name(), e.getValue()));
            }
        }

        System.out.println("Procesamiento completado. Archivo 'Salida.txt' generado en el directorio de ejecución.");
    }
}
