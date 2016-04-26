# Latera Activiti extension

This repository keeps sources of Activiti extension with some stuff needed for Hydra BSS and [Hydra Order Management System](https://github.com/latera/homs) (homs).

Requirements:
* [Groovy](http://www.groovy-lang.org/) 1.8.6+
* [Activiti](http://www.activiti.org/) 5.19.x
* Dependent jars:
  * [Apache Commons BeanUtils](http://commons.apache.org/proper/commons-beanutils/) 1.9.2
  * [Apache Commons Codec](https://commons.apache.org/proper/commons-codec/) 1.10
  * [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) 2.6
  * [Apache HttpClient](https://hc.apache.org/) 4.5
  * [Apache HttpCore](https://hc.apache.org/) 4.4.1
  * [Apache Xerces](http://xerces.apache.org/) 2.4.0
  * [Apache XML Resolver](http://xerces.apache.org/xml-commons/components/resolver/) 1.2
  * [EZMorph](http://ezmorph.sourceforge.net/) 1.0.6
  * [Groovy HTTPBuilder](https://github.com/jgritman/httpbuilder) 0.7.1
  * [Json-lib](http://json-lib.sourceforge.net/) 2.4
  * [NekoHTML](http://nekohtml.sourceforge.net/) 1.9.21
* [Apache Ant](http://ant.apache.org/)

## Compiling

1. Install Groovy 1.8.6+. Example for Debian Linux:

    ```bash
    sudo apt-get install groovy
    ```
2. Download [Activiti 5.19.x](http://activiti.org/download.html) and unzip it.
3. Unzip `wars/activiti-explorer.war`.
4. Download and put all dependent jars to `WEB-INF/lib` directory of _activiti-explorer_.
5. Download source code by git:

    ```bash
    git clone https://github.com/latera/activiti-ext.git
    ```
6. Compile extension jar via Apache Ant (may-be you need to edit `build.xml` before):

    ```bash
    ant
    ```

## Installation

1. [Download](https://github.com/latera/activiti-ext/releases) or compile extension jar.
2. Put extension jar and all dependent jars into `WEB-INF/lib` directory of _activiti-explorer_ and _activiti-rest_ applications.

## License

Copyright (c) 2016 Latera LLC under the [Apache License](https://github.com/latera/activiti-ext/blob/master/LICENSE).
