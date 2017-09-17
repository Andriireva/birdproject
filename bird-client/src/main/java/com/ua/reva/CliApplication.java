package com.ua.reva;

import org.apache.commons.cli.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CliApplication {

    private static final String DEFAULT_PORT = "3000";

    public static void main(String[] args) throws ParseException {
        //1. parse incoming arguments
        Option portOption = new Option(null, "serverPort", true, "port of server");
        portOption.setArgs(1);
        portOption.setOptionalArg(true);
        Options options = new Options();
        options.addOption(portOption);
        CommandLineParser cmdLinePosixParser = new DefaultParser();
        CommandLine commandLine = cmdLinePosixParser.parse(options, args);

        //2. Check that args contain "port" arg. If so override default port.
        String port = "";
        if (commandLine.hasOption("serverPort")) {
            port = commandLine.getOptionValue("serverPort");
        } else {
            port = DEFAULT_PORT;
        }

        //3. forward args for spring boot runner
        String[] bootArgs = new String[1];
        bootArgs[0] = "--bird.server.port=" + port;

        SpringApplication.run(CliApplication.class, bootArgs);
    }

    @Bean
    public RestTemplate birdClientRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(httpMessageConverter());
        return restTemplate;
    }

    @Bean
    public HttpMessageConverter httpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }
}
