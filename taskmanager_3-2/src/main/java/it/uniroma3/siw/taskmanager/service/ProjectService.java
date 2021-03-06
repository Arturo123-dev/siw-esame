package it.uniroma3.siw.taskmanager.service;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The ProjectService handles logic for Projects.
 * This mainly consists in CRUD operations, as well as sharing projects with other users.
 */
@Service
public class ProjectService {

    @Autowired
    protected ProjectRepository projectRepository;

    /**
     * This method retrieves a Project from the DB based on its ID.
     * @param id the id of the Project to retrieve from the DB
     * @return the retrieved Project, or null if no Project with the passed ID could be found in the DB
     */
    @Transactional
    public Project getProject(long id) {
        Optional<Project> result = this.projectRepository.findById(id);
        return result.orElse(null);
    }

    /**
     * This method saves a Project in the DB.
     * @param project the Project to save into the DB
     * @return the saved Project
     */
    @Transactional
    public Project saveProject(Project project, User user) {
    	project.addMember(user);
    	project.setOwner(user);
        return this.projectRepository.save(project);
    }
    
    @Transactional
    public List<User> getAllMembers(Long id){
    	return this.getProject(id).getMembers();
    }

    /**
     * This method deletes a Project from the DB.
     * @param project the Project to delete from the DB
     */
    @Transactional
    public void deleteProject(Project project) {
        this.projectRepository.delete(project);
    }

    /**
     * This method saves a Project among the ones shared with a specific User.
     * @param project the Project to share with the User
     * @param user the User with to share the Project to
     * @return the shared Project
     */
    @Transactional
    public Project shareProjectWithUser(Project project, User user) {
        project.addMember(user);
        return this.projectRepository.save(project);
    }
    
    @Transactional
    public Project addTaskProject(Project project, Task task) {
        project.addTask(task);
        return this.projectRepository.save(project);
    }
    
    @Transactional
    public Project addTagProject(Project project, Tag tag) {
        project.addTag(tag);
        return this.projectRepository.save(project);
    }
    
    @Transactional
    public List<Project> retrieveProjectsOwnedBy(User user){
    	return this.projectRepository.findByOwner(user);
    }
    
    @Transactional
    public List<Project> getSharedProjects(User user){
    	return this.projectRepository.findByMembers(user);
    }
    
    @Transactional
    public Project updateProject(Project project, Project project2) {
    	project.setName(project2.getName());
		project.setDescription(project2.getDescription());
        return this.projectRepository.save(project);
    }
    
    @Transactional
    public List<Tag> getAllTags(Long id){
    	return this.getProject(id).getTags();
    }
    
}
