package com.cloudcard.photoDownloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class UdfFileStorageService extends FileStorageService implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(UdfFileStorageService.class);

    @Value("${downloader.photoDirectory}")
    String photoDirectory;

    @Value("${downloader.slash}")
    private String slash;

    @Value("${downloader.enableUdf}")
    private boolean enableUdf;

    @Value("${downloader.udfDirectory}")
    String udfDirectory;

    @Value("${downloader.udfFilePrefix}")
    private String udfFilePrefix;

    @Value("${downloader.udfFileExtension}")
    private String udfFileExtension;

    @Value("${downloader.descriptionDateFormat}")
    private String descriptionDateFormat;

    @Value("${downloader.batchIdDateFormat}")
    private String batchIdDateFormat;

    @Value("${downloader.createdDateFormat}")
    private String createdDateFormat;

    @Override
    public List<PhotoFile> save(Collection<Photo> photos) throws Exception {

        List<PhotoFile> photoFiles = super.save(photos);

        if (enableUdf) createUdfFile(photoFiles);

        return photoFiles;
    }

    private void createUdfFile(List<PhotoFile> photoFiles) throws IOException {

        List<String> lines = new ArrayList<>();
        String blankLine = "!";

        lines.addAll(generateUdfHeader(photoFiles));
        lines.add(blankLine);
        lines.addAll(generateUdfFormat(photoFiles));
        lines.add(blankLine);
        lines.addAll(generateUdfData(photoFiles));

        writeUdfFile(lines);
    }

    private List<String> generateUdfHeader(List<PhotoFile> photoFiles) {

        List<String> header = new ArrayList<>();
        header.add("!Description: Photo Import " + new SimpleDateFormat(descriptionDateFormat).format(new Date()));
        header.add("!Source: CloudCard Online Photo Submission");
        header.add("!BatchID: " + new SimpleDateFormat(batchIdDateFormat).format(new Date()));
        header.add("!Created: " + new SimpleDateFormat(createdDateFormat).format(new Date()));
        header.add("!Version:");
        header.add("!RecordCount: " + photoFiles.size());
        return header;
    }

    private List<String> generateUdfFormat(List<PhotoFile> photoFiles) {

        List<String> format = new ArrayList<>();
        int longestId = findLongestId(photoFiles);
        int longestFilename = findLongestFilename(photoFiles);

        format.add("!BeginFormat");
        format.add("!Odyssey_PCS,\t1,\t" + longestId + "\t\"IDNUMBER\"");
        format.add("!Odyssey_PCS,\t" + (longestId + 1) + ",\t" + longestFilename + "\t\"PICTUREPATH\"");
        format.add("!EndFormat");
        return format;
    }

    private List<String> generateUdfData(List<PhotoFile> photoFiles) {

        List<String> data = new ArrayList<>();
        int longestId = findLongestId(photoFiles);

        data.add("!BeginData");
        for (PhotoFile photoFile : photoFiles) {
            data.add(fixedLengthString(photoFile.getIdNumber(), longestId) + photoFile.getFileName());
        }
        data.add("!EndData");
        return data;
    }

    private int findLongestFilename(List<PhotoFile> photoFiles) {

        int longestFilename = 0;
        for (PhotoFile photoFile : photoFiles) {
            longestFilename = Math.max(photoFile.getFileName().length(), longestFilename);
        }
        return longestFilename;
    }

    private int findLongestId(List<PhotoFile> photoFiles) {

        int longestId = 0;
        for (PhotoFile photoFile : photoFiles) {
            longestId = Math.max(photoFile.getIdNumber().length(), longestId);
        }
        return longestId;
    }

    private static String fixedLengthString(String string, int length) {

        return String.format("%1$-" + length + "s", string);
    }

    private void writeUdfFile(List<String> lines) throws IOException {

        log.info("Writing the UDF file...");
        String fileName = udfDirectory + slash + udfFilePrefix + new SimpleDateFormat(batchIdDateFormat).format(new Date()) + udfFileExtension;
        FileWriter writer = new FileWriter(fileName);
        for (String line : lines) {
            log.info(line);
            writer.write(line + "\n");
        }
        writer.close();
        log.info("...UDF file writing complete");
    }

}