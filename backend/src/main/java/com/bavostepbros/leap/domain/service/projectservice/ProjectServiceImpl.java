package com.bavostepbros.leap.domain.service.projectservice;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Program;
import com.bavostepbros.leap.domain.model.Project;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.persistence.ProgramDAL;
import com.bavostepbros.leap.persistence.ProjectDAL;
import com.bavostepbros.leap.persistence.StatusDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
	
	@Autowired
    private ProgramDAL programDAL;
	
	@Autowired
    private StatusDAL statusDAL;
	
	@Autowired
    private ProjectDAL projectDAL;
	
	@Lazy
	@Autowired
	private CapabilityService capabilityService;
	
	
	/** 
	 * @param projectName
	 * @param programId
	 * @param statusId
	 * @return Project
	 */
	@Override
	public Project save(String projectName, Integer programId, Integer statusId) {
		Optional<Program> program = programDAL.findById(programId);
		program.orElseThrow(() -> new NullPointerException("Program does not exist."));
		Optional<Status> status = statusDAL.findById(statusId);
		status.orElseThrow(() -> new NullPointerException("Status does not exist."));
		Project project = new Project(projectName, program.get(), status.get());
		return projectDAL.save(project);
	}

	
	/** 
	 * @param projectId
	 * @return Project
	 */
	@Override
	public Project get(Integer projectId) {
		Optional<Project> project = projectDAL.findById(projectId);
		project.orElseThrow(() -> new NullPointerException("Project does not exist."));
		return project.get();
	}

	
	/** 
	 * @param projectId
	 * @param projectName
	 * @param programId
	 * @param statusId
	 * @return Project
	 */
	@Override
	public Project update(Integer projectId, String projectName, Integer programId, Integer statusId) {
		Optional<Program> program = programDAL.findById(programId);
		program.orElseThrow(() -> new NullPointerException("Program does not exist."));
		Optional<Status> status = statusDAL.findById(statusId);
		status.orElseThrow(() -> new NullPointerException("Status does not exist."));
		Project project = new Project(projectId, projectName, program.get(), status.get());
		return projectDAL.save(project);
	}

	
	/** 
	 * @param projectId
	 */
	@Override
	public void delete(Integer projectId) {
		projectDAL.deleteById(projectId);
	}

	
	/** 
	 * @return List<Project>
	 */
	@Override
	public List<Project> getAll() {
		return projectDAL.findAll();
	}

	
	/** 
	 * @param programId
	 * @return List<Project>
	 */
	@Override
	public List<Project> getAllProgramId(Integer programId) {
		Optional<Program> program = programDAL.findById(programId);
		program.orElseThrow(() -> new NullPointerException("Program does not exist."));
		return projectDAL.findByProgram(program.get());
	}

	
	/** 
	 * @param projectName
	 * @return Project
	 */
	@Override
	public Project getProjectByName(String projectName) {
		Optional<Project> project = projectDAL.findByProjectName(projectName);
		project.orElseThrow(() -> new NullPointerException("Project does not exist."));
		return project.get();
	}

	
	/** 
	 * @param projectId
	 * @param capabilityId
	 */
	@Override
	public void addCapability(Integer projectId, Integer capabilityId) {
		Project project = get(projectId);
		Capability capability = capabilityService.get(capabilityId);
		project.addCapability(capability);
		return;
	}

	
	/** 
	 * @param projectId
	 * @param capabilityId
	 */
	@Override
	public void deleteCapability(Integer projectId, Integer capabilityId) {
		Project project = get(projectId);
		Capability capability = capabilityService.get(capabilityId);
		project.removeCapability(capability);
		return;
	}

	
	/** 
	 * @param projectId
	 * @return Set<Capability>
	 */
	@Override
	public Set<Capability> getAllCapabilitiesByProjectId(Integer projectId) {
		Project project = get(projectId);
		return project.getCapabilities();
	}

}
