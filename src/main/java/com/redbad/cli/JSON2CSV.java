package com.redbad.cli;

import com.redbad.JSON;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "json2csv", description = "Convert json to csv")
public class JSON2CSV implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Input filepath")
    Path inputPath;

    @CommandLine.Option(names = {"--output", "-o"}, description = "Output filepath or directory")
    Path outputPath;

    @Override
    public Integer call() throws Exception {
        if (!Files.exists(inputPath) || !Files.isRegularFile(inputPath)) {
            System.err.println("Invalid input file: " + inputPath);
            return 1;
        }

        String fileName = inputPath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        String nameWithoutExtension = (dotIndex > 0) ? fileName.substring(0, dotIndex) : fileName;

        Path finalOutputPath;

        if (Objects.isNull(outputPath)) {
            finalOutputPath = Objects.nonNull(inputPath.getParent())
                    ? inputPath.getParent().resolve(nameWithoutExtension + ".csv")
                    : Paths.get(nameWithoutExtension + ".csv");
        } else if (Files.isDirectory(outputPath)) {
            finalOutputPath = outputPath.resolve(nameWithoutExtension + ".csv");
        } else {
            finalOutputPath = outputPath;
        }

        JSON.toCSV(
                inputPath.toString(),
                finalOutputPath.toString()
        );

        System.out.println("Successfully saved as " + finalOutputPath.toAbsolutePath());

        return 0;
    }
}
