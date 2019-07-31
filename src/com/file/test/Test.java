package com.file.test;

import com.file.util.PoiExcelToHtmlUtil;
import com.file.util.PoiWordToHtmlUtil;

public class Test {

    public static void main(String[] args) throws Exception{

        String dd= PoiExcelToHtmlUtil.excelToHtml("D:\\", "ddd.xls");
        System.out.println(dd.trim());
        String aa= PoiWordToHtmlUtil.wordToHtml("E:\\", "简历.docx");
        System.out.println(dd.trim());
    }
}
