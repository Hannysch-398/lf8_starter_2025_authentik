package de.szut.lf8_starter.project;

import de.szut.lf8_starter.hello.HelloRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service
public class ProjectService {

    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {

        this.repository = repository;
    }
    public ProjectEntity create(ProjectEntity newProject){
        return repository.save(newProject);
    }
}
