package eu.nimble.service.bp.impl;

import eu.nimble.service.bp.swagger.model.ModelApiResponse;
import eu.nimble.service.bp.swagger.model.ProcessPreferences;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * API tests for DefaultApi
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Ignore
public class PreferenceControllerTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Add a new partner business process sequence preference
     */
    @Test
    public void addBusinessProcessPartnerPreferenceTest() {
        ProcessPreferences body = TestObjectFactory.createProcessPreferences();
        String url = "http://localhost:" + port + "/preference";

        ResponseEntity<ModelApiResponse> response = restTemplate.postForEntity(url, body, ModelApiResponse.class);

        logger.info(" $$$ Test response {} ", response.toString());

        assertEquals(200, response.getBody().getCode().intValue());
    }

    /**
     * Get the business process preference of a partner
     */
    @Test
    public void getBusinessProcessPartnerPreferenceTest() {
        String partnerID = TestObjectFactory.getPartnerID();
        String url = "http://localhost:" + port +"/preference/{partnerID}";

        Map<String, String> params = new HashMap<String, String>();
        params.put("partnerID", partnerID);

        ResponseEntity<ProcessPreferences> response = restTemplate.getForEntity(url, ProcessPreferences.class, params);

        logger.info(" $$$ Test response {} ", response.toString());

        assertNotNull(response);
    }

    /**
     * Update the business process preference of a partner
     */
    @Test
    public void updateBusinessProcessPartnerPreferenceTest() {
        ProcessPreferences body = TestObjectFactory.updateProcessPreferences();
        restTemplate.put("http://localhost:" + port +"/preference", body);
    }

    /**
     * Deletes the business process preference of a partner
     */
    @Test
    public void z_deleteBusinessProcessPartnerPreferenceTest() {
        String partnerID = TestObjectFactory.getPartnerID();
        String url = "http://localhost:" + port + "/preference/{partnerID}";

        Map<String, String> params = new HashMap<String, String>();
        params.put("partnerID", partnerID);

        restTemplate.delete(url, params);
    }
}
