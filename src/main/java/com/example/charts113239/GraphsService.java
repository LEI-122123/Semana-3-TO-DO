package com.example.charts113239;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GraphsService {

    private final GraphsRepository graphsRepository;

    GraphsService(GraphsRepository taskRepository) {
        this.graphsRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<Graphs> list(Pageable pageable) {
        return graphsRepository.findAllBy(pageable).toList();
    }

}
