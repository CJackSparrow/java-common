package com.github.icovn.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.makers.FixedSizeThumbnailMaker;
import org.imgscalr.Scalr;

/** Created by ico on 5/10/2017. */
@Slf4j
public class ImageUtil {

  public static void generateThumb(
      String inputFile, String outputFile, int x, int y, int width, int height) {
    try {
      Image src = ImageIO.read(new File(inputFile));
      BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      dst.getGraphics().drawImage(src, 0, 0, width, height, x, y, x + width, y + height, null);

      ImageIO.write(dst, FileUtil.getExtensionWithoutDot(inputFile), new File(outputFile));
    } catch (Exception ex) {
      log.error(ExceptionUtil.getFullStackTrace(ex));
    }
  }

  public static void generateThumb(String inputFile, String outputFile, int width, int height) {
    try {
      Thumbnails.of(inputFile).size(width, height).toFile(outputFile);
    } catch (Exception ex) {
      log.error(ExceptionUtil.getFullStackTrace(ex));
    }
  }

  public static void generateFixSize(String inputFile, String outputFile, int width, int height) {
    try {
      BufferedImage originalImage = ImageIO.read(new File(inputFile));

      BufferedImage thumbnail =
          new FixedSizeThumbnailMaker().size(width, height).make(originalImage);

      ImageIO.write(thumbnail, FileUtil.getExtensionWithoutDot(inputFile), new File(outputFile));
    } catch (Exception ex) {
      log.error(ExceptionUtil.getFullStackTrace(ex));
    }
  }

  public static void generateFixSize2(String inputFile, String outputFile, int width, int height) {
    try {
      BufferedImage originalImage = ImageIO.read(new File(inputFile));
      BufferedImage thumbImg =
          Scalr.resize(
              originalImage,
              Scalr.Method.QUALITY,
              Scalr.Mode.FIT_EXACT,
              width,
              height,
              Scalr.OP_ANTIALIAS);
      ImageIO.write(thumbImg, FileUtil.getExtensionWithoutDot(inputFile), new File(outputFile));
    } catch (Exception ex) {
      log.error(ExceptionUtil.getFullStackTrace(ex));
    }
  }
}
