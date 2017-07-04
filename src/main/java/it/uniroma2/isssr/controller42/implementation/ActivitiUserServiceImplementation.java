package it.uniroma2.isssr.controller42.implementation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.uniroma2.isssr.activiti.rest.ActivitiInterationImplementation;
import it.uniroma2.isssr.controller42.ActivitiUserService;
import it.uniroma2.isssr.Exception.ActivitiGetException;
import it.uniroma2.isssr.model42.activiti.user.ActivitiUser;
import it.uniroma2.isssr.model42.rest.response.activiti.DTOResponseActivitiUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("ActivitiUserService")
public class ActivitiUserServiceImplementation implements ActivitiUserService {
	@Autowired
	ActivitiInterationImplementation actitiviInterationImplementation;
	
	/**
	 * Metodo che restituisce la lista degli utenti di Activiti Rest
	 */
	
	public ResponseEntity<?> getUsers() throws JsonParseException, JsonMappingException, IOException, ActivitiGetException {
		// TODO Auto-generated method stub
			List<ActivitiUser> users = actitiviInterationImplementation.getUsers();
			List<DTOResponseActivitiUser> dtoResponseActivitiUserList=
					new ArrayList<DTOResponseActivitiUser>();
			for(ActivitiUser user : users){
				DTOResponseActivitiUser dtoResponseActivitiUser =
						new DTOResponseActivitiUser();
				dtoResponseActivitiUser.setId(user.getId());
				dtoResponseActivitiUser.setFirstName(user.getFirstName());
				dtoResponseActivitiUser.setLastName(user.getLastName());
				dtoResponseActivitiUser.setUrl(user.getUrl());
				dtoResponseActivitiUser.setEmail(user.getEmail());
				dtoResponseActivitiUserList.add(dtoResponseActivitiUser);
			}
			ResponseEntity<List<DTOResponseActivitiUser>> responseEntity =
					new ResponseEntity<List<DTOResponseActivitiUser>>(dtoResponseActivitiUserList, HttpStatus.OK);
			return responseEntity;
	}
}
