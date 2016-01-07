/*
 * Copyright (c) 2016 Company and Others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.releng.autorelease.autochecker.service;

/**
 * A service that checks source code or compliance with check style
 * configurations.
 */
public interface CheckerService {

    /**
     * Load service.
     * 
     * @param conf
     *            Path to configuration file.
     */
    public void load(String conf);

    /**
     * Execute service.
     * @return Number of failures.
     */
    public int check();

}
