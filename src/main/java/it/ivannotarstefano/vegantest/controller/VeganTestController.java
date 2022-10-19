package it.ivannotarstefano.vegantest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.ivannotarstefano.vegantest.dto.VeganTestDTO;
import it.ivannotarstefano.vegantest.service.VeganTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Vegan Test Project")
@RestController
@RequestMapping("/api")
public class VeganTestController {
	
	@Autowired
	private VeganTestService veganTestService;
	
	@Operation(description = "Returns the list of IDs of the elements with the highest price whose added weight does not exceed the maximunWeight")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Return the IDs of the object that  can maximize the price of the final package without exceding the maximumWeight",
					content = { @Content(mediaType = "text/plain") }),
			@ApiResponse(responseCode = "400", description = "Error in the request",
					content = @Content)})
	@PostMapping(value = "/getIds")
	public ResponseEntity<String> getIds(@Parameter(description = "The list of the objects to build the final package") @RequestBody List<VeganTestDTO> veganTestDTOList,
	                                     @Parameter(description = "The maximum weight that the returned package could have") @RequestParam(name = "packageMaxWeight") Double packageMaxWeight){
		
		String response = veganTestService.getIds(veganTestDTOList, packageMaxWeight);
		
		if(response.contains("Error")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
