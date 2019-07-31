package com.file.service;

import com.file.pojo.FileOffice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileOfficeService {
     int uploadFile(MultipartFile multipartFile,String path);

     List<FileOffice> queryFileInfo();

     int deleteFileInfo(String id,String fileName,String fileTypeName);
}
