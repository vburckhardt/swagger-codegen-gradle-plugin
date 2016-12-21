package org.detoeuf

import io.swagger.codegen.DefaultGenerator
import io.swagger.codegen.config.CodegenConfigurator
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class SwaggerCodeGenTask extends DefaultTask {

    @TaskAction
    def swaggerCodeGen() {
        def config = project.extensions.findByName('swagger').asType(CodegenConfigurator.class)

        // anyway .. delete any existing files for a clean build
        project.delete(config.getOutputDir())

        new DefaultGenerator()
                .opts(config.toClientOptInput())
                .generate()
    }
}
