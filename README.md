# SUPERCELL CSV-JSON Converter

A lightweight Java project for converting between CSV and JSON formats using a command-line interface (CLI).

## Features

- Convert **CSV files to JSON** with options for pretty formatting.
- Convert **JSON files to CSV** efficiently.
- Save output files in the same directory as the input file or a custom directory.
- Handles individual files for conversion.

## Prerequisites

- **Java 17** or higher
- **Maven** for building and running the project.

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/Roma4ka-RedBad/supercell-csv-json-tool.git
   cd supercell-csv-json-tool
   ```

2. Build the project with Maven:

   ```bash
   mvn clean package
   ```

3. Run the application:

   ```bash
   java -jar target/csv-json-tool-0.0.1.jar
   ```

## Usage

The project provides two primary commands:

### CSV to JSON

```bash
java -jar target/csv-json-tool-0.0.1.jar csv2json <input_filepath> [--output <output_filepath_or_directory>] [--pretty]
```

#### Example:

1. Convert `data.csv` to `data.json` in the same directory:

   ```bash
   java -jar target/csv-json-tool-0.0.1.jar csv2json data.csv
   ```

2. Convert `data.csv` to `output.json` with pretty formatting:

   ```bash
   java -jar target/csv-json-tool-0.0.1.jar csv2json data.csv --output output.json --pretty
   ```

### JSON to CSV

```bash
java -jar target/csv-json-tool-0.0.1.jar json2csv <input_filepath> [--output <output_filepath_or_directory>]
```

#### Example:

1. Convert `data.json` to `data.csv` in the same directory:

   ```bash
   java -jar target/csv-json-tool-0.0.1.jar json2csv data.json
   ```

2. Convert `data.json` to a specific directory:

   ```bash
   java -jar target/csv-json-tool-0.0.1.jar json2csv data.json --output /path/to/directory
   ```

## Project Structure

- `CSV.java`: Main class for converting CSV to JSON.
- `JSON.java`: Main class for converting JSON to CSV.
- `cli/`
  - `CSV2JSON.java`: CLI command for converting CSV to JSON (designed for end-users).
  - `JSON2CSV.java`: CLI command for converting JSON to CSV (designed for end-users).
  - `Main.java`: Entry point for running the CLI application.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Submit a pull request with a clear explanation of your changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

