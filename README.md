# AutoChecker

## Check for License Header

## Build
Inside the build folder, run the build.sh command with the following options:
```
AutoChecker Build Commend
usage: build.sh [<phase>]
Phases:
    help    Display command line usage information.
    clean   Clean project binaries.
    package Build project binaries.
```

### Run
After building, go to the bin folder, and run autochecker command with the following options:
```
AutoChecker Commend
Usage: autochecker [-options]
Where options include:
    --name | -n     Name of a SINGLE project repository.  Supports
                    only one project.  --file parameter is ignored
                    when --name is defined
    --file | -f     Path to file with project repository list file.
                    Use when defining multiple projects
    --output | -o   Path to location of ooutput report file.
```

