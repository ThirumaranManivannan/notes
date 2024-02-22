package com.teletronics.notes.service;

import com.teletronics.notes.document.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotesServiceAsync {

    private final MongoTemplate mongoTemplate;

    public NotesServiceAsync(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Async
    public void updateWordCounts(String noteId, String text) {
        log.error("going to calculate...........");
        String[] words = text.split(" ");
        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            word = word.replaceAll("[^a-zA-Z0-9]", "");
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }
        LinkedHashMap<String, Integer> sortedMap = wordCount.entrySet().stream()
              .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
              .collect(Collectors.toMap(Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (e1, e2) -> e1, LinkedHashMap::new));
//        AggregationOperation match = Aggregation.match(Criteria.where("_id").is(noteId));
//        AggregationOperation toUpperCase = Aggregation.project("text")
//                .and(StringOperators.valueOf("text").toLower()).as("text");
//        AggregationOperation splitWords = Aggregation.project("text")
//                .and(StringOperators.valueOf("text").split(" ")).as("words");
//        AggregationOperation unwind = Aggregation.unwind("words");
//        GroupOperation group = Aggregation.group("words").count().as("count");
//        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "count");
//        AggregationOperation project = Aggregation.project("count").and("_id").as("word");

//        List<Map> result = mongoTemplate.aggregate(
//                Aggregation.newAggregation(match, toUpperCase, splitWords, unwind, group, sort, project),
//                "notes", Map.class
//        ).getMappedResults();

//        Map<String, Integer> wordCountMap = result.stream()
//                .collect(Collectors.toMap(entry -> (String) entry.get("word"), entry -> (Integer) entry.get("count"), (existing, replacement) -> existing, LinkedHashMap::new));

        mongoTemplate.updateFirst(
                org.springframework.data.mongodb.core.query.Query.query(Criteria.where("_id").is(noteId)),
                org.springframework.data.mongodb.core.query.Update.update("wordCountMap", sortedMap),
                Note.class
        );
    }
}
