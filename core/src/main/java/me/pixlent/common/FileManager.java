package me.pixlent.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class FileManager {
    /**
     * Returns the base path.
     *
     * @return The base path as a Path object.
     */
    @Contract(pure = true)
    public static @NotNull Path getBasePath() {
        return Path.of("");
    }

    /**
     * Searches a directory for files and returns a list of files
     *
     * @param directory The directory to search.
     * @return A list of File objects found in the directory.
     */
    public static List<File> searchDirectory(final Path directory) {
        List<File> files = new ArrayList<>();

        Guard.tryCatch("Failed to search directory", () -> {
            DirectoryStream<Path> stream = Files.newDirectoryStream(directory);

            for (Path file : stream) {
                files.add(file.toFile());
            }

            stream.close();
        });

        return files;
    }

    /**
     * Searches a directory and its subdirectories for files and returns a list of files.
     *
     * @param directory The directory to start the deep search from.
     * @return A list of File objects found in the directory and its subdirectories.
     */
    public static List<File> searchDirectoryDeep(final Path directory) {
        List<File> files = new ArrayList<>();

        Guard.tryCatch("Failed to search directory", () -> {
            DirectoryStream<Path> stream = Files.newDirectoryStream(directory);

            for (Path file : stream) {
                if (Files.isDirectory(file)) {
                    files.addAll(searchDirectoryDeep(file));
                } else {
                    files.add(file.toFile());
                }
            }

            stream.close();
        });

        return files;
    }

    /**
     * Saves a resource to a file if the file does not already exist.
     *
     * @param path     The path to the file where the resource will be saved.
     * @param resource The name of the resource to save, typically a file in the classpath.
     */
    public static void saveResourceIfNotExists(Path path, String resource) {
        if (Files.exists(path)) return;
        // Try to write the file, because it doesn't exist

        Guard.tryCatch("Failed to write file: " + path, () ->
                Files.write(path, Objects.requireNonNull(FileManager.class.getClassLoader().getResourceAsStream(resource)).readAllBytes(), StandardOpenOption.CREATE));
    }

    /**
     * Reads the contents of a file and returns them as a single string.
     *
     * @param file The file to read.
     * @return The contents of the file as a single string.
     * @throws RuntimeException If the file does not exist or cannot be read.
     */
    public static String readFile(File file) {
        final var content = new AtomicReference<byte[]>();

        Guard.tryCatch("", () -> content.set(Files.readAllBytes(file.toPath())));
        return new String(content.get(), StandardCharsets.UTF_8);
    }

    public static void writeFile(File file, String text) {
        Guard.tryCatch("Couldn't write to file " + file.getPath(), () -> {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
            writer.close();
        });
    }
}
