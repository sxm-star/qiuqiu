package com.songxm.qiuqiu.manage.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.songxm.qiuqiu.common.bean.PicUploadResult;
import com.songxm.qiuqiu.manage.service.component.PropertiesService;

/**
 * 图片上传
 * 
 */
@Controller
@RequestMapping("pic")
public class PicUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PicUploadController.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    // 获取env.properties里面的配置信息
    // @Value("${REPOSITORY_PATH}")
    // private String REPOSITORY_PATH;

    // 获取env.properties里面的配置信息
    // @Value("${IMAGE_BASE_URL}")
    // private String IMAGE_BASE_URL;

    @Autowired
    private PropertiesService propertiesService;

    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[] { ".bmp", ".jpg", ".jpeg", ".gif", ".png" };

    // 需要返回的响应是text/html,所以需要增加produces= MediaType.TEXT_HTML_VALUE
    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String upload(@RequestParam("uploadFile") MultipartFile uploadFile, HttpServletResponse response)
            throws Exception {

        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }

        // 封装Result对象
        PicUploadResult fileUploadResult = new PicUploadResult();

        // 状态 如果上传成功error为0 如果失败，error为1
        fileUploadResult.setError(isLegal ? 0 : 1);

        // 文件新路径D:\\0707\\taotao-upload\\images\\2015\\11\\01\\20151101095911001123.jpg
        String filePath = getFilePath(uploadFile.getOriginalFilename());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pic file upload .[{}] to [{}] .", uploadFile.getOriginalFilename(), filePath);
        }

        // 生成图片的绝对引用地址 \\images\\2015\\11\\01\\20151101095911001123.jpg
        // 生成图片的绝对引用地址 /images/2015/11/01/20151101095911001123.jpg
        String picUrl = StringUtils.replace(StringUtils.substringAfter(filePath, this.propertiesService.REPOSITORY_PATH), "\\",
                "/");
        // 生成图片的绝对引用地址 http://image.taotao.com/images/2015/11/01/20151101095911001123.jpg
        fileUploadResult.setUrl(this.propertiesService.IMAGE_BASE_URL + picUrl);

        File newFile = new File(filePath);

        // 写文件到磁盘
        uploadFile.transferTo(newFile);

        // 校验图片是否合法
        isLegal = false;
        try {
            BufferedImage image = ImageIO.read(newFile);
            if (image != null) {
                fileUploadResult.setWidth(image.getWidth() + "");
                fileUploadResult.setHeight(image.getHeight() + "");
                isLegal = true;
            }
        } catch (IOException e) {
        }

        // 状态
        fileUploadResult.setError(isLegal ? 0 : 1);

        if (!isLegal) {
            // 不合法，将磁盘上的文件删除
            newFile.delete();
        }

        // response.setContentType(MediaType.TEXT_HTML_VALUE);
        // 把对象序列化为json字符串
        return mapper.writeValueAsString(fileUploadResult);
    }

    // D:\\0707\\taotao-upload\\images\\2015\\11\\01\\20151101095911001123.jpg
    private String getFilePath(String sourceFileName) {
        // File.separator跨系统的文件分隔符
        String baseFolder = this.propertiesService.REPOSITORY_PATH + File.separator + "images";
        Date nowDate = new Date();
        String fileFolder = baseFolder + File.separator + new DateTime(nowDate).toString("yyyy")
                + File.separator + new DateTime(nowDate).toString("MM") + File.separator
                + new DateTime(nowDate).toString("dd");
        File file = new File(fileFolder);
        if (!file.isDirectory()) {
            // 如果目录不存在，则创建目录
            file.mkdirs();
        }
        // 生成新的文件名
        String fileName = new DateTime(nowDate).toString("yyyyMMddhhmmssSSSS")
                + RandomUtils.nextInt(100, 9999) + "." + StringUtils.substringAfterLast(sourceFileName, ".");
        return fileFolder + File.separator + fileName;
    }

}
