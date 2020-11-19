package com.example.ssm_demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * @Auther: WangPingYin
 * @Description:文件上传Controller
 * @Date: 2019/8/8 15:22
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {
    @Value("${uploadFile.location}")
    private String uploadFileLocation;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file)throws IOException{
        String filename = file.getOriginalFilename();
        File saveFile = new File(uploadFileLocation, filename);
        file.transferTo(saveFile);
        if (file == null || file.isEmpty()) {
            return "上传文件为空";
        }
        return "not null";
    }

    @RequestMapping(value = "upload1", method = RequestMethod.POST)
    public String uploadFile1(@RequestParam("file") List<MultipartFile> files)throws IOException{
        String filename="";
        if (files == null || files.isEmpty()) {
            return "上传文件为空";
        }else{
            for(int i=0;i<files.size();i++){
                String name=uploadFileLocation;
                filename = files.get(i).getOriginalFilename();
                File saveFile = new File(name, filename);
                if(!saveFile.exists()&&!saveFile.isDirectory()){
                    saveFile.mkdirs();
                }
                files.get(i).transferTo(saveFile);
            }
        }
        return "not null";
    }
}
