package it.uniroma2.isssr.tools;

import it.uniroma2.isssr.HostSettings;
import it.uniroma2.isssr.Exception.JsonRequestException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class JsonRequest {

	private RestTemplate restTemplate;
	private HostSettings hostSettings;
	private HttpHeaders headers;
	private HttpEntity<String> entity;

	public JsonRequest(HostSettings hostSettings) {


		this.restTemplate = new RestTemplate();
		this.hostSettings = hostSettings;
		String plainCreds = hostSettings.getActivitiUsername() + ":" + hostSettings.getActivitiPassword();
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		this.headers = new HttpHeaders();
		this.headers.add("Authorization", "Basic " + base64Creds);
		this.entity = new HttpEntity<String>(this.headers);

	}

	/**
	 * Normal Header with auth data
	 * 
	 * @return Normal Header with auth data
	 */
	public HttpHeaders getHeaders() {
		return headers;
	}

	/**
	 * GET to "restAddress"
	 * 
	 * @param restAddress
	 *            Address of rest Api of Activiti Rest (from
	 *            localhost:8080/activiti-rest)
	 * @param T
	 *            Class of response to be received
	 * 
	 * @return response of Class T
	 * 
	 * @throws JsonRequestException
	 *             If there is an error during rest request.
	 */
	public <T> ResponseEntity<T> get(String restAddress, Class<T> T) throws JsonRequestException {

		return this.get(restAddress, T, this.entity);
	}

	/**
	 * GET to "restAddress"
	 * 
	 * @param restAddress
	 *            Address of rest Api of Activiti Rest (from
	 *            localhost:8080/activiti-rest)
	 * @param T
	 *            Class of response to be received
	 * @param entity
	 *            Represents an HTTP request or response entity, consisting of
	 *            headers and body.
	 * 
	 * @return response of Class T
	 * 
	 * @throws JsonRequestException
	 *             If there is an error during rest request.
	 */
	public <T> ResponseEntity<T> get(String restAddress, Class<T> T, HttpEntity<String> entity)
			throws JsonRequestException {

		System.out.println(hostSettings.getActivitiRestConnectionUrl() + restAddress);

		ResponseEntity<T> response = restTemplate.exchange(hostSettings.getActivitiRestConnectionUrl() + restAddress,
				HttpMethod.GET, entity, T);
		if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED
				&& !response.hasBody()) {
			throw new JsonRequestException(response.getStatusCode().toString());
		}
		return response;
	}

	
	
	public void delete(String restAddress) throws JsonRequestException {

		System.out.println(hostSettings.getActivitiRestConnectionUrl() + restAddress);
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<?> response = restTemplate.exchange(hostSettings.getActivitiRestConnectionUrl() + restAddress,
				HttpMethod.DELETE, entity, String.class);
		if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.NO_CONTENT) {
			throw new JsonRequestException(response.getStatusCode().toString());
		}
		return;
	}
	
	
	/**
	 * POST to "restAddress"
	 * 
	 * @param restAddress
	 *            Address of rest Api of Activiti Rest (from
	 *            localhost:8080/activiti-rest)
	 * @param requestBody
	 *            Data to sent
	 * @param E
	 *            Class of response to be received
	 * 
	 * @return response of Class E
	 * 
	 * @throws JsonRequestException
	 *             If there is an error during rest request.
	 */
	public <T, E> ResponseEntity<E> post(String restAddress, T requestBody, Class<E> E) throws JsonRequestException {

		System.out.println(hostSettings.getActivitiRestConnectionUrl() + restAddress);
		
		HttpEntity<T> entity = new HttpEntity<T>(requestBody, headers);

		ResponseEntity<E> response = restTemplate.exchange(hostSettings.getActivitiRestConnectionUrl() + restAddress,
				HttpMethod.POST, entity, E);
		if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
			throw new JsonRequestException(response.getStatusCode().toString());
		}
		return response;
	}

	/**
	 * POST to "restAddress" a MultiPart filled with content of a Xml
	 * MetaWorkflow Model
	 * 
	 * @param restAddress
	 *            Address of rest Api of Activiti Rest (from
	 *            localhost:8080/activiti-rest)
	 * @param T
	 *            Class of response to be received
	 * @param metaWorkflowName
	 *            Name of MetaWorkflow
	 * @param multiPartContent
	 *            Data to sent in a MultiPart
	 * 
	 * @return response of Class T
	 * 
	 * @throws JsonRequestException
	 *             If there is an error during rest request.
	 */
	public <T> ResponseEntity<T> postMetaWorkflowMultiPart(String restAddress, Class<T> T, String metaWorkflowName,
                                                           String multiPartContent) throws JsonRequestException {

		System.out.println(hostSettings.getActivitiRestConnectionUrl() + restAddress);
		
		HttpEntity<MultiValueMap<String, Object>> multiPartRequest = buildMultiPartMetaWorkflowRequest(metaWorkflowName,
				multiPartContent);

		ResponseEntity<T> response = restTemplate.exchange(hostSettings.getActivitiRestConnectionUrl() + restAddress,
				HttpMethod.POST, multiPartRequest, T);
		if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
			throw new JsonRequestException(response.getStatusCode().toString());
		}
		return response;
	}

	/**
	 * PUT to "restAddress"
	 * 
	 * @param restAddress
	 *            Address of rest Api of Activiti Rest (from
	 *            localhost:8080/activiti-rest)
	 * @param requestBody
	 *            Data to sent
	 * @param E
	 *            Class of response to be received
	 * 
	 * @return response of Class E
	 * 
	 * @throws JsonRequestException
	 *             If there is an error during rest request.
	 */
	public <T, E> ResponseEntity<E> put(String restAddress, T requestBody, Class<E> E) throws JsonRequestException {

		System.out.println(hostSettings.getActivitiRestConnectionUrl() + restAddress);
		
		HttpEntity<T> entity = new HttpEntity<T>(requestBody, headers);

		ResponseEntity<E> response = restTemplate.exchange(hostSettings.getActivitiRestConnectionUrl() + restAddress,
				HttpMethod.PUT, entity, E);
		if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED
				&& response.getStatusCode() != HttpStatus.ACCEPTED
				&& response.getStatusCode() != HttpStatus.NO_CONTENT) {
			throw new JsonRequestException(response.getStatusCode().toString());
		}
		return response;
	}

	/**
	 * PUT to "restAddress"
	 * 
	 * @param restAddress
	 *            Address of rest Api of Activiti Rest (from
	 *            localhost:8080/activiti-rest)
	 * @param T
	 *            Class of response to be received
	 * @param name
	 *            Name of MultiPart data
	 * @param multiPartContent
	 *            Data to sent wrapped in a MultiPart
	 * 
	 * @return response of Class E
	 * 
	 * @throws JsonRequestException
	 *             If there is an error during rest request.
	 */
	public <T> ResponseEntity<T> putMultiPart(String restAddress, Class<T> T, String name, String multiPartContent)
			throws JsonRequestException {

		System.out.println(hostSettings.getActivitiRestConnectionUrl() + restAddress);
		
		HttpEntity<MultiValueMap<String, Object>> multiPartRequest = buildMultiPart(name, multiPartContent);

		ResponseEntity<T> response = restTemplate.exchange(hostSettings.getActivitiRestConnectionUrl() + restAddress,
				HttpMethod.PUT, multiPartRequest, T);
		if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED
				&& response.getStatusCode() != HttpStatus.ACCEPTED
				&& response.getStatusCode() != HttpStatus.NO_CONTENT) {
			throw new JsonRequestException(response.getStatusCode().toString());
		}
		return response;
	}

	/**
	 * Build an HTTP request or response entity, consisting of headers and
	 * MultiPart Body filled with MetaWorkflow Model
	 * 
	 * @param name
	 *            Name of MetaWorkflow
	 * @param multiPartContent
	 *            MetaWorkflow Model wrapped in a String
	 * 
	 * @return HTTP request or response entity, consisting of headers and
	 *         MultiPart Body
	 */
	private HttpEntity<MultiValueMap<String, Object>> buildMultiPartMetaWorkflowRequest(String name,
                                                                                        String multiPartContent) {
		
		if(name==null) name = "workflow_model_xml";
		
		final String filename = name.replaceAll(" ", "_") + ".bpmn20.xml";
		ByteArrayResource contentsAsResource = null;
		try {
			contentsAsResource = new ByteArrayResource(multiPartContent.getBytes("UTF-8")) {
				@Override
				public String getFilename() {
					return filename;
				}
			};
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
		// creating an HttpEntity for the binary part
		HttpHeaders xmlHeader = new HttpHeaders();
		xmlHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<ByteArrayResource> xmlPart = new HttpEntity<>(contentsAsResource, xmlHeader);

		// putting the two parts in one request
		multipartRequest.add("file", xmlPart);

		return new HttpEntity<>(multipartRequest, headers);
	}

	/**
	 * Build an HTTP request or response entity, consisting of headers and
	 * MultiPart Body.
	 * 
	 * @param name
	 *            Name of MultiPart data
	 * @param multiPartContent
	 *            Data to sent wrapped in a MultiPart
	 * 
	 * @return HTTP request or response entity, consisting of headers and
	 *         MultiPart Body
	 */
	private HttpEntity<MultiValueMap<String, Object>> buildMultiPart(String name, String multiPartContent) {

		final String filename = name;
		ByteArrayResource contentsAsResource = null;
		try {
			contentsAsResource = new ByteArrayResource(multiPartContent.getBytes("UTF-8")) {
				@Override
				public String getFilename() {
					return filename;
				}
			};
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
		// creating an HttpEntity for the binary part
		HttpHeaders xmlHeader = new HttpHeaders();
		xmlHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<ByteArrayResource> multiPart = new HttpEntity<>(contentsAsResource, xmlHeader);

		// putting the two parts in one request
		multipartRequest.add("file", multiPart);

		return new HttpEntity<>(multipartRequest, headers);
	}

}
