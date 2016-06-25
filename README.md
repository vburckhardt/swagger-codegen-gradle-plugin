swagger-codegen-gradle-plugin
============================

[![Build Status](https://travis-ci.org/thebignet/swagger-codegen-gradle-plugin.svg?branch=master)](https://travis-ci.org/thebignet/swagger-codegen-gradle-plugin)

A Gradle plugin to support the [swagger](http://swagger.io) code generation project

Usage
============================

see the [swagger-codegen-gradle-plugin-example](https://github.com/vorburger/swagger-codegen-gradle-plugin-example), or:

Here is an example of how to use the plugin in a `build.gradle` file
```groovy
plugins {
    id 'org.detoeuf.swagger-codegen' version '1.6.0'
    id 'java'
}

repositories {
    jcenter()
}

swagger {
    inputSpec = 'http://petstore.swagger.io/v2/swagger.json'

    output = 'build/swagger'
    language = 'spring-mvc'

    additionalProperties = [
            'invokerPackage'   : 'io.swagger.petstore.client.jersey1',
            'modelPackage'     : 'io.swagger.petstore.client.jersey1.model',
            'apiPackage'       : 'io.swagger.petstore.client.jersey1.api',
            'dateLibrary'      : 'java8'
    ]
    apis = ''
    models = ''
}

sourceSets {
    swagger {
        java {
            srcDir file("${project.buildDir.path}/swagger/src/main/java")
        }
    }
}

ext {
    jackson_version = '2.4.2'
    jersey_version = '1.18'
    jodatime_version = '2.3'
    junit_version = '4.8.1'
}

dependencies {
    swaggerCompile 'org.springframework.boot:spring-boot-starter-web'

    compile "com.sun.jersey:jersey-client:$jersey_version"
    compile "com.sun.jersey.contribs:jersey-multipart:$jersey_version"
    compile "com.fasterxml.jackson.core:jackson-core:$jackson_version"
    compile "com.fasterxml.jackson.core:jackson-annotations:$jackson_version"
    compile "com.fasterxml.jackson.core:jackson-databind:$jackson_version"
    compile "com.fasterxml.jackson.datatype:jackson-datatype-joda:2.1.5"
    compile "joda-time:joda-time:$jodatime_version"

    testCompile "junit:junit:$junit_version"
}
```

Launch with:

```
gradle swagger
```

### Configuration parameters

- `inputSpec` - swagger spec file path
- `output` - target output path (default is ${project.build.directory}/generated-sources/swagger)
- `language` - target generation language. Adapt sourceSet accordingly.
- `additionalProperties` - sets additional properties that can be referenced by the mustache templates in the format of name=value,name=value.  See [Customizing the generator](https://github.com/swagger-api/swagger-codegen/#customizing-the-generator) for list of parameters
- `models` - [selective generation](https://github.com/swagger-api/swagger-codegen/#selective-generation) of models.  Leave blank to generate models only
- `apis` - [selective generation](https://github.com/swagger-api/swagger-codegen/#selective-generation) of apis.  Leave blank to generate apis only
- `supportingFiles` - [selective generation](https://github.com/swagger-api/swagger-codegen/#selective-generation) of supporting files.  Leave blank to generate supporting files only
- `cleanOutputDir` - set if the output folders should be deleted (default is true)
