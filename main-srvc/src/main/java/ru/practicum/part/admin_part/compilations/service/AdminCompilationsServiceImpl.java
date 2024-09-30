package ru.practicum.part.admin_part.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationEditDto;
import ru.practicum.dto.compilation.CompilationMapper;
import ru.practicum.dto.compilation.CompilationOutDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.storage.compilation.CompilationRepository;
import ru.practicum.storage.event.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    public CompilationOutDto addComp(CompilationDto compilationDto) throws Exception {
        if (compilationRepository.findByTitle(compilationDto.getTitle()) != null) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "Compilation already exist");
        }

        Compilation compilation = new Compilation();
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        } else {
            compilation.setPinned(false);
        }
        compilation.setTitle(compilationDto.getTitle());
        compilationRepository.save(compilation);

        List<ShortEventDto> events = new ArrayList<>();
        if (compilationDto.getEvents() != null) {
            for (Integer eventId : compilationDto.getEvents()) {
                Event event = eventRepository.findById(eventId).get();
                events.add(EventMapper.toEventShortDto(event));
                event.setCompilation(compilationRepository.findFirstByOrderByIdDesc());
                eventRepository.save(event);
            }
        }
        return getCompilationOutDto(compilation, events, compilationRepository.findFirstByOrderByIdDesc());
    }

    @Override
    public void deleteComp(Integer compId) throws Exception {
        compFoundCheck(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationOutDto editComp(Integer compId, CompilationEditDto compilationEditDto) throws Exception {
        compFoundCheck(compId);

        Compilation compilation = compilationRepository.findById(compId).get();
        if (compilationEditDto.getTitle() != null) {
            compilation.setTitle(compilationEditDto.getTitle());
        }
        compilation.setPinned(compilationEditDto.getPinned());
        compilationRepository.save(compilation);

        List<ShortEventDto> events = new ArrayList<>();
        if (compilationEditDto.getEvents() != null) {
            for (Integer eventId : compilationEditDto.getEvents()) {
                Event event = eventRepository.findById(eventId).get();
                events.add(EventMapper.toEventShortDto(event));
                event.setCompilation(compilationRepository.findById(compId).get());
                eventRepository.save(event);
            }
        } else {
            for (Event event : eventRepository.findByCompilationId(compId)) {
                events.add(EventMapper.toEventShortDto(event));
            }
        }
        return getCompilationOutDto(compilation, events, compilation);
    }

    private CompilationOutDto getCompilationOutDto (Compilation compilation,
                                                    List<ShortEventDto> events, Compilation compFromDb) {
        CompilationOutDto compilationOutDto = CompilationMapper.toCompOutDto(compilation);
        compilationOutDto.setEvents(events);
        compilationOutDto.setId(compFromDb.getId());

        return compilationOutDto;
    }

    private void compFoundCheck(Integer compId) throws Exception {
        if (compilationRepository.findById(compId).isEmpty()) {
            throw new NotFoundException("Compilation with id=" + compId + " not found");
        }
    }
}