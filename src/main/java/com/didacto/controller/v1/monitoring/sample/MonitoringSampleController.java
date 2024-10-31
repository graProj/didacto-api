package com.didacto.controller.v1.monitoring.sample;

import com.didacto.dto.monitoring.SampleImageResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Base64;

@RequestMapping("monitoring-sample")
@RestController
public class MonitoringSampleController {
    private static String FILE_NAME_01 = "cat";
    private static String FILE_PATH_01 = "src/main/resources/image/sample_image01.jpeg";
    private static String FILE_NAME_02 = "punch";
    private static String FILE_PATH_02 = "src/main/resources/image/sample_image02.jpeg";

    @GetMapping(value = "image-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SampleImageResponse> streamImages() {
        byte[] fileContent1;
        byte[] fileContent2;

        try {
            File file1 = new File(FILE_PATH_01);
            fileContent1 = new byte[(int) file1.length()];

            try (FileInputStream fis = new FileInputStream(file1)) {
                fis.read(fileContent1);
            }

            File file2 = new File(FILE_PATH_02);
            fileContent2 = new byte[(int) file2.length()];

            try (FileInputStream fis = new FileInputStream(file2)) {
                fis.read(fileContent2);
            }
        } catch (IOException e) {
            throw new RuntimeException("이미지 읽기 실패", e);
        }

        return Flux.interval(Duration.ofSeconds(1))
                .index()
                .map(tuple -> {
                    long index = tuple.getT1();
                    if (index % 2 == 0) {
                        return new SampleImageResponse(FILE_NAME_01, "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileContent1));
                    } else {
                        return new SampleImageResponse(FILE_NAME_02, "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileContent2));
                    }
                });
    }
}