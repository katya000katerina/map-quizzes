package com.mapquizzes.services.implemenations;

import com.mapquizzes.services.interfaces.EntityExistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EntityExistenceServiceImpl implements EntityExistenceService {
    private final ApplicationContext context;
    private final Map<String, JpaRepository<?, Integer>> repositoryCache = new ConcurrentHashMap<>();

    public boolean checkIfEntityExists(String entityName, Integer id) {
        JpaRepository<?, Integer> repository =
                repositoryCache.computeIfAbsent(entityName, this::findRepository);
        return repository.existsById(id);
    }

    private JpaRepository<?, Integer> findRepository(String entityName) {
        String repositoryBeanName = entityName.toLowerCase() + "Repository";

        if (context.containsBean(repositoryBeanName)) {
            return (JpaRepository<?, Integer>) context.getBean(repositoryBeanName);
        }

        throw new IllegalArgumentException("Repository not found for entity: " + entityName);
    }
}
