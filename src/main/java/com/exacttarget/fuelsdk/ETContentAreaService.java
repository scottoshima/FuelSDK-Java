//
// ETContentAreaService.java -
//
//      x
//
// Copyright (C) 2013 ExactTarget
//
// @COPYRIGHT@
//

package com.exacttarget.fuelsdk;

import java.util.List;

import com.exacttarget.fuelsdk.filter.ETFilter;
import com.exacttarget.fuelsdk.model.ETContentArea;

public interface ETContentAreaService extends ETCrudService {

	public ETServiceResponse<ETContentArea> get(ETClient client)
	        throws ETSdkException;

	public ETServiceResponse<ETContentArea> get(ETClient client, ETFilter filter)
	        throws ETSdkException;

	public ETServiceResponse<ETContentArea> post(ETClient client, ETContentArea contentArea)
	    	throws ETSdkException;

    public ETServiceResponse<ETContentArea> patch(ETClient client, ETContentArea contentArea)
    		throws ETSdkException;

    public ETServiceResponse<ETContentArea> delete(ETClient client, ETContentArea contentArea)
	    	throws ETSdkException;
    
    public ETServiceResponse<ETContentArea> post(ETClient client, List<ETContentArea> contentAreas)
			throws ETSdkException;
	
	public ETServiceResponse<ETContentArea> patch(ETClient client, List<ETContentArea> contentAreas)
			throws ETSdkException;
	
	public ETServiceResponse<ETContentArea> delete(ETClient client, List<ETContentArea> contentAreas)
			throws ETSdkException;
}

