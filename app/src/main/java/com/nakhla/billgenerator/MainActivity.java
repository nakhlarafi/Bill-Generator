package com.nakhla.billgenerator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PRAcroForm;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.itextpdf.text.pdf.interfaces.PdfDocumentActions;
import com.itextpdf.text.pdf.parser.LineSegment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.function.LongFunction;

public class MainActivity extends AppCompatActivity {

    Button btn_create_pdf;
    String [] stringArray;
    String totalQuantityStr = "";
    String totalAmount = "";
    HashMap<String, String> hashMap;

    DateTimeFormatter dtf;
    LocalDateTime now;

    public static final String[] units = { "", "One", "Two", "Three", "Four",
            "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve",
            "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
            "Eighteen", "Nineteen" };

    public static final String[] tens = {
            "",         // 0
            "",     // 1
            "Twenty",   // 2
            "Thirty",   // 3
            "Forty",    // 4
            "Fifty",    // 5
            "Sixty",    // 6
            "Seventy",  // 7
            "Eighty",   // 8
            "Ninety"    // 9
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_create_pdf = (Button) findViewById(R.id.btn_create_pdf);
        Intent intent = getIntent();
        stringArray = intent.getStringArrayExtra("string-array");
        hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }

        calculateTotal();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        btn_create_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                createPDFFile(Common.getAppPath(MainActivity.this)+"test_pdf.pdf");
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    public static String convert(final int n) {
        if (n < 0) {
            return "Minus " + convert(-n);
        }

        if (n < 20) {
            return units[n];
        }

        if (n < 100) {
            return tens[n / 10] + ((n % 10 != 0) ? " " : "") + units[n % 10];
        }

        if (n < 1000) {
            return units[n / 100] + " Hundred" + ((n % 100 != 0) ? " " : "") + convert(n % 100);
        }

        if (n < 100000) {
            return convert(n / 1000) + " Thousand" + ((n % 10000 != 0) ? " " : "") + convert(n % 1000);
        }

        if (n < 10000000) {
            return convert(n / 100000) + " Lakh" + ((n % 100000 != 0) ? " " : "") + convert(n % 100000);
        }

        return convert(n / 10000000) + " Crore" + ((n % 10000000 != 0) ? " " : "") + convert(n % 10000000);
    }

    public void calculateTotal(){
        double qnt = 0;
        double tk = 0;
        for (int i = 7; i < stringArray.length;i+=8){
            qnt = qnt + Double.parseDouble(stringArray[i-3]);
            tk = tk + Double.parseDouble(stringArray[i-1]);
        }
        totalQuantityStr = Double.toString(qnt);
        totalAmount = String.format("%.2f", tk);
        System.out.println(totalAmount+"$$$$$$$$$");
    }

    private void createPDFFile(String path) {
        if (new File(path).exists())
            new File(path).delete();
        try{
            Document document = new Document();

            //Save
            PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(path));

            //Open to Write
            document.open();

            //Setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("EDMTDev");
            document.addCreator("Eddy Lee");

            //FontSettings
            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontSize = 20.0f;
            float valueFontSize = 26.0f;

            //Custom Font
            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf",
                    "UTF-8",BaseFont.EMBEDDED);
            //Create Title
            Font titleFont = new Font(fontName,18.0f,Font.BOLD, BaseColor.BLACK);
            Font titleFont2 = new Font(fontName,8.0f,Font.NORMAL, BaseColor.BLACK);
            addNewItem(document,"Bismillahir Rahmanir Rahim", Element.ALIGN_CENTER,titleFont2);
            addNewItem(document,"M/S DOLPHIN CAREER ENTERPRISE", Element.ALIGN_CENTER,titleFont);

            //Name
            Font titleFontName = new Font(fontName,14.0f,Font.BOLD, BaseColor.BLACK);
            addNewItem(document,"Proprietor: MD IQBAL HOSSAIN (JIBON)", Element.ALIGN_CENTER,titleFontName);

            addNewItem(document,"Address: 103 Pathantuli, Moshjid Road, Narayanganj", Element.ALIGN_CENTER,titleFontName);
            addNewItem(document,"Email: IQBAL021054@GMAIL.COM", Element.ALIGN_CENTER,titleFontName);
            addNewItem(document,"Mobile: 01711074444, 01823058478", Element.ALIGN_CENTER,titleFontName);
            addLineSeperator(document);

            Font orderNumberValueFont = new Font(fontName,14.0f,Font.NORMAL,BaseColor.BLACK);

            //ADD more
            Font orderNumberFont = new Font(fontName,14.0f,Font.BOLD,BaseColor.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                addNewItemWithLeftAndRight(document,"To:","Date: "+dtf.format(now),orderNumberFont,orderNumberFont);
            }

