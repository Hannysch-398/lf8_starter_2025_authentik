package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.hello.HelloRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
@Service
public class ProjectService {

    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {

        this.repository = repository;
    }

    public ProjectEntity create(ProjectEntity newProject) {
        return repository.save(newProject);
    }

    public List<ProjectEntity> readAll() {
        return repository.findAll();
    }

    public ProjectEntity readByID(long id) {

        Optional<ProjectEntity> oProject = repository.findById(id);

        if (oProject.isEmpty()) {
            throw new ResourceNotFoundException("Entity not found on Id = " + id);
        }

        return oProject.get();

    }

    public void delete(long id) {
        ProjectEntity entityToDelete = readByID(id);
        repository.delete(entityToDelete);
    }
}
