package com.redbad.cli;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "csv-json-tool", mixinStandardHelpOptions = true, version = "v0.0.1",
        description = "This tool is designed to convert files from CSV to JSON and back.\nDeveloper: redbad",
        subcommands = {CSV2JSON.class, JSON2CSV.class})
public class Main implements Callable<Integer> {
    @Override
    public Integer call() {
        System.out.println("Supercell CSV-JSON Tool:");
        System.out.println("    -h, --help : Show help message.");
        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}