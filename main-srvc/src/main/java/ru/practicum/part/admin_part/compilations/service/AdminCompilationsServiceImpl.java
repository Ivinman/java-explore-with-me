package ru.practicum.part.admin_part.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationMapper;
import ru.practicum.dto.compilation.CompilationOutDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.exception.BadRequestException;
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
        if (compilationDto.getTitle() == null
                || compilationDto.getTitle().isBlank()
                || compilationDto.getTitle().isEmpty()) {
            throw new BadRequestException("Field title filled incorrectly");
        }
        compNameValid(compilationDto);

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

        CompilationOutDto compilationOutDto = CompilationMapper.toCompOutDto(compilation);
        compilationOutDto.setEvents(events);
        compilationOutDto.setId(compilationRepository.findFirstByOrderByIdDesc().getId());

        return compilationOutDto;
    }

    @Override
    public void deleteComp(Integer compId) throws Exception {
        if (compilationRepository.findById(compId).isEmpty()) {
            throw new NotFoundException("Compilation with id=" + compId + " not found");
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationOutDto editComp(Integer compId, CompilationDto compilationDto) throws Exception {
        if (compilationRepository.findById(compId).isEmpty()) {
            throw new NotFoundException("Compilation with id=" + compId + " not found");
        }

        Compilation compilation = compilationRepository.findById(compId).get();
        if (compilationDto.getTitle() != null) {
            compNameValid(compilationDto);
            compilation.setTitle(compilationDto.getTitle());
        }
        compilation.setPinned(compilationDto.getPinned());
        compilationRepository.save(compilation);

        List<ShortEventDto> events = new ArrayList<>();
        if (compilationDto.getEvents() != null) {
            for (Integer eventId : compilationDto.getEvents()) {
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

        CompilationOutDto compilationOutDto = CompilationMapper.toCompOutDto(compilation);
        compilationOutDto.setEvents(events);
        compilationOutDto.setId(compId);

        return compilationOutDto;
    }

    private void compNameValid(CompilationDto compDto) throws Exception {
        if (compDto.getTitle().length() > 50) {
            throw new BadRequestException("Title is too long");
        }
    }
}