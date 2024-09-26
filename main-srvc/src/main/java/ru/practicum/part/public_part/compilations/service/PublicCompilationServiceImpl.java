package ru.practicum.part.public_part.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationOutDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.storage.compilation.CompilationRepository;
import ru.practicum.storage.event.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationOutDto> getComp(Boolean pinned, Integer from, Integer size) throws Exception {
        List<Compilation> compilations;
        List<CompilationOutDto> compilationOutDtos = new ArrayList<>();
        if (pinned) {
            compilations = compilationRepository.findByPinned(true);
            System.out.println(compilations);
        } else {
            compilations = compilationRepository.findByPinned(false);
        }
        for (Compilation compilation : compilations) {
            List<ShortEventDto> events = new ArrayList<>();
            for (Event event : eventRepository.findByCompilationId(compilation.getId())) {
                events.add(EventMapper.toEventShortDto(event));
            }
            CompilationOutDto compilationOutDto = new CompilationOutDto();
            compilationOutDto.setEvents(events);
            compilationOutDto.setId(compilation.getId());
            compilationOutDto.setPinned(compilation.getPinned());
            compilationOutDto.setTitle(compilation.getTitle());

            compilationOutDtos.add(compilationOutDto);
        }
        return compilationOutDtos.stream().skip(from).limit(size).toList();
    }

    @Override
    public CompilationOutDto getCompById(Integer compId) throws Exception {
        if (compilationRepository.findById(compId).isEmpty()) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found");
        }
        Compilation compilation = compilationRepository.findById(compId).get();

        List<ShortEventDto> events = new ArrayList<>();
        for (Event event : eventRepository.findByCompilationId(compilation.getId())) {
            events.add(EventMapper.toEventShortDto(event));
        }
        CompilationOutDto compilationOutDto = new CompilationOutDto();
        compilationOutDto.setEvents(events);
        compilationOutDto.setId(compilation.getId());
        compilationOutDto.setPinned(compilation.getPinned());
        compilationOutDto.setTitle(compilation.getTitle());

        return compilationOutDto;
    }
}
