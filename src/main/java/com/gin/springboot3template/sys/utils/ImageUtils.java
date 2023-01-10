package com.gin.springboot3template.sys.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 图片工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/10 11:11
 */
public class ImageUtils {
    /**
     * 图片转换为base64
     * @param bufferedImage 图片
     * @return base64字符串
     * @throws IOException 异常
     */
    public static String img2Base64(BufferedImage bufferedImage) throws IOException {
        final FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpeg", os);
        return Base64.encodeBase64String(os.toByteArray());
    }

}   
