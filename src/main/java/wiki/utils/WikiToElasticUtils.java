/**
 * @author  Alon Eirew
 */

package wiki.utils;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.util.fst.IntsRefFSTEnum;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WikiToElasticUtils {

    private final static Logger LOGGER = LogManager.getLogger(WikiToElasticUtils.class);

    public static InputStream openCompressedFileInputStream(String fileIn) throws IOException {
        LOGGER.debug("opening compressed input stream");
        FileInputStream fin = new FileInputStream(fileIn);
        boolean multiStream = fileIn.contains("multistream");
        return new BZip2CompressorInputStream(fin, multiStream);
    }

    public static void closeCompressedFileInputStream(InputStream is) throws IOException {
        if(is != null) {
            LOGGER.debug("closing compressed input stream");
            is.close();
        }
    }

    public static String getFileContent(String fileName) {
        String fileContent = null;
        try {
            if(fileName != null) {
                URI resource = WikiToElasticUtils.class.getClassLoader().getResource(fileName).toURI();
                fileContent = IOUtils.toString(resource, StandardCharsets.UTF_8);
            }
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Failed loading file-" + fileName, e);
        }

        return fileContent;
    }
}
