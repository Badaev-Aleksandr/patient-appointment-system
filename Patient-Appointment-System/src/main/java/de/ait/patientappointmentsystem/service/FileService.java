package de.ait.patientappointmentsystem.service;

import de.ait.patientappointmentsystem.model.FileEntity;
import de.ait.patientappointmentsystem.repositories.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@Slf4j
public class FileService {
    private final FileRepository fileRepository;

    @Value("${file.upload}")
    private String  uploadDir;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileEntity saveFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        String fileType = file.getContentType();
        try {
            byte[] data = file.getBytes();
            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(fileName);
            fileEntity.setFileType(fileType);
            fileEntity.setData(data);
            log.info("Saving file {}", fileEntity.getName());
            return fileRepository.save(fileEntity);
        } catch (IOException e) {
            log.error("Error while saving file {}", e.getMessage());
            return null;
        }
    }

    public Optional<FileEntity> getFileById(Long id) {
        log.info("Getting file by id {}", id);
        return fileRepository.findById(id);
    }

    public String saveFileOS(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Path destination = Paths.get(uploadDir).resolve(fileName);
        try {
            file.transferTo(destination);
            return fileName;
        }catch (IOException e) {
            log.error("Error while saving file {}", e.getMessage());
            return null;
        }
    }
}
