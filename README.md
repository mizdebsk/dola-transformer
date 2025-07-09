[![build status](https://img.shields.io/github/actions/workflow/status/mizdebsk/dola-transformer/ci.yml?branch=master)](https://github.com/mizdebsk/dola-transformer/actions/workflows/ci.yml?query=branch%3Amaster)
[![License](https://img.shields.io/github/license/mizdebsk/dola-transformer.svg?label=License)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central version](https://img.shields.io/maven-central/v/io.kojan/dola-transformer.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.kojan/dola-transformer)
![Fedora Rawhide version](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fmdapi.fedoraproject.org%2Frawhide%2Fpkg%2Fdola-transformer&query=%24.version&label=Fedora%20Rawhide)
[![Javadoc](https://javadoc.io/badge2/io.kojan/dola-transformer/javadoc.svg)](https://javadoc.io/doc/io.kojan/dola-transformer)

Dola Transformer
================

Maven 4 Extension for Dynamic Project Model Transformation

Dola Transformer is an extension for Apache Maven 4 that enables
dynamic, in-memory transformation of project models (POMs) without
modifying them on disk.  It supports a range of transformations,
including adding or removing plugins, dependencies, and parent POMs.

Unlike traditional POM modification tools from the Javapackages
project, Dola Transformer works with a variety of model formats and is
not limited to XML.  This makes it especially useful in environments
where custom Maven builds are needed without manually editing POM
files.

Usage: Run Maven as usual, but activate the extension by including
`-Dmaven.ext.class.path=/path/to/dola-transformer.jar`.
Then, specify your transformation instructions with:
`-Ddola.transformer.insn.optionalId.opcode=...`.

This is free software. You can redistribute and/or modify it under the
terms of Apache License Version 2.0.

This software was written by Mikolaj Izdebski.
