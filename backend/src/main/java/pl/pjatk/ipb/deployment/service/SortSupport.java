package pl.pjatk.ipb.deployment.service;

import org.springframework.data.domain.Sort;

import java.util.Set;

final class SortSupport {

    private SortSupport() {
    }

    static Sort parse(String sortParam, Set<String> allowed, String defaultField) {
        if (sortParam == null || sortParam.isBlank()) {
            return Sort.by(Sort.Direction.ASC, defaultField);
        }
        String[] parts = sortParam.split(",");
        String field = parts[0].trim();
        if (!allowed.contains(field)) {
            throw new IllegalArgumentException(
                    "Niedozwolone pole sortowania: '" + field + "'. Dozwolone: " + allowed);
        }
        Sort.Direction direction = Sort.Direction.ASC;
        if (parts.length > 1 && parts[1].trim().equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        return Sort.by(direction, field);
    }
}
