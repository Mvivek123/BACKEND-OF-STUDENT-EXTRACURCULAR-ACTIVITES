package com.klu.demoprojrect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class CampusConnectBackendApplication {

	private static final Logger logger = LoggerFactory.getLogger(CampusConnectBackendApplication.class);
	private static FileLock processLock;
	private static FileChannel lockChannel;
	private static FileOutputStream lockStream;

	public static void main(String[] args) {
		// Acquire a single-instance file lock in the system temp directory
		String tmpDir = System.getProperty("java.io.tmpdir");
		File lockFile = new File(tmpDir, "campus-connect-backend.lock");
		try {
			lockStream = new FileOutputStream(lockFile);
			lockChannel = lockStream.getChannel();
			processLock = lockChannel.tryLock();
			if (processLock == null) {
				logger.error("Another instance appears to be running (could not acquire lock on {}), exiting.", lockFile.getAbsolutePath());
				System.exit(1);
			}
			// Ensure lock file is deleted on exit
			lockFile.deleteOnExit();
		} catch (IOException e) {
			logger.warn("Could not create lock file {}; proceeding anyway. Error: {}", lockFile.getAbsolutePath(), e.getMessage());
		}

		SpringApplication app = new SpringApplication(CampusConnectBackendApplication.class);

		// If server.port not set via env/arg/properties, default to 0 (random free port)
		Map<String, Object> defaults = new HashMap<>();
		defaults.put("server.port", 50507);
		app.setDefaultProperties(defaults);

		// Listener to log the actual port once the app is ready
		app.addListeners((ApplicationListener<ApplicationReadyEvent>) event -> {
			String port = event.getApplicationContext().getEnvironment().getProperty("local.server.port");
			logger.info("Application started on port: {}", port);
		});

		app.run(args);
	}

	// Optional: release lock when the JVM shuts down
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				if (processLock != null) {
					processLock.release();
				}
				if (lockChannel != null) {
					lockChannel.close();
				}
				if (lockStream != null) {
					lockStream.close();
				}
			} catch (IOException e) {
				// nothing much we can do on shutdown
			}
		}));
	}

	// Global CORS filter allowing all origins (suitable for development)
	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", config);
		return new CorsFilter(source);
	}

}