package com.ampliar.filejoiner;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String... args) {

        String authorName = "Cleber Zanella";
        String documentNumber = "";
        String repositoryUrl = "";
        String baseFolder = "/home/cleberzanella/Documents/dev/android/workspaces/myapp/app/src/main/java/";

        // criação do objeto documento
        Document document = new Document();

        try {

//            PdfReader reader = new PdfReader("HelloWorld.pdf");
            /*PdfWriter writer = */PdfWriter.getInstance(document, new FileOutputStream("PDF_DevMedia.pdf"));
//            PdfStamper stamper = new PdfStamper(reader, writer, PdfWriter.VERSION_1_5);
//            writer.setPdfVersion(PdfWriter.PDF_VERSION_1_5);
//            writer.setCompressionLevel(PdfStream.BEST_COMPRESSION);
//            reader.removeFields();
//            reader.removeUnusedObjects();
//            stamper.getReader().removeUnusedObjects();
//
//            stamper.setFullCompression();
//            stamper.getWriter().setFullCompression();
//            stamper.close();

            document.open();

            document.addTitle("Registro INPI - fontes do aplicativo");
            document.addSubject("Fontes para o INPI");
            document.addKeywords("copilotoapp.com");
            document.addCreator("Copiloto Telemetria");
            document.addAuthor(authorName);

            // content
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL);
            Font codedFont = new Font(Font.FontFamily.COURIER, 8f, Font.NORMAL);

            // lista de arquivos
            document.add(new Paragraph("Código fonte do aplicativo Copiloto", titleFont));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Fonte de propriedade de " + authorName + " portador do CPF " + documentNumber + ", versionado no repositorio: " + repositoryUrl, normalFont));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Lista de arquivos.", boldFont));

            URI baseFolderUri = new File(baseFolder).toURI();
            java.util.List<File> listOfFiles = new ArrayList<>();
            listf(baseFolder, listOfFiles);

            for(File file : listOfFiles) {
                document.add(new Paragraph(Paths.get(baseFolderUri).relativize(Paths.get(file.getPath())).toString(), normalFont));
            }

            // conteúdo dos arquivos
            document.newPage();
            document.add(new Paragraph("Conteúdo dos arquivos.", boldFont));
            document.add(Chunk.NEWLINE);

            for(File file : listOfFiles) {

                document.add(new Paragraph(Paths.get(baseFolderUri).relativize(Paths.get(file.getPath())).toString(), normalFont));
                document.add(new Paragraph(readFile(file.getPath(), StandardCharsets.UTF_8), codedFont));

                document.add(Chunk.NEWLINE);
            }

        }
        catch(DocumentException de) {
            System.err.println(de.getMessage());
        }
        catch(IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }

    private static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private static void listf(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        // Get all the files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null){
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), files);
                }
            }
        }
    }

}

