package ua.nekl08.varus.repository;

import org.springframework.data.repository.CrudRepository;
import ua.nekl08.varus.model.Template;

public interface TemplateRepository extends CrudRepository<Template, Long> {
}
