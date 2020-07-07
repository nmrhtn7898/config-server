package com.example.springcloudconfigserver;

import com.example.springcloudconfigserver.ConfigServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ConfigServerApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Environment environment;

    /**
     * 깃 리포지토리 외부 설정 파일 로드 application-name=testservice, profile=default
     * @throws Exception
     */
    @Test
    public void getTestServiceDefault() throws Exception {
        String name = environment.getProperty("spring.cloud.config.server.git.uri") + "/testservice/testservice.yml";
        mockMvc.perform(get("/testservice/default"))
                .andExpect(jsonPath("$.name").value("testservice"))
                .andExpect(jsonPath("$.profiles[0]").value("default"))
                .andExpect(jsonPath("$.label").isEmpty())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.state").isEmpty())
                .andExpect(jsonPath("$.propertySources[0].name").value(name))
                .andExpect(jsonPath("$.propertySources[0].source.['name.firstname']").exists())
                .andExpect(jsonPath("$.propertySources[0].source.['name.lastname']").exists())
                .andExpect(jsonPath("$.propertySources[0].source.['name.password']").exists())
                .andExpect(jsonPath("$.propertySources[0].source.['name.message']").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 깃 리포지토리 외부 설정 파일 로드 application-name=testservice, profile=dev
     * @throws Exception
     */
    @Test
    public void getTestServiceDev() throws Exception {
        String gitUri = environment.getProperty("spring.cloud.config.server.git.uri");
        String defaultName = gitUri + "/testservice/testservice.yml";
        String devName = gitUri + "/testservice/testservice-dev.yml";
        mockMvc.perform(get("/testservice/dev"))
                .andExpect(jsonPath("$.name").value("testservice"))
                .andExpect(jsonPath("$.profiles[0]").value("dev"))
                .andExpect(jsonPath("$.label").isEmpty())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.state").isEmpty())
                .andExpect(jsonPath("$.propertySources[0].name").value(devName))
                .andExpect(jsonPath("$.propertySources[1].name").value(defaultName))
                .andExpect(jsonPath("$.propertySources[*].source.['name.firstname']").exists())
                .andExpect(jsonPath("$.propertySources[*].source.['name.lastname']").exists())
                .andExpect(jsonPath("$.propertySources[*].source.['name.password']").exists())
                .andExpect(jsonPath("$.propertySources[*].source.['name.message']").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 깃 리포지토리 외부 설정 파일 로드 application-name=testservice, profile=prod
     * @throws Exception
     */
    @Test
    public void getTestServiceProd() throws Exception {
        String gitUri = environment.getProperty("spring.cloud.config.server.git.uri");
        String defaultName = gitUri + "/testservice/testservice.yml";
        String prodName = gitUri + "/testservice/testservice-prod.yml";
        mockMvc.perform(get("/testservice/prod"))
                .andExpect(jsonPath("$.name").value("testservice"))
                .andExpect(jsonPath("$.profiles[0]").value("prod"))
                .andExpect(jsonPath("$.label").isEmpty())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.state").isEmpty())
                .andExpect(jsonPath("$.propertySources[0].name").value(prodName))
                .andExpect(jsonPath("$.propertySources[1].name").value(defaultName))
                .andExpect(jsonPath("$.propertySources[*].source.['name.firstname']").exists())
                .andExpect(jsonPath("$.propertySources[*].source.['name.lastname']").exists())
                .andExpect(jsonPath("$.propertySources[*].source.['name.password']").exists())
                .andExpect(jsonPath("$.propertySources[*].source.['name.message']").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }

}
