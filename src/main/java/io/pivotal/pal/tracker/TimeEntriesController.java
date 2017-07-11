package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntriesController {

    private final CounterService counterService;
    private final GaugeService gaugeService;
    private final TimeEntryRepository timeEntryRepository;

    @Autowired
    public TimeEntriesController(
            TimeEntryRepository timeEntryRepository,
            CounterService counterService,
            GaugeService gaugeService) {
        this.timeEntryRepository = timeEntryRepository;
        this.counterService = counterService;
        this.gaugeService = gaugeService;
    }

    @GetMapping("/timeEntries")
    @ResponseBody
    public ResponseEntity<List<TimeEntry>> list() {
        counterService.increment("TimeEntry.listed");
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @GetMapping("/timeEntries/{id}")
    @ResponseBody
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry timeEntry = timeEntryRepository.get(id);
        if (timeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        counterService.increment("TimeEntry.read");
        return ResponseEntity.ok(timeEntry);
    }

    @PostMapping("/timeEntries")
    @ResponseBody
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntry);
        counterService.increment("TimeEntry.created");
        gaugeService.submit("timeEntries.count", timeEntryRepository.list().size());

        return new ResponseEntity<>(createdTimeEntry, HttpStatus.CREATED);
    }

    @PutMapping("/timeEntries/{id}")
    @ResponseBody
    public ResponseEntity<TimeEntry> update(@PathVariable long id, @RequestBody TimeEntry timeEntry) {
        TimeEntry updatedTimeEntry = timeEntryRepository.update(id, timeEntry);
        if (updatedTimeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        counterService.increment("TimeEntry.updated");
        return ResponseEntity.ok(updatedTimeEntry);
    }

    @DeleteMapping("/timeEntries/{id}")
    @ResponseBody
    public ResponseEntity delete(@PathVariable long id) {
        timeEntryRepository.delete(id);
        counterService.increment("TimeEntry.deleted");
        gaugeService.submit("timeEntries.count", timeEntryRepository.list().size());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
