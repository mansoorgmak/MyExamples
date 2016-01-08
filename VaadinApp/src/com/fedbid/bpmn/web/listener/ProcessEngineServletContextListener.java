package com.fedbid.bpmn.web.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;

@WebListener
public class ProcessEngineServletContextListener implements ServletContextListener {

	private static final Logger log = Logger.getLogger(ProcessEngineServletContextListener.class.getName());

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		log.info("Destroying process engines");
		ProcessEngines.destroy();
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.info("======================= VAADIN App Initializing process engines ===========================================");
		ProcessEngines.init();
		//log.info(" =============================== process engine =============== " + ProcessEngines.getDefaultProcessEngine());
		createGroupsIfNotPresent();
		createAdminUserIfNotPresent();
		deployProcesses();
	}

	private void createAdminUserIfNotPresent() {
		if (!isAdminUserPresent()) {
			createAdminUser();
		}
	}

	private void createGroupsIfNotPresent() {
		if (!isGroupPresent("managers")) {
			createGroup("managers", "Managers");
		}
		if (!isGroupPresent("developers")) {
			createGroup("developers", "Developers");
		}
		if (!isGroupPresent("reporters")) {
			createGroup("reporters", "Reporters");
		}
	}

	private boolean isAdminUserPresent() {
		UserQuery query = getIdentityService().createUserQuery();
		query.userId("12345");
		return query.count() > 0;
	}

	private void createAdminUser() {
		log.info("Creating an administration user with the username '12345' and password 'password'");
		User adminUser = getIdentityService().newUser("12345");
		adminUser.setFirstName("Gooty");
		adminUser.setLastName("Administrator");
		adminUser.setPassword("password");
		getIdentityService().saveUser(adminUser);
		assignAdminUserToGroups();
	}

	private void assignAdminUserToGroups() {
		getIdentityService().createMembership("12345", "managers");
		getIdentityService().createMembership("12345", "developers");
		getIdentityService().createMembership("12345", "reporters");
	}

	private boolean isGroupPresent(String groupId) {
		GroupQuery query = getIdentityService().createGroupQuery();
		query.groupId(groupId);
		return query.count() > 0;
	}

	private void createGroup(String groupId, String groupName) {
		log.log(Level.INFO, "Creating a group with the id '{1}' and name '{2}'", new Object[] { groupId, groupName });
		Group group = getIdentityService().newGroup(groupId);
		group.setName(groupName);
		getIdentityService().saveGroup(group);
	}

	private IdentityService getIdentityService() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		log.info("====================================================== processEngine ========================= "+processEngine);
		return processEngine.getIdentityService();
	}

	private void deployProcesses() {
		log.info("Deploying processes");
		RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
		//repositoryService.createDeployment().addClasspathResource("com/github/peholmst/vaadinactivitidemo/bpmn/bugreport.bpmn20.xml").deploy();
		
		DeploymentBuilder builder = repositoryService.createDeployment().addClasspathResource("com/fedbid/bpmn/CreateBuyProcess.bpmn20.xml");
		Deployment deployment = builder.deploy();
			
		System.out.println("=================== deployment ==================== " +deployment.getId() + "  "+deployment.getName());
	}

}