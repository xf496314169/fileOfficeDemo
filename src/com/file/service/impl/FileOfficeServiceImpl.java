package com.file.service.impl;

import com.file.dao.FileOfficeDao;
import com.file.pojo.FileOffice;
import com.file.service.FileOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class FileOfficeServiceImpl implements FileOfficeService{

    @Autowired
    private FileOfficeDao fileOfficeDao;
    @Override
    public int uploadFile(MultipartFile multipartFile,String path) {
        int num=0;
        String fileName= multipartFile.getOriginalFilename();
        String id=String.valueOf(System.currentTimeMillis());
         FileOffice fileOffice=new FileOffice();
         fileOffice.setId(id);
         fileOffice.setFileName(fileName);
         fileOffice.setFilePath(path+fileName);
         fileOffice.setFileUploadTime(new Date());
         fileOffice.setIsRemove(1);
         String fileNamePostfix=fileName.substring(fileName.lastIndexOf(".") + 1);

          if(fileNamePostfix.equals("doc")||fileNamePostfix.equals("docx")){
              fileOffice.setFileType("1");
          }else if(fileNamePostfix.equals("xls")||fileNamePostfix.equals("xlsx")){
              fileOffice.setFileType("2");
          }else if(fileNamePostfix.equals("txt")){
              fileOffice.setFileType("3");
          }else if(fileNamePostfix.equals("pdf")){
              fileOffice.setFileType("4");
          }
          try {
              File file=new File(path+id+"."+fileNamePostfix);
              multipartFile.transferTo(file);
              num=fileOfficeDao.uploadFile(fileOffice);
          }catch (Exception e){
              e.printStackTrace();
          }
        return num;
    }

    @Override
    public List<FileOffice> queryFileInfo() {
        return fileOfficeDao.queryFileInfo();
    }

    @Override
    public int deleteFileInfo(String id,String fileName,String fileTypeName) {
        int num=0;
        try {
			/*
			 * File file=new File(FileOffice.path+fileName); file.delete();
			 * if(fileTypeName.equals("Word")){ File fileFolder=new
			 * File(FileOffice.path+fileName+".files"); delAllFile(fileFolder); }
			 */
            num= fileOfficeDao.deleteFileInfo(id);
        }catch (Exception e){
            e.printStackTrace();
        }

        return num;
    }

    public static void delAllFile(File directory){
        if (!directory.isDirectory()){
            directory.delete();
        } else{
            File [] files = directory.listFiles();

         // 空文件夹
            if (files.length == 0){
                directory.delete();
                System.out.println("删除" + directory.getAbsolutePath());
                return;
            }

            // 删除子文件夹和子文件
            for (File file : files){
                if (file.isDirectory()){
                    delAllFile(file);
                } else {
                    file.delete();
                    System.out.println("删除" + file.getAbsolutePath());
                }
            }

            // 删除文件夹本身
            directory.delete();
            System.out.println("删除" + directory.getAbsolutePath());
        }
    }

}
