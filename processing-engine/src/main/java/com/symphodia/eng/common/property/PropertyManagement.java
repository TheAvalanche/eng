package com.symphodia.eng.common.property;

import com.symphodia.spring.common.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class PropertyManagement {

    @Autowired
    PropertyService propertyService;

    @RequestMapping("/clear")
    public void clearCache() {
        propertyService.clearCache();
    }

    @RequestMapping("/putValue")
    public void putValue(final @RequestParam(value="key") String key, final @RequestParam(value="value") String value) {
        Property property = Property.valueOf(key);
        propertyService.putValue(property, value);
    }



    @RequestMapping("/content")
    public String cacheContent() {
        return propertyService.cacheContent();
    }

}
