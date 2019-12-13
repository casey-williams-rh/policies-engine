/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.alerts.actions.api;

import java.util.Map;
import java.util.Set;

/**
 * A listener that will initialize the plugin and process incoming messages.
 *
 * @author Lucas Ponce
 */
public interface ActionPluginListener {

    /**
     * The alerts engine registers the plugins available with their properties.
     * This method is invoked at plugin registration time.
     *
     * @return a list of properties available on this plugin
     */
    Set<String> getProperties();

    /**
     * The alerts engine registers the plugins available with their default values.
     * This method is invoked at plugin registration time.
     * Default values can be modified by the alerts engine.
     *
     *
     * @return a list of default values for properties available on this plugin
     */
    Map<String, String> getDefaultProperties();

    /**
     * This method is invoked by the ActionService to process a new action generated by the engine.
     *
     * @param msg PluginMessage received to be processed by the plugin
     * @throws Exception any problem
     */
    void process(ActionMessage msg) throws Exception;
}
