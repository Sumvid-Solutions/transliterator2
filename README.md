# Transliterator2

Indian language transliteration tool originally created in 2009 using ICU4J. Current source code was recreated by decompiling the original JAR.

## Features

- Swing GUI for interactive transliteration
- File-based transliteration (UTF-16 encoded files)
- Clipboard transliteration - transliterate text directly from/to system clipboard
- Supports multiple Indian scripts: Telugu, Devanagari, Bengali, Kannada, Tamil, Malayalam, Gujarati, Gurmukhi, Oriya, and more

## Requirements

- Java 8 or higher

## Usage

Download the latest release JAR and run:

```
java -jar transliterator2-1.0.jar
```

## Building from Source

```
mvn clean package
java -jar target/transliterator2-1.0.jar
```

## Project Structure

- `org.dattapeetham.transliteration.ui.Transliterator` - Main Swing GUI
- `org.dattapeetham.transliteration.ICUHelper` - Core transliteration using ICU4J
- `org.dattapeetham.transliteration.FileTransliterator` - File-based transliteration
- `org.dattapeetham.transliteration.PreReplacements` - Script-specific character replacements
- `org.dattapeetham.transliteration.ui.ClipboardTransfer` - System clipboard utility