            //addNewItem(document,"Order Date: 29/10/2020", Element.ALIGN_RIGHT,orderNumberFont);

            //To elements
            addNewItem(document,hashMap.get("to"),Element.ALIGN_LEFT,orderNumberValueFont);
            addNewItem(document,"Through: "+hashMap.get("name"),Element.ALIGN_LEFT,orderNumberValueFont);
            addNewItem(document,hashMap.get("post"),Element.ALIGN_LEFT,orderNumberValueFont);
            addNewItem(document,"Side: "+hashMap.get("side"),Element.ALIGN_LEFT,orderNumberValueFont);
            addNewItem(document,"Address: "+hashMap.get("add1"),Element.ALIGN_LEFT,orderNumberValueFont);
            addNewItem(document,hashMap.get("add2"),Element.ALIGN_LEFT,orderNumberValueFont);


            addLineSeperator(document);

            Font poNumberFont = new Font(fontName,12.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"P.O No: "+hashMap.get("po"), Element.ALIGN_RIGHT,poNumberFont);
            //addNewItem(document,"Eddy Lee",Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);

            //ADD product order detail
            //addLineSpace(document);
            addNewItem(document,"Bill No - "+hashMap.get("bill"),Element.ALIGN_CENTER,titleFont);
            //addLineSeperator(document);
            addNewLine(document);

            String colNames[] = {"SL No.","Date","Challan","Name of Voltage","Quantity(cft)","Rate(Tk)","Amount(Tk)",
                    "Remark"};


            Font fontH1 = new Font(fontName, 12.0f, Font.NORMAL);
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 1,3,2,3,3,2,3,2 });
            for(int aw = 0; aw < 8; aw++){
                table.addCell(new PdfPCell(new Phrase(colNames[aw],fontH1)));
            }

            for(int aw = 0; aw < stringArray.length; aw++){
                table.addCell(new PdfPCell(new Phrase(stringArray[aw],fontH1)));
            }


            document.add(table);

            addNewLine(document);

            Font totalAmountValueFont = new Font(fontName,12.0f,Font.NORMAL,BaseColor.BLACK);
            //Item 1
            addNewItemWithLeftAndRight(document,"Total: ","",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,"Quantity(cft)","= "+String.format("%.2f", Double.parseDouble(totalQuantityStr))+" cft",totalAmountValueFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,"Amount","= "+String.format("%.2f", Double.parseDouble(totalAmount))+" Tk",totalAmountValueFont,orderNumberValueFont);

            addNewLine(document);

            //String inWord = convert((long) Double.parseDouble(totalAmount));

            //Signature
            double doubleNumber = Double.parseDouble(totalAmount);
            int intPart = (int) doubleNumber;
            String numberD = String.valueOf(doubleNumber);
            numberD = numberD.substring ( numberD.indexOf ( "." ) +1);
            System.out.println(intPart+"*********"+numberD);
            addNewItem(document,"In Words: "+convert(intPart)+ " point "+
                            convert(Integer.parseInt(numberD))+" Tk Only.",
                    Element.ALIGN_LEFT,totalAmountValueFont);

            addNewLine(document);
            addNewLine(document);
            addNewLine(document);
            addNewLine(document);
            addNewItem(document,"Proprietor: ", Element.ALIGN_RIGHT,poNumberFont);
            //addToBottom(document,"N.B. - All kinds of goods transportation service. ", Element.ALIGN_CENTER,poNumberFont);

            absText(fontName,pdfWriter,"N.B. - All kinds of goods transportation service.",50,50);


            document.close();
            Toast.makeText(this,"Success!",Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                printPdf();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPdf() {
        PrintManager printManager = (PrintManager)getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(MainActivity.this,Common.getAppPath(MainActivity.this)+"test_pdf.pdf");
            printManager.print("Document",printDocumentAdapter, new PrintAttributes.Builder().build());
        }
        catch (Exception ex){
            Log.e("EMDTv",""+ex.getMessage());
        }
    }

    private static void absText(BaseFont bf, PdfWriter writer,String text, int x, int y) {
        try {
            PdfContentByte cb = writer.getDirectContent();
            //BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb.saveState();
            cb.beginText();
            cb.moveText(x, y);
            cb.setFontAndSize(bf, 8);
            cb.showText(text);
            cb.endText();
            cb.restoreState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {

        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);

    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewLine(Document document) throws DocumentException {
        document.add(new Paragraph("\n"));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text,font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }

    private void addToBottom(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text,font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        paragraph.setAlignment(Element.ALIGN_BOTTOM);
        document.add(paragraph);
    }
}
