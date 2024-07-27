package com.mapquizzes.services.implemenations;

import com.mapquizzes.models.dto.PrincipalCertificateImageDto;
import com.mapquizzes.models.dto.PrincipalRankingDto;
import com.mapquizzes.services.interfaces.CertificateService;
import com.mapquizzes.services.interfaces.RankingService;
import com.mapquizzes.services.interfaces.UserImageService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final UserImageService imageService;
    private final RankingService rankingService;
    @Value("${map-quizzes.resources.certificate}")
    private String certificatePath; // original image from https://gas-kvas.com/risunki-fon/24466-fonovyj-risunok-dlja-sertifikata-50-foto.html
    @Value("${map-quizzes.resources.frame}")
    private String framePath;

    @Override
    public byte[] getCertificate(Principal principal) throws IOException {
        return new CertificateBuilder()
                .principal(principal)
                .ranking(rankingService.getRankingByPrincipal(principal).toList())
                .build();
    }

    private class CertificateBuilder {
        private Principal principal;
        private List<PrincipalRankingDto> ranking;
        private PDDocument document;
        private PDPageContentStream contentStream;
        private PDFont font;

        public CertificateBuilder principal(Principal principal) {
            if (principal == null) {
                throw new IllegalArgumentException("Principal cannot be null");
            }
            this.principal = principal;
            return this;
        }

        public CertificateBuilder ranking(List<PrincipalRankingDto> ranking) {
            if (ranking == null || ranking.isEmpty()) {
                throw new IllegalArgumentException("Ranking cannot be null or empty");
            }
            this.ranking = ranking;
            return this;
        }

        public byte[] build() throws IOException {
            try (PDDocument doc = Loader.loadPDF(ResourceUtils.getFile(certificatePath));
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                document = doc;
                PDPage page = document.getPages().get(0);
                contentStream = new PDPageContentStream(document, page,
                        PDPageContentStream.AppendMode.APPEND, true);
                font = new PDType1Font(Standard14Fonts.getMappedFontName("Times-Italic"));

                printUsername();
                printRanking();
                printTimestamp();
                printImage();

                contentStream.close();
                document.save(outputStream);
                return outputStream.toByteArray();
            }
        }

        private void printUsername() throws IOException {
            contentStream.beginText();
            contentStream.setFont(font, 18);
            contentStream.setLeading(15);
            contentStream.setNonStrokingColor(new Color(172, 135, 96));

            String username = principal.getName();
            float offset = calculateUsernameOffset(username);
            float x = 553 - username.length() * offset;
            float y = 309;

            contentStream.newLineAtOffset(x, y);
            contentStream.showText(username);
            contentStream.endText();
        }

        private float calculateUsernameOffset(String username) {
            float offset = 3.5f;
            if (username.matches(".*\\d.*")) {
                long digitCount = username.chars().filter(Character::isDigit).count();
                offset += 0.033f * digitCount;
            }
            return offset;
        }

        private void printRanking() throws IOException {
            float x = 470;
            float y = 230;
            for (int i = 0; i < ranking.size(); i++) {
                if (i > 0) {
                    y -= 30;
                }
                contentStream.beginText();
                contentStream.newLineAtOffset(x, y);
                PrincipalRankingDto dto = ranking.get(i);
                contentStream.showText(dto.quizName() + ": " + dto.rank());
                contentStream.endText();
            }
        }

        private void printTimestamp() throws IOException {
            contentStream.beginText();
            contentStream.setFont(font, 16);
            String timeStamp = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(Calendar.getInstance().getTime());
            contentStream.newLineAtOffset(500, 60);
            contentStream.showText(timeStamp);
            contentStream.endText();
        }

        private void printImage() throws IOException {
            PrincipalCertificateImageDto dto = imageService.getPrincipalCertificateImageDto(principal);
            byte[] croppedImage = ImageCropper.cropImage(dto.bytes(), dto.extension()).orElseThrow(() -> new RuntimeException("Cropped image is null"));
            BufferedImage profileBufferedImage = ImageIO.read(new ByteArrayInputStream(croppedImage));
            PDImageXObject profileImage = LosslessFactory.createFromImage(document, profileBufferedImage);
            PDImageXObject frame = PDImageXObject.createFromFileByExtension(ResourceUtils.getFile(framePath), document);

            contentStream.drawImage(profileImage, 130, 180, 200, 200);
            contentStream.drawImage(frame, 130, 180, 200, 200);
        }
    }

    private static class ImageCropper {
        private static final String DEFAULT_IMAGE_FORMAT = "png";

        public static Optional<byte[]> cropImage(byte[] imageBytes, String extension) throws IOException {
            if (imageBytes == null || imageBytes.length == 0) {
                throw new IllegalArgumentException("Image bytes cannot be null or empty");
            }

            String imageFormat = validateAndGetImageFormat(extension);

            try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
                BufferedImage originalImage = ImageIO.read(bais);
                if (originalImage == null) {
                    throw new IOException("Unable to read image from byte array");
                }

                BufferedImage croppedImage = cropToSquare(originalImage);

                return Optional.of(convertToByteArray(croppedImage, imageFormat));
            }
        }

        private static String validateAndGetImageFormat(String extension) {
            if (extension == null || extension.isBlank()) {
                return DEFAULT_IMAGE_FORMAT;
            }
            return extension.toLowerCase();
        }

        private static BufferedImage cropToSquare(BufferedImage image) {
            int width = image.getWidth();
            int height = image.getHeight();
            int size = Math.min(width, height);

            int x = (width - size) / 2;
            int y = (height - size) / 2;

            return image.getSubimage(x, y, size, size);
        }

        private static byte[] convertToByteArray(BufferedImage image, String format) throws IOException {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(image, format, baos);
                return baos.toByteArray();
            }
        }
    }
}
