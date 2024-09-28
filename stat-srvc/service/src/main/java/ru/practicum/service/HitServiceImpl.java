package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.model.HitMapper;
import ru.practicum.HitStatDto;
import ru.practicum.model.Hit;
import ru.practicum.storage.HitRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;

    @Override
    public void addHit(HitDto hitDto) {
        Hit hit = HitMapper.toHit(hitDto);
        hitRepository.save(hit);
    }

    @Override
    public List<HitStatDto> getStats(String start, String end, List<String> uris, Boolean unique) throws Exception {
        System.out.println("unique = "+ unique);
        System.out.println("uris = " + uris);


        if (Timestamp.valueOf(end).before(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new BadRequestException("End of event is before current date");
        }
        if (uris == null) {
            List<String> allUris = hitRepository.getDistinctUri();
            if (!unique) {
                return hitStatDtosNotUnique(start, end, allUris);
            }
            return hitStatDtosUnique(start, end, allUris);
        }
        if (!unique) {
            return hitStatDtosNotUnique(start, end, uris);
        }
        return hitStatDtosUnique(start, end, uris);
    }

    private List<HitStatDto> hitStatDtosUnique(String start, String end, List<String> uris) {
        List<String> ips = hitRepository.getDistinctIp();

        if (uris.getFirst().contains("[") || uris.getLast().contains("]")) {
            uris.set(0, uris.getFirst().substring(1));
            uris.set(uris.size() - 1, uris.getLast().replaceAll("]", ""));
        }

        List<Hit> hitList = hitRepository.getHitByUrisAndTime(uris,
                Timestamp.valueOf(start), Timestamp.valueOf(end));
        Map<String, HitStatDto> hitMap = new HashMap<>();

        for (String uri : uris) {
            int hits = 0;
            for (String ip : ips) {
                for (Hit hit : hitList) {
                    if (hit.getIp().equals(ip) && hit.getUri().equals(uri)) {
                        HitStatDto hitStatDto = HitMapper.toHitStatDto(hit);
                        hits++;
                        hitStatDto.setHits(hits);
                        hitMap.put(uri, hitStatDto);
                        break;
                    }
                }
            }
        }
        List<HitStatDto> hitStatDtoList = new ArrayList<>(hitMap.values());
        return hitStatDtoList.stream().sorted(Comparator.comparing(HitStatDto::getHits).reversed()).collect(Collectors.toList());
    }

    private List<HitStatDto> hitStatDtosNotUnique(String start, String end, List<String> uris) {
        List<HitStatDto> hitStatDtoList = new ArrayList<>();
        List<Hit> hitList = hitRepository.getHitByUrisAndTime(uris,
                Timestamp.valueOf(start), Timestamp.valueOf(end));
        for (String uri : uris) {
            for (Hit hit : hitList) {
                if (hit.getUri().equals(uri)) {
                    HitStatDto hitStatDto = HitMapper.toHitStatDto(hit);
                    hitStatDto.setHits(hitRepository.getUriHits(uri,
                            Timestamp.valueOf(start), Timestamp.valueOf(end)));
                    hitStatDtoList.add(hitStatDto);
                    break;
                }
            }
        }
        return hitStatDtoList.stream().sorted(Comparator.comparing(HitStatDto::getHits).reversed()).collect(Collectors.toList());
    }
}
