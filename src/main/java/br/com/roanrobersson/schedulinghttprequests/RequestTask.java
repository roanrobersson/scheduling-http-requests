package br.com.roanrobersson.schedulinghttprequests;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
public class RequestTask implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(RequestTask.class);

	@Value("${shr.responses.path}")
	private String responsesPath;

	@Autowired
	private HTMLRequestClient htmlRequestClient;

	@Override
	public void run() {
		String response = htmlRequestClient.get();
		InputStream inputStream = new ByteArrayInputStream(response.getBytes());
		File file = File.builder().name(generateFileName()).contentType("text/html").inputStream(inputStream).build();
		saveFile(file);
	}

	public void saveFile(File file) {
		Path filePath = generateFilePath(file.getName());
		try {
			FileCopyUtils.copy(file.getInputStream(), Files.newOutputStream(filePath));
		} catch (Exception e) {
			log.warn("Failed to save file {}", file.getName());
		}
	}

	private Path generateFilePath(String fileName) {
		return Path.of(new FileSystemResource("").getFile().getAbsolutePath(), responsesPath, fileName);
	}

	private String generateFileName() {
		return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString().replace(':', '-')
				.concat(".html");
	}
}
