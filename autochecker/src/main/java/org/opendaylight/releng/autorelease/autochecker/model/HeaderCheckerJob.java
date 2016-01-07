/*
 * Copyright (c) 2016 Company and Others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.releng.autorelease.autochecker.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.opendaylight.releng.autorelease.autochecker.utils.CheckerUtils;

public class HeaderCheckerJob implements CheckerJob {

    private String directory;

    private List<String> extensions;

    private List<String> excludes;

    private String template;

    private List<String> lines;

    public HeaderCheckerJob(JSONObject json) throws JSONException, IOException {
        if (json.has("directory")) {
            directory = json.getString("directory");
        }
        if (json.has("extensions")) {
            extensions = CheckerUtils.toStringList(json.getJSONArray("extensions"));
        }
        if (json.has("excludes")) {
        	excludes = CheckerUtils.toStringList(json.getJSONArray("excludes"));
        }
        if (json.has("template")) {
            template = json.getString("template");
            lines = FileUtils.readLines(new File(template));
        }
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    public List<String> getExcludes() {
		return excludes;
	}

	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

	public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

}
