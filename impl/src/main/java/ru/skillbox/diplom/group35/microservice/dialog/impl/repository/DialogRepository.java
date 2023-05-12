package ru.skillbox.diplom.group35.microservice.dialog.impl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group35.library.core.repository.BaseRepository;
import ru.skillbox.diplom.group35.microservice.dialog.domain.model.Dialog;

/**
 * DialogRepository
 *
 * @author Georgii Vinogradov
 */

@Repository
public interface DialogRepository extends BaseRepository<Dialog> {

}
