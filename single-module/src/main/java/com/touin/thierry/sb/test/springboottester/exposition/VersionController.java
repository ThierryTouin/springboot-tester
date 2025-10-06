package com.touin.thierry.sb.test.springboottester.exposition;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

    @Value("${appli.version}")
    private String version;


    @GetMapping(value="/version", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Version hello() {
        return new Version(version);
    }


    class Version {
        private String version;

        public Version(String version) {
            this.version = version;
        }

        public String getversion() {
            return this.version;
        }
    }

}
