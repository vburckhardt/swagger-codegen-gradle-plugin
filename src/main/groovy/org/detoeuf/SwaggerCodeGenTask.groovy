package org.detoeuf

import io.swagger.codegen.DefaultGenerator
import io.swagger.codegen.config.CodegenConfigurator
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class SwaggerCodeGenTask extends DefaultTask {

    boolean cleanOutputDir = true

    @Input
    CodegenConfigurator configurator() {
        return project.extensions.findByName('swagger') as CodegenConfigurator
    }

    @OutputDirectory
    File outputDir() {
        return project.file(configurator().outputDir)
    }

    private deleteFileValidator(File against) {
        if (outputDir() == against) {
            throw new GradleException("You probably don't want to overwrite this directory: $against")
        }
    }

    @TaskAction
    void swaggerCodeGen() {
        deleteFileValidator(project.projectDir)
        deleteFileValidator(project.rootProject.projectDir)
        CodegenConfigurator config = configurator()

        // anyway .. delete any existing files for a clean build
        if (cleanOutputDir) {
            project.delete(outputDir())
        }

        new DefaultGenerator()
                .opts(config.toClientOptInput())
                .generate()
    }
}
