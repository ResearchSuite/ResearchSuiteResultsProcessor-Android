package org.researchsuite.rsrp.CSVBackend_RSRPSupport;

import org.apache.commons.lang3.StringUtils;
import org.researchsuite.rsrp.CSVBackend.CSVEncodable;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Christina on 2/1/2018.
 */

public class YADLSpotRawCSVEncodable extends YADLSpotRaw implements CSVEncodable {

    public YADLSpotRawCSVEncodable(UUID uuid, String taskIdentifier, UUID taskRunUUID, Map<String, Object> schemaID, String[] selected, String[] notSelected, String[] excluded, Map<String, String> resultMap) {
        super(uuid, taskIdentifier, taskRunUUID, schemaID, selected, notSelected, excluded, resultMap);
    }

    public static String TYPE = "YADLSpotRawCSVEncodable";

    @Override
    public String[] toRecords() {

        String time = "time"; // TODO: add time now in this field

        Collection resultMapValuesCollection = this.getResultMap().values();
        String[] resultMapValues = Arrays.copyOf(resultMapValuesCollection.toArray(), resultMapValuesCollection.toArray().length, String[].class);

       // String resultMapJoined = StringUtils.join(resultMapValues,",");

        String[] selected = this.getSelected();
        String[] notSelected = this.getNotSelected();
        String[] excluded = this.getExcluded();

        StringBuilder recordBuilder = new StringBuilder();

        String[] yadlHeader = new String[]{"timestamp","BedtoChair","Dressing","Housework","Lifting","Shopping","ShortWalk","Walkingupstairs"};

        for(int i=0; i<yadlHeader.length; i++){
            if (Arrays.asList(selected).contains(yadlHeader[i])){
                recordBuilder.append("selected,");
            }
            if (Arrays.asList(notSelected).contains(yadlHeader[i])){
                recordBuilder.append("not selected,");
            }
            if(Arrays.asList(excluded).contains(yadlHeader[i])){
                recordBuilder.append("excluded,");
            }
        }

        String record = recordBuilder.toString();
        String completeRecord = getTimestamp() + "," + record;
        String[] completeRecordArray = new String[]{completeRecord};

        return completeRecordArray;
    }

    @Override
    public String getTypeString() {
        return this.getTaskIdentifier();
    }

    @Override
    public String getHeader() {
        String[] yadlHeader = new String[]{"timestamp","BedToChair","Dressing","Housework","Lifting","Shopping","ShortWalk","WalkingUpStairs"};
        String yadlHeaderJoined = StringUtils.join(yadlHeader,",");
        return yadlHeaderJoined;
    }

    private String getTimestamp () {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int zone = calendar.get(Calendar.ZONE_OFFSET);

        StringBuilder timestampBuilder = new StringBuilder();
        timestampBuilder.append(year);
        timestampBuilder.append("-");
        timestampBuilder.append(month);
        timestampBuilder.append("-");
        timestampBuilder.append(date);
        timestampBuilder.append("T");
        timestampBuilder.append(hour);
        timestampBuilder.append(":");
        timestampBuilder.append(minute);
        timestampBuilder.append(":");
        timestampBuilder.append(second);
        timestampBuilder.append("-");
        timestampBuilder.append(zone);

        String timestamp = timestampBuilder.toString();

        return timestamp;
    }
}
