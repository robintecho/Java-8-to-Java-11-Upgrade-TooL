package com.upgrader.upgraderApplication.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UpgraderService {

    private static final Logger logger = LoggerFactory.getLogger("UpgraderService");

    @Autowired
    private ModifyFilesService modifyFilesService;

    @Autowired
    private SearchFilesService searchFilesService;

    @Autowired
    private JsonToHashMapService jsonToHashMapService;

    @Value("${configFilepath}")
    private String configFilepath;

    public void execute() {
        try {
            InputStream inputStream = new FileInputStream(configFilepath);
            String jsonTxt = IOUtils.toString(inputStream, "UTF-8");

			/*final String uri = "http://192.168.1.41:9002/fileconfig/retrive";
			RestTemplate restTemplate = new RestTemplate();
			String jsonTxt = restTemplate.getForObject(uri, String.class);*/

            jsonToHashMapService.ConvertToMap(jsonTxt).keySet().stream().spliterator().forEachRemaining(key -> {

                Map<String, Object> jsonMap;
                try {
                    jsonMap = (Map<String, Object>) jsonToHashMapService.ConvertToMap(jsonTxt).get((String) key);

                    if (jsonMap.get("enable").equals(true)) {
                        final List<String> getPaths = searchFilesService.find((String) jsonMap.get("path"),
                                (String) jsonMap.get("fileName"), new ArrayList<String>());

                        logger.info(" All Files Searched.");

                        // Create an object of array list
                        ArrayList<Map<String, Object>> arrayList = (ArrayList<Map<String, Object>>) jsonMap
                                .get("changes");

                        Long startTime = System.currentTimeMillis();

                        arrayList.stream().spliterator().forEachRemaining((list) -> {
                            getPaths.stream().spliterator().forEachRemaining(abpath -> {
                                modifyFilesService.modifyFile(abpath, (String) list.get("oldValue"),
                                        (String) list.get("newValue"), (String) list.get("parentNodes"));
                            });
                        });

                        Long endTime = System.currentTimeMillis();

                        logger.info("Time Taken for execution of " + key + " is " + (endTime - startTime));
                    }
                } catch (Exception e) {
                    logger.error("Some issue while convert json to Map: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        logger.error("IOException while closing inputStream: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
            logger.info("All Files Updated.");
        } catch (Exception e) {
            logger.error("Some issue while convert json to Map: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
