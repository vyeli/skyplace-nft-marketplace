package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ImageService {

    Image createImage(MultipartFile image);

    Optional<Image> getImage(int id);
}
