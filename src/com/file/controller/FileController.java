package com.file.controller;



import com.file.pojo.FileOffice;
import com.file.service.FileOfficeService;
import com.file.util.PoiExcelToHtmlUtil;
import com.file.util.PoiWordToHtmlUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class FileController {

    @Autowired
    private FileOfficeService fileOfficeService;

  /*  @RequestMapping("showPdf")
    public void showPdf(HttpServletRequest request, HttpServletResponse response, String fileName) throws Exception {
           request.setCharacterEncoding("UTF-8");
       String  path=request.getSession().getServletContext().getRealPath("");
       String downLoadPath=path+"/file/"+fileName;
        FileInputStream in=new FileInputStream(new File(downLoadPath));
        OutputStream out=response.getOutputStream();
        byte[] b=new byte[512];
        while ((in.read(b))!=-1){
            out.write(b);
        }
        out.flush();
        out.close();
        in.close();
    }
    @RequestMapping("finExcel")
    public void finExcel(HttpServletRequest request, HttpServletResponse response, String fileName) throws Exception {
        String dd= PoiExcelToHtmlUtil.excelToHtml("D:\\", "ddd.xls");
        PrintWriter out= response.getWriter();
         out.print(dd);
    }
    @RequestMapping("finWord")
    public void finWord(HttpServletRequest request, HttpServletResponse response, String fileName) throws Exception {
        String aa= PoiWordToHtmlUtil.wordToHtml("E:\\", "简历.docx");
        PrintWriter out= response.getWriter();
        out.print(aa);
    }*/
  /* 上传文件并把文件信息提交到库中
   * @Param multipartFile 文件
   * */
   @RequestMapping(value = "uploadFile")
   @ResponseBody
   public Object  uploadFile(@RequestParam("file") MultipartFile multipartFile,HttpServletRequest request)throws Exception{
       String path= request.getServletContext().getRealPath("//file//");
       System.out.println(path);
       int num= fileOfficeService.uploadFile(multipartFile,path);
       return "上传成功";
   }
    /* 文件表数据查询
     * */
   @RequestMapping(value ="tableQuery")
   @ResponseBody
    public Object  tableQuery(){
       List<FileOffice> fileList=fileOfficeService.queryFileInfo();
       return fileList;
   }
    /* 在线查看word，pdf，excel,txt文件
      @Param fileName 文件名
      @Param fileType 文件类型
     * */
   @RequestMapping("showFile")
    public String showFile( String fileName,String fileType,String id, HttpServletRequest request, HttpServletResponse response)throws Exception{
       request.setCharacterEncoding("UTF-8");
       String path=request.getSession().getServletContext() .getRealPath("/")+"file\\";
       if(fileType.equals("1")){
    	   String fileNamePostfix=fileName.substring(fileName.lastIndexOf(".") + 1);
    	   String wordName=id+"."+fileNamePostfix;
           PoiWordToHtmlUtil.wordToHtml(path,wordName);
			/* String fileNameurl= URLEncoder.encode(fileName); */
         return "redirect:/file/"+wordName+".html";
      }else if(fileType.equals("2")){
           PrintWriter out= response.getWriter();
           String htmlStr= PoiExcelToHtmlUtil.excelToHtml(path,fileName);
           out.print(htmlStr);
      }else if(fileType.equals("3")){
           response.setCharacterEncoding("utf-8");
           response.setContentType("text/html; charset=utf-8");
           PrintWriter out= response.getWriter();
         File file=new File(path+fileName);
         StringBuilder builder=new StringBuilder();
           InputStreamReader read = new InputStreamReader(
                   new FileInputStream(file), "utf-8");
           BufferedReader bufferedReader = new BufferedReader(read);
           String lineTxt = null;
           while ((lineTxt = bufferedReader.readLine()) != null) {
               builder.append(lineTxt);
           }
           out.print(builder.toString());
      }else if(fileType.equals("4")){
           FileInputStream in=new FileInputStream(path+fileName);
           OutputStream outputStream=response.getOutputStream();
           byte[] b=new byte[512];
           while ((in.read(b))!=-1){
               outputStream.write(b);
           }
           outputStream.flush();
           outputStream.close();
           in.close();
    }
       return null;
   }
    /* 删除文件
      @Param id 文件上传时候自动生成Id
      @Param fileName 文件名
      @Param fileTypeName 文件类型名
      * */
   @RequestMapping("deleteFile")
   @ResponseBody
   public Object deleteFile(String id,String fileName,String fileTypeName){
         int num=fileOfficeService.deleteFileInfo(id,fileName,fileTypeName);
         if(num>0){
             return "删除成功";
         }else{
             return "删除失败";
         }
   }
    /* 文件下载
       @Param fileName 文件名
      * */
   @RequestMapping("downloadFile")
    public ResponseEntity<byte[]> downloadFile(String fileName,String id, HttpServletRequest request, Model model)throws Exception{
    	 String path=request.getSession().getServletContext() .getRealPath("/")+"file\\";
    	String fileNamePostfix=fileName.substring(fileName.lastIndexOf(".") + 1);
    	File file=new File(path+id+"."+fileNamePostfix);
       String dfileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
       headers.setContentDispositionFormData("attachment", dfileName);
       return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
   }


}
