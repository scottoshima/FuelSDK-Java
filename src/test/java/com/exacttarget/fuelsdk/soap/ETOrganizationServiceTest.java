//
// ETOrganizationServiceTest.java -
//
//      x
//
// Copyright (C) 2013 ExactTarget
//
// @COPYRIGHT@
//

package com.exacttarget.fuelsdk.soap;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.exacttarget.fuelsdk.ETClient;
import com.exacttarget.fuelsdk.ETConfiguration;
import com.exacttarget.fuelsdk.ETOrganizationService;
import com.exacttarget.fuelsdk.ETSdkException;
import com.exacttarget.fuelsdk.ETServiceResponse;
import com.exacttarget.fuelsdk.filter.ETFilter;
import com.exacttarget.fuelsdk.filter.ETFilterOperators;
import com.exacttarget.fuelsdk.filter.ETSimpleFilter;
import com.exacttarget.fuelsdk.model.ETAccountType;
import com.exacttarget.fuelsdk.model.ETOrganization;

@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ETOrganizationServiceTest {

	protected static Logger logger = Logger.getLogger(ETOrganizationServiceTest.class);
	protected static ETOrganizationService service;
	protected static ETClient client = null;
	protected static ETConfiguration configuration = null;
	
	private String CustomerKeyofExistingOrganization = "F5D63DE7-A421-4C28-AE44-6E9B524ADB50";
	private String CustomerKeyOfTestOrganization = "TestOrganizationCustomerKey::Key";
	private String NameOfTestOrganization = "TestOrganizationName";
	
	@BeforeClass
	public static void setUp() throws ETSdkException {
		logger.debug("SetUp");
		configuration = new ETConfiguration("/fuelsdk-test.properties");
        client = new ETClient(configuration);
		
		service = new ETOrganizationServiceImpl();
	}
	
	
	@Test
	public void A_TestGet() throws ETSdkException {
		
		ETServiceResponse<ETOrganization> response = service.get(client);
		
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getStatus());
		Assert.assertNotNull(response.getResults());
		
		for(ETOrganization org : response.getResults()) {
			
			logger.debug(org.toString());
		}
	}
	
	
	@Test
	public void B_TestGetSingle() throws ETSdkException {
		
		ETFilter filter = new ETSimpleFilter("CustomerKey", ETFilterOperators.EQUALS, CustomerKeyofExistingOrganization);
		ETServiceResponse<ETOrganization> response = service.get(client, filter);
		
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getStatus());
		Assert.assertNotNull(response.getResults());
		Assert.assertEquals(1, response.getResults().size());
		
		for(ETOrganization org : response.getResults()) {
			
			logger.debug(org.toString());
		}
	}
	

	@Test
	public void C_TestPost() throws ETSdkException {
		
		ETOrganization org = new ETOrganization();
		org.setCustomerKey(CustomerKeyOfTestOrganization);
		org.setName(NameOfTestOrganization);
		org.setAccountType(ETAccountType.PRO_CONNECT);
		org.setEmail("test@organization.com");
		org.setFromName("AGENCY CLIENT");
		org.setBusinessName("Test Organization");
		org.setAddress("123 ABC Street");
		org.setCity("Indianapolis");
		org.setState("IN");
		org.setZip("46202");
		org.setIsTestAccount(true);
		org.setEditionId(3);
		org.setIsActive(true);
		
		ETServiceResponse<ETOrganization> response = service.post(client, org);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getStatus());
		
		// Test it was created
		ETFilter filter = new ETSimpleFilter("CustomerKey", ETFilterOperators.EQUALS, CustomerKeyOfTestOrganization);
		ETServiceResponse<ETOrganization> responseFound = service.get(client, filter);
		
		Assert.assertNotNull(responseFound);
		Assert.assertTrue(responseFound.getStatus());
		Assert.assertNotNull(responseFound.getResults());
		Assert.assertEquals(1, responseFound.getResults().size());
		
		for(ETOrganization orgFound : responseFound.getResults()) {
			
			logger.debug(orgFound.toString());
		}
	}

	@Test
	public void D_TestPatch() throws ETSdkException {
		
		ETOrganization org = new ETOrganization();
		org.setCustomerKey(CustomerKeyOfTestOrganization);
		org.setName("New TestOrganizationName");
		org.setAccountType(ETAccountType.PRO_CONNECT);
		org.setEmail("test@organization.com");
		org.setFromName("AGENCY CLIENT");
		org.setBusinessName("Test Organization");
		org.setAddress("123 ABC Street");
		org.setCity("Indianapolis");
		org.setState("IN");
		org.setZip("46202");
		org.setIsTestAccount(true);
		org.setEditionId(3);
		org.setIsActive(true);
		
		ETServiceResponse<ETOrganization> response = service.patch(client, org);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getStatus());
		
		// Test it was updated
		ETFilter filter = new ETSimpleFilter("CustomerKey", ETFilterOperators.EQUALS, CustomerKeyOfTestOrganization);
		ETServiceResponse<ETOrganization> responseFound = service.get(client, filter);
		
		Assert.assertNotNull(responseFound);
		Assert.assertTrue(responseFound.getStatus());
		Assert.assertNotNull(responseFound.getResults());
		Assert.assertEquals(1, responseFound.getResults().size());
		Assert.assertEquals("New TestOrganizationName", responseFound.getResults().get(0).getName());
		
	}
	
	@Test
	public void E_TestDelete() throws ETSdkException {
		
		ETOrganization organization = new ETOrganization();
		organization.setCustomerKey(CustomerKeyOfTestOrganization);
		
		ETServiceResponse<ETOrganization> response = service.delete(client, organization);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getStatus());
		
		// Test it was deleted
		ETFilter filter = new ETSimpleFilter("CustomerKey", ETFilterOperators.EQUALS, CustomerKeyOfTestOrganization);
		ETServiceResponse<ETOrganization> responseFound = service.get(client, filter);
		
		Assert.assertNotNull(responseFound);
		Assert.assertTrue(responseFound.getStatus());
		Assert.assertNotNull(responseFound.getResults());
		Assert.assertEquals(0, responseFound.getResults().size());
	}
	
}
