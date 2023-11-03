package com.udacity.jwdnd.course1.cloudstorage.controller;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.security.core.Authentication;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.nio.file.Files;
@Controller
public class FileController {
    private final UserService userService;
    private final FileService fileService;
    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }
    @PostMapping("/home/file")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile,
                             Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserId();
        //Display error message if no file uploaded
        if (multipartFile.isEmpty()) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", "Please, choose a file to upload!");
            return "redirect:/home";
        }
        //Checking if filename already exists
        if(fileService.isFilenameAvailable(multipartFile.getOriginalFilename(),userId)) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", "File name already exists!");
            return "redirect:/home";
        }
        try {
            fileService.createFile(multipartFile, userId);
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message", "New file added successfully!");
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", "Error adding file!" + e.getMessage());
        }
        return "redirect:/home";
    }
    @PostMapping("home/file/delete")
    public ModelAndView deleteFile(@ModelAttribute File fileDelete, Authentication authentication, Model model) {
        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserId();
        try {
            fileService.deleteFile(fileDelete, userId);
            model.addAttribute("success", true);
            model.addAttribute("message", "file Deleted!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Error deleting file!" + e.getMessage());
        }
        return new ModelAndView("result");
    }
    @GetMapping("home/file/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable("fileId") Integer fileId) {
        File file = fileService.getFileByFileId(fileId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(httpHeaders.CONTENT_DISPOSITION, "attachment; filename = " + file.getFileName());
        httpHeaders.add("Cache-control", "no-cache, no-store, must-revalidate");
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires", "0");
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(resource);
    }

}