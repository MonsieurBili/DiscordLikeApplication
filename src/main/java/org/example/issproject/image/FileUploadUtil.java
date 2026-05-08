package org.example.issproject.image;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class FileUploadUtil {
    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif))$)";

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";

    public static final String FILE_NAME_FORMAT = "%s_%s_%s";

    public static boolean isAllowedExtension(final String fileName,final String pattern) {
        final Matcher matcher = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE).matcher(fileName);
        return matcher.matches();
    }


    public static void assertAllowedExtension(MultipartFile file, String pattern) {
        final long size = file.getSize();
        if (size > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File too large");
        }

        final String filename = file.getOriginalFilename();
        final String extension = FilenameUtils.getExtension(filename);


        if(!isAllowedExtension(file.getOriginalFilename(),pattern)) {
            throw new IllegalArgumentException("Wrong extension");
        }

    }


    public static String getFileName(final String name)
    {
        final DateFormat dateFormat= new SimpleDateFormat(DATE_FORMAT);
        final String date =  dateFormat.format(System.currentTimeMillis());
        return String.format(FILE_NAME_FORMAT,"avatar",name,date);
    }
}
