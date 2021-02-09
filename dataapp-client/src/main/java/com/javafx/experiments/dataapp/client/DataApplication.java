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
package com.javafx.experiments.dataapp.client;

import com.javafx.experiments.dataapp.client.rest.ProductTypeClient;
import com.javafx.experiments.dataapp.client.rest.RegionClient;
import com.javafx.experiments.dataapp.model.ProductType;
import com.javafx.experiments.dataapp.model.Region;
import dataapppreloader.DataAppPreloader.PreloaderHandoverEvent;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Vector;

/**
 * Main Application for the Henley Car Sales Application
 */
public class DataApplication extends Application {
    public static final String SERVER_URI = "http://localhost:8080/dataapp-server/resources";
    private static final ObservableList<Object> americanRegions = FXCollections.observableArrayList();
    private static final ObservableList<Object> productTypes = FXCollections.observableArrayList();
    private static final Vector<Runnable> dataLoadingTasks = new Vector<>();
    private Parent root;

    @Override public void init() throws Exception {
        Thread.sleep(5000);
        americanRegions.add("All Regions");
        productTypes.add("All Products");
        registerDataLoadingTask(() -> {
            // fetch available regions in the background
            var getRegions = new Task<Region[]>() {
                @Override protected Region[] call() {
                    RegionClient regionClient = new RegionClient();
                    Region[] regions;
                    regions = regionClient.findAmerican(Region[].class);
                    regionClient.close();
                    regions = Arrays.copyOfRange(regions, 0, regions.length-1);
                    return regions;
                }
            };
            getRegions.valueProperty().addListener((ov, t, t1) -> americanRegions.addAll((Object[])t1));
            new Thread(getRegions).start();
            // fetch available product types in the background
            var getProductTypes = new Task<ProductType[]>(){
                @Override protected ProductType[] call() {
                    ProductTypeClient ptClient = new ProductTypeClient();
                    ProductType[] types = ptClient.findAll(ProductType[].class);
                    ptClient.close();
                    return types;
                }
            };
            getProductTypes.valueProperty().addListener((ov, t, t1) -> productTypes.addAll((Object[])t1));
            new Thread(getProductTypes).start();
        });
        // create ui
        root = FXMLLoader.load(DataApplication.class.getResource("dataapp.fxml"));
    }
    
    @Override public void start(Stage stage) throws Exception {
        // let preloader know we are done creating the ui
        notifyPreloader(new PreloaderHandoverEvent(root,
                DataApplication.class.getResource("dataapp.css").toExternalForm(),
                dataLoadingTasks));
    }
    
    public static void registerDataLoadingTask(Runnable task) {
        dataLoadingTasks.add(task);
    }

    public static ObservableList<Object> getAmericanRegions() {
        return americanRegions;
    }

    public static ObservableList<Object> getProductTypes() {
        return productTypes;
    }
    
    public static void main(String[] args) { launch(args); }
}
