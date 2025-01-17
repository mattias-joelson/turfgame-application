package org.joelson.turf.application.model;

import org.joelson.turf.application.db.DatabaseEntityManager;
import org.joelson.turf.util.TimeUtil;
import org.joelson.turf.zundin.Today;
import org.joelson.turf.zundin.TodayZone;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VisitCollection {

    private final DatabaseEntityManager dbEntity;

    public VisitCollection(DatabaseEntityManager dbEntity) {
        this.dbEntity = dbEntity;
    }

    private static VisitData createVisit(
            Map<String, ZoneData> zones, Map<String, UserData> userMap, String userName, TodayZone todayZone) {
        return switch (todayZone.getActivity()) {
            case "Takeover" -> new TakeData(getZone(zones, todayZone.getZoneName()), getWhen(todayZone.getDate()),
                    getUser(userMap, userName));
            case "Lost" -> new TakeData(getZone(zones, todayZone.getZoneName()), getWhen(todayZone.getDate()),
                    getUser(userMap, todayZone.getUserId()));
            case "Assist" -> new AssistData(getZone(zones, todayZone.getZoneName()), getWhen(todayZone.getDate()),
                    getUser(userMap, todayZone.getUserId()), getUser(userMap, userName));
            case "Revisit" -> new RevisitData(getZone(zones, todayZone.getZoneName()), getWhen(todayZone.getDate()),
                    getUser(userMap, userName));
            default -> throw new RuntimeException("Activity " + todayZone.getActivity() + " not implemented yet.");
        };
    }

    private static ZoneData getZone(Map<String, ZoneData> zones, String zoneName) {
        return Objects.requireNonNull(zones.get(zoneName), String.format("Unknown zone %s", zoneName));
    }

    private static UserData getUser(Map<String, UserData> userMap, String userName) {
        return Objects.requireNonNull(userMap.get(userName), String.format("Unknown user %s", userName));
    }

    private static Instant getWhen(String datetime) {
        return TimeUtil.zundinTimestampToInstant(datetime);
    }

    public List<VisitData> getVisits(UserData user) {
        return dbEntity.getVisits(user);
    }

    public List<VisitData> getAllVisits() {
        return dbEntity.getAllVisits();
    }

    public void updateVisits(Today today) {
        Map<String, UserData> userMap = getUsers(today);
        Map<String, ZoneData> zoneMap = getZones(today);

        List<VisitData> visits = today.getZones().stream()
                .map(todayZone -> createVisit(zoneMap, userMap, today.getUserName(), todayZone)).toList();
        dbEntity.updateVisits(visits);
    }

    private Map<String, UserData> getUsers(Today today) {
        Set<String> userNames = new HashSet<>(today.getZones().size() + 1);
        userNames.add(today.getUserName());
        today.getZones().forEach(todayZone -> userNames.add(todayZone.getUserId()));
        return dbEntity.getUsers().stream().filter(userData -> userNames.contains(userData.getName()))
                .collect(Collectors.toMap(UserData::getName, Function.identity()));
    }

    private Map<String, ZoneData> getZones(Today today) {
        Set<String> zoneNames = new HashSet<>(today.getZones().size());
        today.getZones().forEach(todayZone -> zoneNames.add(todayZone.getZoneName()));
        return dbEntity.getZones().stream().filter(zoneData -> zoneNames.contains(zoneData.getName()))
                .collect(Collectors.toMap(ZoneData::getName, Function.identity()));
    }
}
