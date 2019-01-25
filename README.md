# cloudcard-photo-downloader

This project automatically downloads photos from [CloudCard Online Photo Submission](http://onlinephotosubmission.com/).

## Video Tutorial
https://youtu.be/0KcCnMOf1jA

[![Tutorial Video](https://img.youtube.com/vi/0KcCnMOf1jA/0.jpg)](https://www.youtube.com/watch?v=0KcCnMOf1jA)

## Requirements

- Java 1.8 (to run from the Jar file)
- Java 1.6+ (to build from Source)
- Office level access to [CloudCard Online Photo Submission](http://onlinephotosubmission.com/)

## Usage

1. Download the [jar file](https://github.com/online-photo-submission/cloudcard-photo-downloader/raw/master/cloudcard-photo-downloader.jar).
1. Download [application.properties](https://raw.githubusercontent.com/online-photo-submission/cloudcard-photo-downloader/master/src/main/resources/application.properties) into the same directory
1. Configure `application.properties`
1. Run `java -jar cloudcard-photo-downloader.jar`

## Configuration

The simplest way to configure the application is by creating an `application.properties` file, which should be saved in the same directory as the downloader.  There are, however, many other strategies for configuring the application.  For example you may configure the settings using environment variables, JVM variables, etc.  See the [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) for more information on those options.

Below are descriptions of each option:

- cloudcard.api.url  
  - default: `https://api.onlinephotosubmission.com/api`
  - description: This option allows you to specify the URL of your CloudCard Online Photo Submission API.  Most users will not need to change this setting.  Generally, this is only useful if you are testing the integration using the test intance `https://test-api.onlinephotosubmission.com/api`.
- cloudcard.api.accessToken
  - default: none
  - description: this setting holds the API access token for your service account and must be set before the exporter to run. On a Unix/Linux based operating system, you can use `get-token.sh` to get your access token.
- downloader.delay.milliseconds
  - default: `600000` (Ten Minutes)
  - this is the amount of time the exporter will wait between exports
- downloader.photoDirectory
  - default: none
  - description: this is the absolute path to the directory into which the photos will be saved
- downloader.udfDirectory
  - default: 
- downloader.udfFilePrefix
  - default: `CloudCard_Photos_`
  - description: The first part of the UDF filename.  The UDF filename is constructed by concatonating the `udfFilePrefix`, a date formated accordning to `batchIdDateFormat`, and `udfFileExtension`.  Given the defaults the generated filename will look something like `CloudCard_Photos_201805221648.udf`
- downloader.udfFileExtension
  - default: `.udf`
  - description: The extension to use for the UDF filename (see the description for `downloader.udfFilePrefix`)
- downloader.descriptionDateFormat
  - default: `MMM dd 'at' HHmm`
  - description: The format of the date in the UDF description (i.e. `!Description: Photo Import May 22 at 1541`)
- downloader.batchIdDateFormat
  - default: `YYYYMMddHHmm`
  - description: The format of the date in the UDF batch ID field (i.e. `!BatchID: 201805221541`) and in the filename suffix (see the description for `downloader.udfFilePrefix`) 
- downloader.createdDateFormat
  - default: `YYYY-MM-dd`
  - description: The format of the date in the UDF Created field (i.e. `!Created: 2018-05-22`)
- downloader.enableUdf
  - default: `true`
  - description: Enable/Disable UDF file generation
- downloader.fetchStatuses
  - default: `READY_FOR_DOWNLOAD`
  - description: Photos with these statuses will be downloaded. Separate statuses with a comma.
- downloader.putStatus
  - default: `DOWNLOADED`
  - description: Downloaded photos will be marked with this status in the CloudCard web application.
## Support and Warranty
THIS PROJECT IS DISTRIBUTED WITH NO WARRANTY.  SEE THE LICENSE FOR FULL DETAILS.
If your organization needs fully warranteed CloudCard integration software, consider Cloud Photo Connect from [Vision Database Systems](http://www.visiondatabase.com/).
