package persistence;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import exceptions.CouldNotUploadFileException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * Handles Azure storage
 *
 * @author Matthias Kunnen
 */
public class AzureBlobStorage {
    private static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=iim;AccountKey=ZTIwjaeSYCxEuqyQmhtYzo348wTMd5IkeC2Vwyxl67rXOKay4CfuJE2deGDo0RGSooIwcGXB7G6CJC+AMvu24w==;BlobEndpoint=https://iim.blob.core.windows.net/;TableEndpoint=https://iim.table.core.windows.net/;QueueEndpoint=https://iim.queue.core.windows.net/;FileEndpoint=https://iim.file.core.windows.net/";

    public void upload(File sourceFile, String containerName, String blobName) throws CouldNotUploadFileException {
        try {
            CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            CloudBlobContainer container = serviceClient.getContainerReference(containerName);
            container.createIfNotExists();

            CloudBlockBlob blob = container.getBlockBlobReference(blobName);
            blob.upload(new FileInputStream(sourceFile), sourceFile.length());
        } catch (URISyntaxException | StorageException | InvalidKeyException | IOException e) {
            throw new CouldNotUploadFileException(String.format("Could not upload %s to container: %s/%s", sourceFile.getName(), containerName, blobName),e);
        }
    }
    /*
    Download:
    File destinationFile = new File(sourceFile.getParentFile(), "image1Download.tmp");
    blob.downloadToFile(destinationFile.getAbsolutePath());
     */
}
