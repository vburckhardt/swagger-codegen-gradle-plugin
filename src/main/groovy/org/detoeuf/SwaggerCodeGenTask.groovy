package org.detoeuf

import config.Config
import config.ConfigParser
import io.swagger.codegen.CliOption
import io.swagger.codegen.ClientOptInput
import io.swagger.codegen.ClientOpts
import io.swagger.codegen.CodegenConfig
import io.swagger.codegen.DefaultGenerator
import io.swagger.models.Swagger
import io.swagger.parser.SwaggerParser
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class SwaggerCodeGenTask extends DefaultTask {

    @TaskAction
    def swaggerCodeGen() {
        Swagger swagger = new SwaggerParser().read(project.file(project.swaggerInputSpec).absolutePath)
        CodegenConfig config = forName(project.swaggerLanguage)

        def outputDir = project.hasProperty('swaggerOutput') ? project.swaggerOutput : 'build/generated-sources/swagger'
        config.setOutputDir(project.file(outputDir).absolutePath)
        project.delete(outputDir)

        if (project.hasProperty('swaggerTemplateDirectory')) {
            config.additionalProperties().put("templateDir", project.file(project.swaggerTemplateDirectory).absolutePath)
        }
        if(project.hasProperty('swaggerApiPackage')){
            config.additionalProperties().put('apiPackage', project.swaggerApiPackage)
        }
        if(project.hasProperty('swaggerInvokerPackage')){
            config.additionalProperties().put('invokerPackage', project.swaggerInvokerPackage)
        }
        if(project.hasProperty('swaggerModelPackage')){
            config.additionalProperties().put('modelPackage', project.swaggerModelPackage)
        }

        if(project.hasProperty("swaggerLibrary")){
            config.additionalProperties().put('library', project.swaggerLibrary)
        }

        ClientOptInput input = new ClientOptInput().opts(new ClientOpts()).swagger(swagger)

        input.setConfig(config)

        def srcDir = project.hasProperty('swaggerSrc') ? project.swaggerSrc : 'src/swagger'
        project.delete(srcDir)
        new DefaultGenerator().opts(input).generate()
        project.copy {
            from outputDir+'/src/main/'+project.swaggerLanguage
            into srcDir+'/'+project.swaggerLanguage
        }
    }

    private static void applyConfigFileSettings(final CodegenConfig config, final File swaggerConfigFile) {
        Config genConfig = ConfigParser.read(swaggerConfigFile.absolutePath);
        if (null != genConfig) {
            for (CliOption langCliOption : config.cliOptions()) {
                if (genConfig.hasOption(langCliOption.getOpt())) {
                    config.additionalProperties().put(langCliOption.getOpt(), genConfig.getOption(langCliOption.getOpt()));
                }
            }
        }
    }

    private static CodegenConfig forName(String name) {
        ServiceLoader<CodegenConfig> loader = ServiceLoader.load(CodegenConfig.class)
        for (CodegenConfig config : loader) {
            if (config.getName().equals(name)) {
                return config
            }
        }

        // else try to load directly
        try {
            return (CodegenConfig) Class.forName(name).newInstance()
        } catch (Exception e) {
            throw new RuntimeException("Can't load config class with name ".concat(name), e)
        }
    }

}
