package ua.nekl08.varus.repository;

import org.springframework.data.repository.CrudRepository;
import ua.nekl08.varus.model.Document;

public interface DocumentRepository extends CrudRepository<Document, Long> {

}

