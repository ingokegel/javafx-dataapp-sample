/*
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.javafx.experiments.dataapp.client.rest;

import com.javafx.experiments.dataapp.client.DataApplication;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class LiveSalesViewClient {
    private final WebTarget rootTarget;
    private final Client client;

    public LiveSalesViewClient() {
        client = ClientBuilder.newClient().register(JacksonJaxbJsonProvider.class);
        rootTarget = client.target(DataApplication.SERVER_URI).path("com.javafx.experiments.dataapp.model.livesaleslist");
    }

    public void remove(String id) {
        rootTarget.path(id).request().delete();
    }

    public String countREST() {
        WebTarget target = rootTarget.path("count");
        return target.request(MediaType.TEXT_PLAIN).get(String.class);
    }

    public <T> T findAll(Class<T> responseType) {
        return rootTarget.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public void edit(Object requestEntity) {
        rootTarget.request().put(Entity.json(requestEntity));
    }

    public void create(Object requestEntity) {
        rootTarget.request().post(Entity.json(requestEntity));
    }

    public <T> T findRange(Class<T> responseType, String from, String to) {
        WebTarget target = rootTarget.path(from).path(to);
        return target.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T find(Class<T> responseType, String id) {
        WebTarget target = rootTarget.path(id);
        return target.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findFrom(Class<T> responseType, Integer from) {
        WebTarget target = rootTarget.path("date").path(from.toString());
        return target.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRecent(Class<T> responseType) {
        WebTarget target = rootTarget.path("recent");
        return target.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRecentRegion(Class<T> responseType, String region) {
        WebTarget target = rootTarget.path("recent").path("region").path(region);
        return target.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRecentProductType(Class<T> responseType, Integer productTypeId) {
        WebTarget target = rootTarget.path("recent").path("producttype").path(productTypeId.toString());
        return target.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRecentRegionProductType(Class<T> responseType, String region, Integer productTypeId) {
        WebTarget target = rootTarget
                .path("recent")
                .path("region")
                .path("producttype")
                .path(region)
                .path(productTypeId.toString());
        return target.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRecentRegionFrom(Class<T> responseType, String region, Integer orderLineId) {
        WebTarget target = rootTarget
                .path("recent")
                .path("region")
                .path(region)
                .path(orderLineId.toString());
        return target.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRecentProductTypeFrom(Class<T> responseType, Integer productTypeId, Integer orderLineId) {
        WebTarget target = rootTarget
                .path("recent")
                .path("producttype")
                .path(productTypeId.toString())
                .path(orderLineId.toString());
        return target.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRecentRegionProductTypeFrom(Class<T> responseType, String region, Integer productTypeId, Integer orderLineId) {
        WebTarget target = rootTarget
                .path("recent")
                .path("region")
                .path("producttype")
                .path(region)
                .path(productTypeId.toString())
                .path(orderLineId.toString());
        return target.request(MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}
