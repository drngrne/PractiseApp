package com.greenedarren.practiseapp;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TemplatePDF {

    private Context context;
    private File pdfFile;
    private com.itextpdf.text.Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fontTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private Font fontSubTitle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font fontText = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private Font fontHighText = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.RED);


    public TemplatePDF(Context context) {
        this.context = context;
    }



    public void openDocument (){
        createFile();
        try{
            document = new com.itextpdf.text.Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

        }catch (Exception e){
            Log.e("openDocument", e.toString());
        }
    }



    private void createFile() {
        File folder = new File (Environment.getExternalStorageDirectory().toString(),"PDF");

        if (!folder.exists()){
            folder.mkdirs();
        }
        pdfFile = new File(folder, "TemplatePDF.pdf");
    }



    public void closeDocument(){
        document.close();
    }



    public void addMetaData(String title, String subject, String author){
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
    }



    public void addTitles(String title, String subTitle, String date){

        try {
            paragraph = new Paragraph();
            addChildPara(new Paragraph(title, fontTitle));
            addChildPara(new Paragraph(subTitle, fontSubTitle));
            addChildPara(new Paragraph("Generado: " +date, fontHighText));
            paragraph.setSpacingAfter(30);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addTitles", e.toString());
        }
    }



    public void addChildPara(Paragraph childPara){
        childPara.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childPara);
    }



    public void addPara(String text){

        try {
            paragraph = new Paragraph(text, fontText);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addPara", e.toString());
        }
    }


    public void createTable(String[]header, ArrayList<String[]>clients){

        try {
            paragraph = new Paragraph();
            paragraph.setFont(fontText);
            PdfPTable pdfPTable = new PdfPTable(header.length);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int indexC = 0;

            while (indexC < header.length){
                pdfPCell = new PdfPCell(new Phrase(header[indexC++], fontSubTitle));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setBackgroundColor(BaseColor.GREEN);
                pdfPTable.addCell(pdfPCell);
            }

            for (int indexR = 0; indexR < clients.size() ; indexR++){
                String[]row = clients.get(indexR);

                for (indexC = 0; indexC < clients.size() ; indexC++){
                    pdfPCell = new PdfPCell(new Phrase(row [indexC]));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setFixedHeight(40);
                    pdfPTable.addCell(pdfPTable);
                }
            }

            paragraph.add(pdfPTable);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("createTable", e.toString());
        }
    }
}
