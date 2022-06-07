package org.Tumanyan.services;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.ProgressListener;
import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.json.Link;
import com.yandex.disk.rest.json.Resource;
import com.yandex.disk.rest.util.ResourcePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DiskService {
    private static final Logger logger = LoggerFactory.getLogger(DiskService.class);
    private final RestClient client;

    public DiskService(String user, String token) {
        logger.info("Начало сеанса работы.");
        Credentials credentials = new Credentials(user, token);
        client = new RestClient(credentials);
    }

    ArrayList<String> getFilesInfo(String resourceType) {
        try {
            ResourcesArgs resourcesArgs = new ResourcesArgs.Builder()
                    .setMediaType(resourceType)
                    .setLimit(50)
                    .setOffset(0)
                    .build();
            List<Resource> resourceList = client.getFlatResourceList(resourcesArgs).getItems();
            final ArrayList<String> fileInfoList = new ArrayList<String>(resourceList.size());
            for (Resource r : resourceList) {
                fileInfoList.add(r.getName() + " " + r.getMediaType());
            }
            return fileInfoList;
        } catch (Exception ex) {
            logger.warn(ex.toString());
        }
        return new ArrayList<String>();
    }

    public boolean getFile(String pathToDownload, String pathOnDisk) {
        try {
            File local = new File(pathToDownload);
            int i = 0;
            while (local.exists()) {
                local = new File(pathToDownload + "_delete" + ++i);
            }
            client.downloadFile(pathOnDisk, local, new ProgressListener() {
                @Override
                public void updateProgress(long loaded, long total) {
                    logger.info("updateProgress: " + loaded + " / " + total);
                }

                @Override
                public boolean hasCancelled() {
                    return false;
                }
            });
            return true;
        } catch (Exception ex) {
            logger.warn(ex.toString());
        }
        return false;
    }


    String uploadFile(String localPath) throws Exception {
        File local = new File(localPath.replace("\"",""));
        Link link;
        String diskPath = "";

            String[] buffName = localPath.split( "\\\\");
            String name = buffName[buffName.length - 1].replace("\"","");
            diskPath = "/Files/" + name;
            link = client.getUploadLink(diskPath, true);


            ProgressListener myListener = new ProgressListener() {
                boolean doCancel = false;

                @Override
                public void updateProgress(long loaded, long total) {
                    logger.info("updateProgress: " + loaded + " / " + total);
                    if (loaded >= 50000000) {
                        doCancel = true;
                    }
                }

                @Override
                public boolean hasCancelled() {
                    if (doCancel) {
                        logger.info("cancelled");
                    }
                    return doCancel;
                }
            };
            client.uploadFile(link, true, local, myListener);
            return diskPath;

    }


    boolean makeFolder(String path, String name) {
        try {
            client.makeFolder(path + name);
            return true;
        } catch (Exception ex) {
            logger.warn(ex.toString());
        }
        return false;
    }

    boolean deleteFile(String name) {
        try {
            ResourcesArgs resourcesArgs = new ResourcesArgs.Builder()
                    .setMediaType("xml")
                    .setLimit(50)
                    .setOffset(0)
                    .build();
            List<Resource> resourceList = client.getFlatResourceList(resourcesArgs).getItems();
            String path = "";
            for (Resource r : resourceList) {
                if (r.getName().equals(name)) {
                    path = r.getPath().getPath();
                    break;
                }
            }
            if (!path.equals("")) {
                client.delete(path, false);
                return true;
            }
        } catch (Exception ex) {
            logger.warn(ex.toString());
        }
        return false;
    }
}