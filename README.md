# Continuous File Processor

## Motivation

This project is created by Idil Saglam for the HICX's interview process. All rights are reserved to the project's author

## Build

From project's root directory. Run the following command tp create an executable JAR archive
```bash
mvn clean pakcage
```

The executable jar will be created under `target` folder.

## Usage

You can either use the executable `JAR` file created in [build section](#build), or you can use the following command to build and run directly

```bash
mvn clean compile exec:java -Dexec.arguments="<source_folder_to_monitor>,[<destination_folder_path>]"
```

If you prefer to use the executable `JAR`, you can use the following command

```bash
java -jar target/hicx-test-1.0-SNAPSHOT.jar <source_folder_to_monitor> [<destination_folder_path>]
```

**IMPORTANT:** On the above notation, arguments surrounded by brackets (`[` and `]`) represents an optional argument. If this argument is missing processed files will be copied to a temporary folder.

## Test

To run all tests

```bash
mvn test
```
