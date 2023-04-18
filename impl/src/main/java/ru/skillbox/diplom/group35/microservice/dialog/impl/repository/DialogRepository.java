package ru.skillbox.diplom.group35.microservice.dialog.impl.repository;

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
