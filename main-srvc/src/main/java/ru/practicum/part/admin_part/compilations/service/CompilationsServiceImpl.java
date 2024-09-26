package ru.practicum.part.admin_part.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationOutDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.storage.compilation.CompilationRepository;
import ru.practicum.storage.event.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationsServiceImpl implements CompilationsService {
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
        if (compilationDto.getTitle().length() > 50) {
            throw new BadRequestException("Title is too long");
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

                //eventRepository.findById(eventId).get().setCompilation(compilationRepository.findFirstByOrderByIdDesc());
            }
        }

        CompilationOutDto compilationOutDto = new CompilationOutDto();
        compilationOutDto.setEvents(events);
        compilationOutDto.setId(compilationRepository.findFirstByOrderByIdDesc().getId());
        compilationOutDto.setPinned(compilation.getPinned());
        compilationOutDto.setTitle(compilation.getTitle());

        return compilationOutDto;
    }

    @Override
    public void deleteComp(Integer compId) throws Exception {
        try {
            compilationRepository.deleteById(compId);
        } catch (Exception e) {
            throw new BadRequestException("Fields filled incorrectly");
        }
    }

    @Override
    public CompilationOutDto editComp(Integer compId, CompilationDto compilationDto) throws Exception {


        Compilation compilation = compilationRepository.findById(compId).get();

        if (compilationDto.getTitle() != null) {
            if (compilationDto.getTitle().length() > 50) {
                throw new BadRequestException("Title is too long");
            }
            compilation.setTitle(compilationDto.getTitle());
        }

        compilation.setPinned(compilationDto.getPinned());
        compilationRepository.save(compilation);

        List<ShortEventDto> events = new ArrayList<>();

        if (compilationDto.getEvents() != null) {
            for (Integer eventId : compilationDto.getEvents()) {
                events.add(EventMapper.toEventShortDto(eventRepository.findById(eventId).get()));
                Event event =  eventRepository.findById(eventId).get();
                event.setCompilation(compilationRepository.findById(compId).get());
                eventRepository.save(event);
            }
        } else {
            for (Event event : eventRepository.findByCompilationId(compId)) {
                events.add(EventMapper.toEventShortDto(event));
            }
        }


        CompilationOutDto compilationOutDto = new CompilationOutDto();
        compilationOutDto.setEvents(events);
        compilationOutDto.setId(compId);
        compilationOutDto.setPinned(compilation.getPinned());
        compilationOutDto.setTitle(compilation.getTitle());

        return compilationOutDto;

    }
}
