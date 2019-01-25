package com.cloudcard.photoDownloader;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.cloudcard.photoDownloader.CloudCardPhotoService.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

@Ignore
public class CloudCardPhotoServiceTests {

    private String[] fetchStatuses = {"APPROVED", "READY_FOR_DOWNLOAD"};

    CloudCardPhotoService service;

    @Before
    public void setup() {

        service = new CloudCardPhotoService();
        ReflectionTestUtils.setField(service, "apiUrl", "https://api.onlinephotosubmission.com/api");
        ReflectionTestUtils.setField(service, "accessToken", "bom0m9qvfpkr42rv3odo41feilmvj740");
        ReflectionTestUtils.setField(service, "fetchStatuses", fetchStatuses);
        ReflectionTestUtils.setField(service, "putStatus", "DOWNLOADED");
    }

    @Test
    public void testFetchReadyForDownload() throws Exception {

        List<Photo> photos = service.fetchReadyForDownload();
        assertThat(photos.size()).isGreaterThanOrEqualTo(0);
        for (Photo photo : photos) {
            assertThat(fetchStatuses).contains(photo.getStatus());
        }
    }

    @Test
    public void testFetchApproved() throws Exception {


        List<Photo> photos = service.fetchApproved();
        assertThat(photos.size()).isGreaterThanOrEqualTo(0);
        for (Photo photo : photos) {
            assertThat(photo.getStatus()).isEqualTo(APPROVED);
        }
    }

    @Test
    public void testMarkAsDownloaded() throws Exception {

        assumeTrue(!service.fetchReadyForDownload().isEmpty());

        Photo photo = service.fetchReadyForDownload().get(0);
        int originalReadyCount = service.fetchReadyForDownload().size();
        int originalDownloadedCount = service.fetch(DOWNLOADED).size();

        Photo updatedPhoto = service.markAsDownloaded(photo);

        List<Photo> downloadedPhotos = service.fetch(DOWNLOADED);
        assertThat(updatedPhoto).isNotNull();
        assertThat(updatedPhoto.getStatus()).isEqualTo(DOWNLOADED);

        assertThat(service.fetchReadyForDownload().size()).isEqualTo(originalReadyCount - 1);
        assertThat(downloadedPhotos.size()).isEqualTo(originalDownloadedCount + 1);

        service.updateStatus(photo, READY_FOR_DOWNLOAD);

        assertThat(service.fetchReadyForDownload().size()).isEqualTo(originalReadyCount);
        assertThat(service.fetch(DOWNLOADED).size()).isEqualTo(originalDownloadedCount);
    }
}
