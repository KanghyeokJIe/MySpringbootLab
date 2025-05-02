package com.rookies3.MySpringbootLab.runner;

import com.rookies3.MySpringbootLab.property.MyPropProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements ApplicationRunner {
    @Value("${myprop.username}")
    private String name;

    @Value("${myprop.port}")
    private int port;

    @Autowired
    private MyPropProperties properties;

    private Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.debug("UserName : " + name);
        logger.debug("UserPort : " + port);

        logger.info("MyPropProperties getName() = " + properties.getUsername());
        logger.info("MyPropProperties getPort() = " + properties.getPort());
    }
}
