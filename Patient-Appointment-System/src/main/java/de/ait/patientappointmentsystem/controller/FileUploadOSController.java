package de.ait.patientappointmentsystem.controller;

import de.ait.patientappointmentsystem.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
public class FileUploadOSController {

    private final FileService fileService;
    private final String uploadOSForm = "uploadOSForm";

    public FileUploadOSController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/uploadOS")
    public String showUploadForm(){
        return uploadOSForm;
    }

    @PostMapping("/uploadOS")
    public String handleFileUploadInOS(@RequestParam("file") MultipartFile file, Model model){
        if (file.isEmpty()) {
            log.info("File is empty");
            model.addAttribute("message", "Вы не выбрали файл для загрузки");
            return uploadOSForm;
        }
        if(!file.getContentType().equalsIgnoreCase("image/jpeg")){
            log.info("File is not image");
            model.addAttribute("message", "Разрешена загрузка только JPEG-файлов");
            return uploadOSForm;
        }
        else {
            String fileName = fileService.saveFileOS(file);
            if (fileName == null) {
                log.info("File save failed");
                model.addAttribute("message","Ошибка при загрузке файла");
            }else {
                log.info("File {} save successful", fileName);
                model.addAttribute("message", "Файл " + fileName + " успешно загружен");
            }
            return uploadOSForm;
        }
    }
}
