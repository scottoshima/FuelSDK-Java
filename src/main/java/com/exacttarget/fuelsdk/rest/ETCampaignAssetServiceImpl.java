//
// ETCampaignAssetServiceImpl.java -
//
//      x
//
// Copyright (C) 2013 ExactTarget
//
// @COPYRIGHT@
//

package com.exacttarget.fuelsdk.rest;

import com.exacttarget.fuelsdk.ETCampaignAssetService;
import com.exacttarget.fuelsdk.ETClient;
import com.exacttarget.fuelsdk.ETSdkException;
import com.exacttarget.fuelsdk.ETServiceResponse;
import com.exacttarget.fuelsdk.filter.ETFilter;
import com.exacttarget.fuelsdk.model.ETCampaignAsset;
import com.exacttarget.fuelsdk.model.ETObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class ETCampaignAssetServiceImpl extends ETCrudServiceImpl implements ETCampaignAssetService {

	public ETServiceResponse<ETCampaignAsset> get(ETClient client)
			throws ETSdkException {
		return super.get(client, ETCampaignAsset.class);
	}
	
	public ETServiceResponse<ETCampaignAsset> get(ETClient client, ETFilter filter)
			throws ETSdkException {
		return super.get(client, ETCampaignAsset.class, filter);
	}
	
	public ETServiceResponse<ETCampaignAsset> post(ETClient client, ETCampaignAsset asset) 
		throws ETSdkException {
		return super.post(client, asset);
	}

	public ETServiceResponse<ETCampaignAsset> patch(ETClient client, ETCampaignAsset asset) 
		throws ETSdkException {
		return super.patch(client, asset);
	}
	
	public ETServiceResponse<ETCampaignAsset> delete(ETClient client, ETCampaignAsset asset) 
		throws ETSdkException 
	{
		return super.delete(client, asset);
	}

	@Override
	protected <T extends ETObject> JsonObject createRequest(T object, Class<T> type) throws ETSdkException {
		
		ETCampaignAsset asset = (ETCampaignAsset)object;
		
		JsonObject root = new JsonObject();
		
		JsonArray ids = new JsonArray();
		
		ids.add(new JsonPrimitive(asset.getItemID()));
		
		root.add("ids", ids);
		
		root.addProperty("type", asset.getType());
		
		return root;
	}
}
