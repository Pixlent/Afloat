package me.pixlent.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;

/**
 * A utility class to clean up exceptions
 */
public class Guard {
    private static final Logger logger = LoggerFactory.getLogger(Guard.class);

    /**
     * A utility method to cleanly do try catch without the ugliness that comes with it
     *
     * @param error The error message that will display in console if an error happens to occur
     * @param runnable The code you want to try and catch
     */
    public static void tryCatch(String error, ExceptionalRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            handleException(e, error);
        }
    }

    private static void handleException(Exception e, String error) {
        logger.error(error + "\n" + e.getMessage());
        saveLogFile(e);
    }

    private static void saveLogFile(Exception e) {
        final var log = FileManager.getBasePath().resolve("logs/" + Date.from(Instant.now()) + ".txt").toFile();

        if (log.mkdirs()) logger.error("Couldn't create directories for " + log.getPath());

        FileManager.writeFile(log, e.getMessage());
    }

    @FunctionalInterface
    public interface ExceptionalRunnable {
        void run() throws Exception;
    }
}