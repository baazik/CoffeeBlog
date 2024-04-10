package cz.hocuspocus.coffeeblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/")
public class ImagesController {

    @Autowired
    private CacheManager cacheManager;

    @PostMapping("uploadImage")
    public ResponseEntity<String> handleImageUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Zpracování nahrávání obrázku a uložení do fyzické složky
            String relativePath = "uploads/" + file.getOriginalFilename();
            Path path = Paths.get("src/main/resources/static", relativePath);

            // Vytvoření složky, pokud neexistuje
            Files.createDirectories(path.getParent());

            // Zápis souboru
            Files.write(path, file.getBytes());

            // Mazání cache
            evictAllCaches();

            // Vráťte JSON s URL k nově nahrávanému obrázku
            String imageUrl = relativePath;
            String jsonResponse = "{ \"location\": \"" + imageUrl + "\" }";
            System.out.println("Image uploaded.");
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(jsonResponse);
        } catch (Exception e) {
            // Zalogování detailů výjimky
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Chyba při zpracování obrázku");
        }
    }

    // Metoda pro mazání cache
    @CacheEvict(allEntries = true, value = {"yourCacheName"})
    public void evictAllCaches() {
        for (String name : cacheManager.getCacheNames()) {
            cacheManager.getCache(name).clear();
        }
    }

    @GetMapping({"/uploads/{imageName:.+}", "/recipes/uploads/{imageName:.+}", "/articles/uploads/{imageName:.+}"})
    @ResponseBody
    @CacheEvict(allEntries = true, value = {"yourCacheName"})
    public ResponseEntity<Resource> serveFile(@PathVariable String imageName) {
        try {

            evictAllCaches();
            Path imagePath = Paths.get("src/main/resources/static/uploads", imageName);
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
