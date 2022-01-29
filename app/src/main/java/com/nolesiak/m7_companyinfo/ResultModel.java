package com.nolesiak.m7_companyinfo;

import android.database.Cursor;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ResultModel {

    private String regon;
    private String nip;
    private String province;
    private String city;
    private String name;

    public static ResultModel fromXmlResult(String result) {

        Document doc = convertStringToXMLDocument(result);

        ResultModel rm = new ResultModel();
        NodeList nodeList = doc.getElementsByTagName("Miejscowosc");
        rm.setCity(nodeList.item(0).getTextContent());

        nodeList = doc.getElementsByTagName("Nazwa");
        rm.setName(nodeList.item(0).getTextContent());

        nodeList = doc.getElementsByTagName("Wojewodztwo");
        rm.setProvince(nodeList.item(0).getTextContent());

        nodeList = doc.getElementsByTagName("Regon");
        rm.setRegon(nodeList.item(0).getTextContent());

        nodeList = doc.getElementsByTagName("Nip");
        rm.setNip(nodeList.item(0).getTextContent());

        return rm;
    }

    private static Document convertStringToXMLDocument(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultModel fromCursor(Cursor res) {

        ResultModel rm = new ResultModel();
        res.moveToFirst();
        int index =res.getColumnIndex("nip");
        String x = res.getString(index);
        rm.setNip(res.getString(index));

        index = res.getColumnIndex("regon");
        rm.setRegon(res.getString(index));

        index = res.getColumnIndex("name");
        rm.setName(res.getString(index));

        index = res.getColumnIndex("city");
        rm.setCity(res.getString(index));

        index = res.getColumnIndex("province");
        rm.setProvince(res.getString(index));

        return rm;
    }


    public String getRegon(){
        return  this.regon;
    }
    public String getNip(){
        return this.nip;
    }

    public String getCity() {
        return this.city;
    }

    public String getName() {
        return this.name;
    }

    public String getProvince() {
        return this.province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setRegon(String regon) {
        this.regon = regon;
    }
}
