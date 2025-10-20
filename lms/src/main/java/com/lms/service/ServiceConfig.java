package com.lms.service;

import com.lms.domain.asset.service.AssetService;

public class ServiceConfig {
    private static AssetService assetService;
    
    public static AssetService assetService() {
        if (assetService == null) {
            assetService = new AssetService();
        }
        
        return  assetService;
    }
}
