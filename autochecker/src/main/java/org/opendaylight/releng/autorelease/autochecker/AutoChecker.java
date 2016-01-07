/*
 * Copyright (c) 2016 Company and Others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.releng.autorelease.autochecker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.opendaylight.releng.autorelease.autochecker.service.CheckerService;
import org.opendaylight.releng.autorelease.autochecker.service.HeaderCheckerService;

public class AutoChecker {

    public static final String DEFAULT_CONFIGURATION_FILEPATH = "conf/autochecker.properties";
    public static final String HEADER_SERVICE_PROPERTY = "org.opendaylight.releng.autorelease.autochecker.service.header";

    private List<CheckerService> services;

    public AutoChecker(String[] args) {
    	InputStream input = null;
        try {
            // Load Properties
            input = new FileInputStream((args != null && args.length > 0 && args[0] != null) ? args[0] : DEFAULT_CONFIGURATION_FILEPATH);
            Properties properties = new Properties();
            properties.load(input);
            // Load Services
            services = new ArrayList<CheckerService>();
            CheckerService headerCheckerService = new HeaderCheckerService();
            headerCheckerService.load(properties.getProperty(HEADER_SERVICE_PROPERTY));
            services.add(headerCheckerService);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int execute() {
    	int failed = 0;
        for (CheckerService service : services) {
            failed = failed + service.check();
        }
        return failed;
    }

    public static void main(String[] args) {
    	AutoChecker application = new AutoChecker(args);
        int failed = application.execute();
        if (failed > 0) {
        	System.out.println("Completed with status: " + 1);
        	System.exit(1);
        } else {
        	System.out.println("Completed with status: " + 0);
        }
    }

}
