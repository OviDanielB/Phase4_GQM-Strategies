package it.uniroma2.isssr.rest;

import it.uniroma2.isssr.controller42.StrategyService;
import it.uniroma2.isssr.hermes.Bus2fase32;
import it.uniroma2.isssr.hermes.BusInterationImplementation;
import it.uniroma2.isssr.model42.rest.DTO;
import it.uniroma2.isssr.model42.rest.DTOLevel3Request;
import it.uniroma2.isssr.model42.rest.response.DTOResponseStrategy;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notify/")
public class RestPresentationBus {
	@Autowired
	StrategyService strategyService;
	@Autowired
	Bus2fase32 bus2fase32;
	@Autowired
	BusInterationImplementation busInterationImplementation;

	@RequestMapping(value = "/eventonstrategy", method = RequestMethod.POST)
	public ResponseEntity<DTOLevel3Request> createStrategy(@RequestBody DTOLevel3Request dtoLevel3Request) {

		String data = dtoLevel3Request.getData();
		JSONObject message;
		String typeobj = "";
		try {
			message = new JSONObject(data);
			typeobj = message.getString("typeObj");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (typeobj.equals("base64-TerminalStrategy")) {
			strategyService.updateStrategyF1();
			return new ResponseEntity<DTOLevel3Request>(dtoLevel3Request, HttpStatus.OK);
		}

		return new ResponseEntity<DTOLevel3Request>(dtoLevel3Request, HttpStatus.BAD_REQUEST);

	}

	@RequestMapping(value = "/tryCreate", method = RequestMethod.GET)
	public ResponseEntity<DTOResponseStrategy> createPhase56() {

		DTOResponseStrategy result = new DTOResponseStrategy();
		result.setMessage(bus2fase32.saveValitatedDataOnBus("sid-9C821864-D5BC-4680-89F5-1C8E6CF57C50"));
		return new ResponseEntity<DTOResponseStrategy>(result, HttpStatus.OK);

	}
	@RequestMapping(value = "/alertPhase2", method = RequestMethod.GET)
	public ResponseEntity<DTO> sendNotification(@RequestParam("id") String id) {
		String result = busInterationImplementation.alertPhase2WrongStrategy(id);
		DTO dto = new DTO();
		dto.setMessage(result);
		return new ResponseEntity<DTO>(dto, HttpStatus.OK);

	}

	@RequestMapping(value = "/tryStrategy", method = RequestMethod.GET)
	public ResponseEntity tryStrategy() {
		ResponseEntity result = strategyService.updateStrategyF1();
		DTO dto = new DTO();
		dto.setMessage("done");
		return result;

	}

}
