package com.symphodia.eng.integrations.servicebus.adapter;

import com.google.gson.*;
import com.symphodia.eng.integrations.servicebus.dto.order.Product;
import com.symphodia.eng.integrations.servicebus.dto.order.ProductType;

import java.lang.reflect.Type;

public class ProductAdapter implements JsonDeserializer<Product>, JsonSerializer<Product> {

    @Override
    public Product deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ProductType productType = ProductType.valueOf(json.getAsJsonObject().get("productType").getAsString());
        return context.deserialize(json, productType.getProductType());
    }

    @Override
    public JsonElement serialize(Product src, Type typeOfSrc, JsonSerializationContext context) {
        ProductType productType = src.productType;
        return context.serialize(src, productType.getProductType());
    }
}
