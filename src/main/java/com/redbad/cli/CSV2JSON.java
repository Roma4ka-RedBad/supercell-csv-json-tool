package com.redbad.cli;

import com.google.gson.FormattingStyle;
import com.redbad.CSV;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "csv2json", description = "Convert csv to json")
public class CSV2JSON implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "Input filepath")
    Path inputPath;

    @CommandLine.Option(names = {"--pretty", "-p"}, description = "Make json prettier")
    Boolean pretty = false;

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
                    ? inputPath.getParent().resolve(nameWithoutExtension + ".json")
                    : Paths.get(nameWithoutExtension + ".json");
        } else if (Files.isDirectory(outputPath)) {
            finalOutputPath = outputPath.resolve(nameWithoutExtension + ".json");
        } else {
            finalOutputPath = outputPath;
        }

        CSV.toJSON(
                inputPath.toString(),
                finalOutputPath.toString(),
                pretty ? FormattingStyle.PRETTY : FormattingStyle.COMPACT
        );

        System.out.println("Successfully saved as " + finalOutputPath.toAbsolutePath());

        return 0;
    }
}

