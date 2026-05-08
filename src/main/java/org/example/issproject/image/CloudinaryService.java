package org.example.issproject.image;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class CloudinaryService {


    private Cloudinary cloudinary;


    @Transactional
    public CloudinaryResponse uploadFile(MultipartFile file,String fileName)
    {
        try
        {
            final Map result = cloudinary.uploader().upload(file.getBytes(),Map.of("public_id",fileName));
            final String url =  (String) result.get("secure_url");
            final String publicId= (String) result.get("public_id");
            return CloudinaryResponse.builder()
                    .publicId((String) result.get("public_id"))
                    .secureUrl((String) result.get("secure_url"))
                    .format((String) result.get("format"))
                    .width((Integer) result.get("width"))
                    .height((Integer) result.get("height"))
                    .build();
        }
        catch (Exception e)
        {
            log.error("Eroare la upload in Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file");
        }
    }

}
