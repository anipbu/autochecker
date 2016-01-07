#!/bin/bash

# @License EPL-1.0 <http://spdx.org/licenses/EPL-1.0>
##############################################################################
# Copyright (c) 2016 Company and Others.
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
##############################################################################

##############################################################################
# Define Defaults
##############################################################################
DEFAULT_REPO_LIST=../conf/projects.repo
DEFAULT_REPO_BRANCH=stable/beryllium
DEFAULT_REPO_REMOTE=remotes/origin/stable/beryllium
DEFAULT_REPORT_PATH=report.txt

##############################################################################
# Define Variables
##############################################################################
REPO_LIST=${1-${DEFAULT_REPO_LIST}}
REPO_BRANCH=${2-${DEFAULT_REPO_BRANCH}}
REPO_REMOTE=${3-${DEFAULT_REPO_REMOTE}}
REPORT_PATH=${4-${DEFAULT_REPORT_PATH}}
REPORT_STATUS=0

##############################################################################
# Clone Projects
##############################################################################
clone () {
  mkdir -p projects
  cp iterate.pl projects/iterate.pl
  (cd projects; ./iterate.pl --no-cd ${REPO_LIST} "git clone https://git.opendaylight.org/gerrit/{f}.git")
  #(cd projects; ./iterate.pl ${REPO_LIST} "git checkout -b ${REPO_BRANCH} ${REPO_REMOTE}")
  rm -rf projects/iterate.pl
}

##############################################################################
# Package Autochecker
##############################################################################
package () {
  (cd ../autochecker/; mvn package)
  cp ../autochecker/target/autochecker.jar autochecker.jar
  cp -R ../autochecker/target/lib lib
}

##############################################################################
# Execute Autochecker
##############################################################################
execute () {
  java -jar autochecker.jar > ${REPORT_PATH}
  REPORT_STATUS=$?
  echo "Completed with status: ${REPORT_STATUS}"
}

clone
package
execute

exit ${REPORT_STATUS}
