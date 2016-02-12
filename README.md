swagger-codegen-gradle-plugin
============================

[![Build Status](https://travis-ci.org/thebignet/swagger-codegen-gradle-plugin.svg?branch=master)](https://travis-ci.org/thebignet/swagger-codegen-gradle-plugin)

A Gradle plugin to support the [swagger](http://swagger.io) code generation project

Usage
============================

Add to your `build.gradle` the following
```groovy
ext {
    swaggerInputSpec = 'src/main/resources/petstore.yaml'
    swaggerOutput = 'src/swagger'
    swaggerLanguage = 'java'
}

apply plugin: 'org.detoeuf.swagger-code-gen'

sourceSets {
    swagger {
        java.srcDir file('src/swagger/java')
    }
}
```

Launch with:

```
gradle swagger
```

### Configuration parameters

- `swaggerInputSpec` - swagger spec file path
- `swaggerLanguage` - target generation language. Adapt sourceSet accordingly.
- `swaggerTemplateDirectory` - directory with mustache templates
- `swaggerApiPackage` - package for default API
- `swaggerInvokerPackage` - package for invoker
- `swaggerModelPackage` - package for models
- `swaggerOutput` - target output path (default is ${project.build.directory}/generated-sources/swagger)
- `swaggerSrc` - target source directory (default is ${project.root.directory}/src/swagger).  This directory should be added as as source set
- `swaggerLibrary` -  library template (sub-template) to use
