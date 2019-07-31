package com.file.dao;

import com.file.pojo.FileOffice;

import java.util.List;

public interface FileOfficeDao {

    int  uploadFile(FileOffice fileOffice);

    List<FileOffice> queryFileInfo();

    int deleteFileInfo(String id);
}
