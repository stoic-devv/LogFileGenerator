# Log generator

This repository is a fork of: [0x1DOCD00D/LogFileGenerator](https://github.com/0x1DOCD00D/LogFileGenerator/tree/homework3)
Please refer the original [README.md](https://github.com/0x1DOCD00D/LogFileGenerator/blob/homework3/README.md) for the functional details of this repository

Changes are made to make this repository run over AWS EC2.

## Functionality
The main functionality of this application, that of generating log messages, is covered extensively in the original repository here: [0x1DOCD00D/LogFileGenerator](https://github.com/0x1DOCD00D/LogFileGenerator/tree/homework3)

### Additional functionality and code changes:
1. Just after all messages are logged:
   1. A hash table of the form `<key, value> == <timestamp, log/file/name>` is generated. Where `timestamp` is the timestamp of the first log message in `log/file/name>`.
   2. All the content from the `/log` directory is copied to the S3 bucket. This S3 bucket name is configured in `resources/application.conf`. To run this application with successful termination _locally_ , user will have to install and configure [AWS CLI](https://aws.amazon.com/cli/), and the S3 bucket should be present in AWS account of the user.
2. `SizeAndTimeBasedRollingPolicy` is used in `logback.xml` so that the size of each log file should not exceed more than `50KB`. This, in combination with hash table is used to search the log messages efficiently.
3. Log message frequency is increased to produce messages within an interval (currently between `0.1-1 sec`) and number of log messages are kept as `5000` (both configured in `application.conf`). This is done for faster testing and generating big log data. For a moew realistic scenario user can lower the frequency of messages within `1-10 sec`

## Youtube video
For more details on deploying this application over AWS check out this [youtube video](https://youtu.be/XiBZYhNcce8)
