package com.company;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Compulsory {
    /**
     * Compulsory task is completed here
     */
    public Compulsory() {
        //residents List
        Resident[] r = IntStream.rangeClosed(0, 3)
                .mapToObj(i -> new Resident("R" + i) )
                .toArray(Resident[]::new);
        List<Resident> residentList = new ArrayList<>();
        residentList.addAll( Arrays.asList(r) );

        Collections.sort(residentList, (r1, r2) -> r1.getName().compareTo(r2.getName()));

        //hospital TreeSet
        Hospital[] h = IntStream.rangeClosed(0, 2)
                .mapToObj(i -> new Hospital("H" + i, 2) )
                .toArray(Hospital[]::new);
        TreeSet<Hospital> hospitalSet = new TreeSet<>();
        hospitalSet.addAll(Arrays.asList(h));

        //Resident map of preferences
        Map<Resident, TreeSet<Hospital>> resPrefMap = new HashMap<>();
        resPrefMap.put(r[0], new TreeSet<>(hospitalSet));
        resPrefMap.put(r[1], new TreeSet<>(hospitalSet));
        hospitalSet.remove(h[2]);
        resPrefMap.put(r[2], new TreeSet<>(hospitalSet));
        hospitalSet.add(h[2]);
        hospitalSet.remove(h[1]);
        resPrefMap.put(r[3], new TreeSet<>(hospitalSet));
        hospitalSet.add(h[1]);

        //Hospital map of preferences
        Map<Hospital, List<Resident>> hospPrefMap = new HashMap<>();
        hospPrefMap.put(h[0], Arrays.asList(r[3], r[0], r[1], r[2]));
        hospPrefMap.put(h[1], Arrays.asList(r[0], r[2], r[1]));
        hospPrefMap.put(h[2], Arrays.asList(r[0], r[1], r[3]));

        System.out.println("Hospitals:\n"+hospPrefMap);
        System.out.println("Residents:\n"+resPrefMap);

        // residents who find acceptable H0 and H2
        List<Hospital> target = Arrays.asList(h[0], h[2]);
        List<Resident> result = residentList.stream()
                .filter(res -> resPrefMap.get(res).containsAll(target))
                .collect(Collectors.toList());
        System.out.println("\nResidents who find acceptable H0 and H2:");
        System.out.println(result);

        //hospitals that have R0 as their top preference
        List <Hospital> result2 = hospitalSet.stream()
                .filter(hosp -> hospPrefMap.get(hosp).get(0) == r[0])
                .collect(Collectors.toList());
        System.out.println("\nHospitals that have R0 as their top preference:");
        System.out.println(result2);
    }
}
