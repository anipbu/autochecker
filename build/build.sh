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

##############################################################################
# Define Variables
##############################################################################
BUILD_PHASE=${1-package}

##############################################################################
# Show Usage
##############################################################################
usage () {
  echo "AutoChecker Build Commend"
  echo "usage: build.sh [<phase>]"
  echo "Phases:"
  echo "    help    Display command line usage information."
  echo "    clean   Clean project binaries."
  echo "    package Build project binaries."
}

##############################################################################
# Clean Autochecker
##############################################################################
clean () {
  rm -rf ../bin
  (cd ../autochecker/; mvn clean)
}

##############################################################################
# Package Autochecker
##############################################################################
package () {
  mkdir -p ../bin
  mkdir -p ../bin/var
  (cd ../autochecker/; mvn package)
  cp ../autochecker/target/autochecker.jar ../bin/var/autochecker.jar
  cp -R ../autochecker/target/lib ../bin/var/lib
  cp iterate.pl ../bin/var/iterate.pl
  cp -R conf ../bin/conf
  cp autochecker ../bin/autochecker
}

if [ "${BUILD_PHASE}" = "help" ]; then
  usage
elif [ "${BUILD_PHASE}" = "clean" ]; then
  clean
elif [ "${BUILD_PHASE}" = "package" ]; then
  package
else
  usage
fi

