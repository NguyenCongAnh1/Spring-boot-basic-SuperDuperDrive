package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;
    public FileService(FileMapper fileMapper){
        this.fileMapper = fileMapper;
    }
    public int store(MultipartFile multipartFile, int userId) throws IOException {
        File file = new File(null, multipartFile.getOriginalFilename(), multipartFile.getContentType(),
                multipartFile.getSize(), userId, multipartFile.getBytes());
        return fileMapper.insert(file);
    }
    public List<File> getFilesByUserId(int userId){
        List<File> files = fileMapper.getFilesByUserId(userId);
        for (File file : files) {
            System.out.println("File ID: " + file.getFileId());
            System.out.println("File Name: " + file.getFileName());
            System.out.println("Content Type: " + file.getContentType());
            // In các thông tin khác của file tại đây
        }
        return files;
    }

    public boolean isFilenameAvailable(String originalFilename, Integer userId) {
        return fileMapper.getFile(originalFilename, userId) == null;
    }

    public void createFile(MultipartFile multipartFile, Integer userId) {
        try {
            byte[] fileBytes = multipartFile.getBytes();  // Đọc dữ liệu từ MultipartFile
            // Tạo một đối tượng File từ dữ liệu đã đọc
            File file = new File(null, multipartFile.getOriginalFilename(),multipartFile.getContentType(),multipartFile.getSize(),userId,fileBytes);
            // Tiến hành insert vào cơ sở dữ liệu
            fileMapper.insert(file);
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi khi đọc dữ liệu từ MultipartFile
        }
    }

    public void deleteFile(File fileDelete, Integer userId) {
        fileMapper.deleteFile(fileDelete.getFileId(), userId);
    }

    public File getFileByFileId(Integer fileId) {
        return fileMapper.getFilesByFileId(fileId);
    }


}
