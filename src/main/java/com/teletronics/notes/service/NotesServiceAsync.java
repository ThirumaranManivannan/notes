package com.teletronics.notes.service;

import com.teletronics.notes.document.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotesServiceAsync {

    private final MongoTemplate mongoTemplate;
    Pattern pattern = Pattern.compile("\\s+");

    public NotesServiceAsync(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Async
    public void updateWordCounts(String noteId, String text) {
        try {
            Map<String, Long> wordCountMap = Arrays.stream(text.split(" "))
                    .map(line -> line.replaceAll("[^a-zA-Z0-9 ]", " "))
                    .flatMap(line -> pattern.splitAsStream(line))
                    .collect(Collectors.groupingBy(NotesServiceAsync::toLowerCase,
                            LinkedHashMap::new, Collectors.counting()));

            List<Map.Entry<String, Long>> sortedEntries = wordCountMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .collect(Collectors.toList());

            Map<String, Long> sortedWordCountMap = new LinkedHashMap<>();
            for (Map.Entry<String, Long> entry : sortedEntries) {
                sortedWordCountMap.put(entry.getKey(), entry.getValue());
            }
            mongoTemplate.updateFirst(
                    org.springframework.data.mongodb.core.query.Query.query(Criteria.where("_id").is(noteId)),
                    org.springframework.data.mongodb.core.query.Update.update("wordCountMap", sortedWordCountMap),
                    Note.class
            );
        }catch (Exception exception){
            log.error("Fall back to normal logic");
            mongoTemplate.updateFirst(
                                org.springframework.data.mongodb.core.query.Query.query(Criteria.where("_id").is(noteId)),
                                org.springframework.data.mongodb.core.query.Update.update("wordCountMap", fallbackCalc(text)),
                                Note.class
                        );
        }


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

    }

    static String toLowerCase(String s) {
        return s.toLowerCase();
    }

    public LinkedHashMap<String, Integer> fallbackCalc(String text){
        String[] words = text.split(" ");
        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            word = word.replaceAll("[^a-zA-Z0-9]", "");
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }
        return wordCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }
}
