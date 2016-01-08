/*
 * Copyright (c) 2016 Company and Others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.releng.autorelease.autochecker.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opendaylight.releng.autorelease.autochecker.model.HeaderCheckerJob;

public class HeaderCheckerService implements CheckerService {

    private List<HeaderCheckerJob> jobs;

    public HeaderCheckerService() {
        jobs = new ArrayList<HeaderCheckerJob>();
    }

    @Override
    public void load(String conf) {
        InputStream input = null;
        try {
            input = new FileInputStream(conf);
            String text = IOUtils.toString(input);
            JSONObject json = new JSONObject(text);
            if (json.has("jobs")) {
                JSONArray array = json.getJSONArray("jobs");
                for (int i = 0; i < array.length(); i++) {
                    jobs.add(new HeaderCheckerJob(array.getJSONObject(i)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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

    @Override
    public int check() {
    	int failed = 0;
    	try {
            for (HeaderCheckerJob job : jobs) {
                failed = failed + scan(job);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    	return failed;
    }

    private boolean exclude(List<String> exlcudes, String path) {
    	if (exlcudes != null && path != null) {
	    	for (String exclude : exlcudes) {
				if (path.contains(exclude)) {
	        		return true;
	        	}
			}
    	}
    	return false;
    }

    private int scan(HeaderCheckerJob job) throws IOException {
        int passed = 0;
        int failed = 0;
        int skipped = 0;
        File directory = new File(job.getDirectory());
        String[] extensions = job.getExtensions().toArray(new String[0]);
        Collection<File> files = FileUtils.listFiles(directory, extensions, true);
        for (File file : files) {
        	if (exclude(job.getExcludes(), file.getAbsolutePath())) {
        		skipped++;
        		continue;
        	}
            if (!file.isFile()) {
                fail("Not a file", file);
                failed++;
                continue;
            }
            boolean find = match(job.getLines(), FileUtils.readLines(file), job.getMultilines(), file);
            if (!find) {
                failed++;
                continue;
            }
            passed++;
        }
        System.out.println("[HeaderCheckerService Report] Total: " + files.size() + "; Passed: " + passed + "; Failed: " + failed + "; Skipped: " + skipped + ";");
        return failed;
    }

    private boolean match(List<String> joblines, List<String> sourcelines, List<Integer> multilines, File file) {
        int sourceindex = 0;
        if (sourcelines.size() < joblines.size()) {
            fail("Header mismatch: Expected " + joblines + " lines; Actual " + sourcelines.size() + " lines.", file);
            return false;
        }
        for (int i = 0; i < joblines.size(); i++) {
            Pattern pattern = Pattern.compile(joblines.get(i));
            Matcher matcher = pattern.matcher(sourcelines.get(sourceindex));
            boolean find = matcher.find();
            if (!find) {
                fail("Line mismatch: Expected " + joblines.get(i) + "; Actual " + sourcelines.get(sourceindex), file);
                return false;
            }
            if (multilines != null && multilines.contains(Integer.valueOf(i))) {
            	while (sourceindex + 1 < sourcelines.size()) {
            		Matcher multilinematcher = pattern.matcher(sourcelines.get(sourceindex + 1));
            		boolean multilinefind = multilinematcher.find();
            		if (multilinefind) {
            			sourceindex++;
            		} else {
            			break;
            		}
            	}
            }
            sourceindex++;
        }
        return true;
    }

    private void fail(String message, File file) {
        System.out.println("[HeaderCheckerService Failure - " + file.getAbsolutePath() + "] " + message);
    }

}
