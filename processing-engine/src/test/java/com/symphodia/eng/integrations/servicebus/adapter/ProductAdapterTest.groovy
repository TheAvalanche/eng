package com.symphodia.eng.integrations.servicebus.adapter

import com.google.common.base.CharMatcher
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.symphodia.eng.integrations.servicebus.dto.order.Product
import com.symphodia.eng.integrations.servicebus.dto.order.Service
import spock.lang.Specification

class ProductAdapterTest extends Specification {

    String serviceJson = '''{
            "activationType":"NEW",
            "domain":"at",
            "productType": "SERVICE",
            "id": "42561"
            }'''

    def "test subscription de/serialization"() {
        when:
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Product.class, new ProductAdapter())
                .create()
        Product product = gson.fromJson(serviceJson, Product.class)
        then:
        product instanceof Service
        product.domain == "at"

        when:
        String productJson = gson.toJson(product)
        then:
        productJson == CharMatcher.WHITESPACE.removeFrom(serviceJson)
    }

}