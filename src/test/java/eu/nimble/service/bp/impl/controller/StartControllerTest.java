package eu.nimble.service.bp.impl.controller;

import com.google.gson.Gson;
import eu.nimble.service.bp.model.billOfMaterial.BillOfMaterial;
import eu.nimble.service.bp.model.billOfMaterial.BillOfMaterialItem;
import eu.nimble.service.bp.swagger.model.ProcessInstance;
import eu.nimble.service.bp.swagger.model.ProcessInstanceInputMessage;
import eu.nimble.service.bp.util.persistence.DataIntegratorUtil;
import eu.nimble.service.model.ubl.catalogue.CatalogueType;
import eu.nimble.service.model.ubl.commonbasiccomponents.QuantityType;
import eu.nimble.utility.JsonSerializationUtility;
import eu.nimble.utility.persistence.resource.EntityIdAwareRepositoryWrapper;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class StartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String orderJSON1 = "/controller/orderJSON1.txt";
    private final String orderJSON2 = "/controller/orderJSON2.txt";
    private final String orderJSON3 = "/controller/orderJSON3.txt";
    private final String iirJSON1 = "/controller/itemInformationRequestJSON1.txt";
    private static String exampleCataloguePath = "/controller/example_catalogue.json";

    public final static String orderId1 = "5b15c501-b90a-4f9c-ab0c-ca695e255237";
    public final static String iirId1 = "07ed85d2-3319-4dec-87f0-792d46a7c9a5";

    public static String processInstanceIdOrder1;
    public static String processInstanceIdOrder2;
    public static String processInstanceIdOrder3;
    public static String processInstanceIdIIR1;

    /**
     * Test scenario:
     * - Create 3 order and 1 item information request processes
     * - Create negotiations for multiple items
     */

    @BeforeClass
    public static void addCatalogue() throws IOException {
        String inputMessageAsString = IOUtils.toString(EPCControllerTest.class.getResourceAsStream(exampleCataloguePath));
        CatalogueType catalogue = JsonSerializationUtility.getObjectMapper().readValue(inputMessageAsString, CatalogueType.class);
        DataIntegratorUtil.checkExistingParties(catalogue);
        EntityIdAwareRepositoryWrapper repositoryWrapper = new EntityIdAwareRepositoryWrapper(catalogue.getProviderParty().getPartyIdentification().get(0).getID());
        repositoryWrapper.updateEntityForPersistCases(catalogue);
    }

    @Test
    public void test1_startProcessInstance() throws Exception {
        Gson gson = new Gson();
        String inputMessageAsString = IOUtils.toString(ProcessInstanceInputMessage.class.getResourceAsStream(orderJSON1));

        MockHttpServletRequestBuilder request = post("/start")
                .header("Authorization", TestConfig.initiatorPersonId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputMessageAsString);
        MvcResult mvcResult = this.mockMvc.perform(request).andDo(print()).andExpect(status().isOk()).andReturn();

        ProcessInstance processInstance = gson.fromJson(mvcResult.getResponse().getContentAsString(), ProcessInstance.class);
        Assert.assertEquals(processInstance.getStatus(), ProcessInstance.StatusEnum.STARTED);

        processInstanceIdOrder1 = processInstance.getProcessInstanceID();
    }

    @Test
    public void test2_startProcessInstance() throws Exception {
        Gson gson = new Gson();
        String inputMessageAsString = IOUtils.toString(ProcessInstanceInputMessage.class.getResourceAsStream(orderJSON2));

        MockHttpServletRequestBuilder request = post("/start")
                .header("Authorization", TestConfig.initiatorPersonId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputMessageAsString);
        MvcResult mvcResult = this.mockMvc.perform(request).andDo(print()).andExpect(status().isOk()).andReturn();

        ProcessInstance processInstance = gson.fromJson(mvcResult.getResponse().getContentAsString(), ProcessInstance.class);
        Assert.assertEquals(processInstance.getStatus(), ProcessInstance.StatusEnum.STARTED);

        processInstanceIdOrder2 = processInstance.getProcessInstanceID();
    }

    @Test
    public void test3_startProcessInstance() throws Exception {
        Gson gson = new Gson();
        String inputMessageAsString = IOUtils.toString(ProcessInstanceInputMessage.class.getResourceAsStream(orderJSON3));

        MockHttpServletRequestBuilder request = post("/start")
                .header("Authorization", TestConfig.initiatorPersonId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputMessageAsString);
        MvcResult mvcResult = this.mockMvc.perform(request).andDo(print()).andExpect(status().isOk()).andReturn();

        ProcessInstance processInstance = gson.fromJson(mvcResult.getResponse().getContentAsString(), ProcessInstance.class);
        Assert.assertEquals(processInstance.getStatus(), ProcessInstance.StatusEnum.STARTED);

        processInstanceIdOrder3 = processInstance.getProcessInstanceID();
    }

    @Test
    public void test4_startProcessInstance() throws Exception {
        Gson gson = new Gson();
        String inputMessageAsString = IOUtils.toString(ProcessInstanceInputMessage.class.getResourceAsStream(iirJSON1));

        MockHttpServletRequestBuilder request = post("/start")
                .header("Authorization", TestConfig.initiatorPersonId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputMessageAsString);
        MvcResult mvcResult = this.mockMvc.perform(request).andDo(print()).andExpect(status().isOk()).andReturn();

        ProcessInstance processInstance = gson.fromJson(mvcResult.getResponse().getContentAsString(), ProcessInstance.class);
        Assert.assertEquals(processInstance.getStatus(), ProcessInstance.StatusEnum.STARTED);

        processInstanceIdIIR1 = processInstance.getProcessInstanceID();
    }

    @Test
    public void test5_createNegotiationsForBOM() throws Exception {
        BillOfMaterial billOfMaterial = createBillOfMaterial();

        String body = JsonSerializationUtility.getObjectMapper().writeValueAsString(billOfMaterial);

        MockHttpServletRequestBuilder request = post("/start/billofmaterials")
                .header("Authorization", TestConfig.initiatorPersonId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        this.mockMvc.perform(request).andDo(print()).andExpect(status().isOk()).andReturn();
    }

    private BillOfMaterial createBillOfMaterial(){
        QuantityType quantity = new QuantityType();
        quantity.setValue(BigDecimal.valueOf(100));
        quantity.setUnitCode("items");

        BillOfMaterialItem billOfMaterialItem = new BillOfMaterialItem();
        billOfMaterialItem.setquantity(quantity);
        billOfMaterialItem.setcatalogueUuid("f0779966-a340-463d-9f04-2add5a6912f6");
        billOfMaterialItem.setlineId("bd52ae1c-6a80-4562-ab7c-70c5c8b7158e");

        BillOfMaterialItem billOfMaterialItem1 = new BillOfMaterialItem();
        billOfMaterialItem1.setquantity(quantity);
        billOfMaterialItem1.setcatalogueUuid("f0779966-a340-463d-9f04-2add5a6912f6");
        billOfMaterialItem1.setlineId("f450b263-8443-43c2-ae79-fbb08c4cc339");

        BillOfMaterial billOfMaterial = new BillOfMaterial();
        billOfMaterial.setBillOfMaterialItems(Arrays.asList(billOfMaterialItem,billOfMaterialItem1));

        return billOfMaterial;
    }
}
