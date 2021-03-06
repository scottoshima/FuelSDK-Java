//
// ETGetServiceImpl.java -
//
//      x
//
// Copyright (C) 2013 ExactTarget
//
// @COPYRIGHT@
//

package com.exacttarget.fuelsdk.soap;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;

import com.exacttarget.fuelsdk.ETClient;
import com.exacttarget.fuelsdk.ETGetService;
import com.exacttarget.fuelsdk.ETSdkException;
import com.exacttarget.fuelsdk.ETServiceResponse;
import com.exacttarget.fuelsdk.annotations.InternalSoapType;
import com.exacttarget.fuelsdk.filter.ETComplexFilter;
import com.exacttarget.fuelsdk.filter.ETFilter;
import com.exacttarget.fuelsdk.filter.ETSimpleFilter;
import com.exacttarget.fuelsdk.internal.APIObject;
import com.exacttarget.fuelsdk.internal.ComplexFilterPart;
import com.exacttarget.fuelsdk.internal.FilterPart;
import com.exacttarget.fuelsdk.internal.LogicalOperators;
import com.exacttarget.fuelsdk.internal.RetrieveRequest;
import com.exacttarget.fuelsdk.internal.RetrieveRequestMsg;
import com.exacttarget.fuelsdk.internal.RetrieveResponseMsg;
import com.exacttarget.fuelsdk.internal.SimpleFilterPart;
import com.exacttarget.fuelsdk.internal.SimpleOperators;
import com.exacttarget.fuelsdk.internal.Soap;
import com.exacttarget.fuelsdk.model.ETObject;
import com.exacttarget.fuelsdk.model.converter.ObjectConverter;

public abstract class ETGetServiceImpl<T extends ETObject> extends ETServiceImpl implements ETGetService {

	private static Logger logger = Logger.getLogger(ETGetServiceImpl.class);

    protected ETServiceResponse<T> get(ETClient client, Class<T> type) throws ETSdkException {
        return this.get(client, type, null);
    }

    protected ETServiceResponse<T> get(ETClient client, Class<T> type, ETFilter filter) throws ETSdkException {
    	    	
		Soap soap = client.getSOAPConnection().getSoap();

        InternalSoapType typeAnnotation = type.getAnnotation(InternalSoapType.class);
        if(typeAnnotation == null) {
        	logger.error("The type specified does not wrap an internal ET APIObject.");
            throw new ETSdkException("The type specified does not wrap an internal ET APIObject.");
        }

        RetrieveRequest retrieveRequest = new RetrieveRequest();
        try {
            retrieveRequest.setObjectType(typeAnnotation.type().getSimpleName());
            retrieveRequest.getProperties().addAll(ObjectConverter.findSerializablePropertyNames(type));
        }
        catch(Exception e) {
        	logger.error("Error inspecting serialization properties of specified type", e);
            throw new ETSdkException("Error inspecting serialization properties of specified type", e);
        }

        if (filter != null) {
        	FilterPart filterPart = convertFilterPart(filter);
            retrieveRequest.setFilter(filterPart);
        }

        RetrieveRequestMsg retrieveRequestMsg = new RetrieveRequestMsg();
        retrieveRequestMsg.setRetrieveRequest(retrieveRequest);

        RetrieveResponseMsg retrieveResponseMsg = soap.retrieve(retrieveRequestMsg);

        ETServiceResponse<T> response = new ETServiceResponseImpl<T>();
        response.setRequestId(retrieveResponseMsg.getRequestID());

        response.setStatus(retrieveResponseMsg.getOverallStatus().equals("OK"));
        response.setMessage(retrieveResponseMsg.getOverallStatus());

        try {
            for (APIObject apiObject : retrieveResponseMsg.getResults()) {
                response.getResults().add(ObjectConverter.convertToEtObject(apiObject, type, false));
            }
        }
        catch (Exception ex) {
        	logger.error("Error instantiating object", ex);
            throw new ETSdkException("Error instantiating object", ex);
        }

        return response;
	}

	protected FilterPart convertFilterPart(ETFilter filter) throws ETSdkException {
		FilterPart filterPart = null;
		if (filter instanceof ETSimpleFilter) {
			filterPart = new SimpleFilterPart();
			((SimpleFilterPart)filterPart).setProperty(((ETSimpleFilter) filter).getProperty());
			((SimpleFilterPart)filterPart).setSimpleOperator(SimpleOperators.valueOf(((ETSimpleFilter) filter).getOperator().toString()));
			((SimpleFilterPart)filterPart).getValue().addAll(((ETSimpleFilter) filter).getValues());
			if (null != ((ETSimpleFilter) filter).getDateValues()) {
				for(Date d : ((ETSimpleFilter) filter).getDateValues()) {
					GregorianCalendar gregorian = new GregorianCalendar();
					gregorian.setTime(d);
					try {
						((SimpleFilterPart)filterPart).getDateValue().add(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorian));
					} catch (DatatypeConfigurationException e) {
						throw new ETSdkException("Error Converting FilterPart object", e);
					}
				}
			}
		} else if (filter instanceof ETComplexFilter) {
			filterPart = new ComplexFilterPart();
			((ComplexFilterPart)filterPart).setLeftOperand(convertFilterPart(((ETComplexFilter) filter).getLeftOperand()));
			((ComplexFilterPart)filterPart).setRightOperand(convertFilterPart(((ETComplexFilter) filter).getRightOperand()));
			if (null != ((ETComplexFilter)filter).getAdditionalOperands()) {
				for(ETFilter additional : ((ETComplexFilter)filter).getAdditionalOperands()) {
					((ComplexFilterPart)filterPart).getAdditionalOperands().getOperand().add(convertFilterPart(additional));
				}
			}
			((ComplexFilterPart)filterPart).setLogicalOperator(LogicalOperators.valueOf(((ETComplexFilter) filter).getOperator().toString()));
		}
		return filterPart;
	}
}
