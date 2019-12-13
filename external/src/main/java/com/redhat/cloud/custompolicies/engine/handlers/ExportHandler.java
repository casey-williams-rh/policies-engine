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
package com.redhat.cloud.custompolicies.engine.handlers;

import com.redhat.cloud.custompolicies.engine.handlers.util.ResponseUtil;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.hawkular.alerts.api.doc.DocEndpoint;
import org.hawkular.alerts.api.doc.DocPath;
import org.hawkular.alerts.api.doc.DocResponse;
import org.hawkular.alerts.api.doc.DocResponses;
import org.hawkular.alerts.api.model.export.Definitions;
import org.hawkular.alerts.api.services.DefinitionsService;
import org.hawkular.commons.log.MsgLogger;
import org.hawkular.commons.log.MsgLogging;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import static org.hawkular.alerts.api.doc.DocConstants.GET;

/**
 * @author Jay Shaughnessy
 * @author Lucas Ponce
 */
@DocEndpoint(value = "/export", description = "Export of triggers and actions definitions")
@ApplicationScoped
public class ExportHandler {
    private static final MsgLogger log = MsgLogging.getMsgLogger(ExportHandler.class);
    private static final String ROOT = "/";

    @Inject
    DefinitionsService definitionsService;

    @PostConstruct
    public void init(@Observes Router router) {
        String path = "/hawkular/alerts/export";
        router.get(path).handler(this::exportDefinitions);
    }

    @DocPath(method = GET,
            path = "/",
            name = "Export a list of full triggers and action definitions.")
    @DocResponses(value = {
            @DocResponse(code = 200, message = "Successfully exported list of full triggers and action definitions.", response = Definitions.class),
            @DocResponse(code = 500, message = "Internal server error.", response = ResponseUtil.ApiError.class)
    })
    public void exportDefinitions(RoutingContext routing) {
        routing.vertx()
                .executeBlocking(future -> {
                    String tenantId = ResponseUtil.checkTenant(routing);
                    try {
                        Definitions definitions = definitionsService.exportDefinitions(tenantId);
                        future.complete(definitions);
                    } catch (Exception e) {
                        log.debug(e.getMessage(), e);
                        throw new ResponseUtil.InternalServerException(e.toString());
                    }
                }, res -> ResponseUtil.result(routing, res));
    }
}
