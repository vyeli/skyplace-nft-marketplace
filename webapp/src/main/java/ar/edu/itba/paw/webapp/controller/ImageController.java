package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static ar.edu.itba.paw.webapp.helpers.Utils.parseInt;

@Controller
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@PathVariable String id) {
        int parseId = parseInt(id);
        Image image = imageService.getImage(parseId).orElseThrow(ImageNotFoundException::new);
        return image.getImage();
    }
}
