package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntriesController {

    private TimeEntryRepository timeEntryRepository;

    @Autowired
    public TimeEntriesController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @GetMapping("/timeEntries")
    @ResponseBody
    public ResponseEntity<List<TimeEntry>> list() {
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @GetMapping("/timeEntries/{id}")
    @ResponseBody
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry timeEntry = timeEntryRepository.get(id);
        if (timeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(timeEntry);
    }

    @PostMapping("/timeEntries")
    @ResponseBody
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntry);
        return new ResponseEntity<>(createdTimeEntry, HttpStatus.CREATED);
    }

    @PutMapping("/timeEntries/{id}")
    @ResponseBody
    public ResponseEntity<TimeEntry> update(@PathVariable long id, @RequestBody TimeEntry timeEntry) {
        TimeEntry updatedTimeEntry = timeEntryRepository.update(id, timeEntry);
        if (updatedTimeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(updatedTimeEntry);
    }

    @DeleteMapping("/timeEntries/{id}")
    @ResponseBody
    public ResponseEntity delete(@PathVariable long id) {
        timeEntryRepository.delete(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
