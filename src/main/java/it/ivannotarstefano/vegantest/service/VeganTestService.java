package it.ivannotarstefano.vegantest.service;

import it.ivannotarstefano.vegantest.dto.VeganTestDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeganTestService {
	
	
	public String getIds(List<VeganTestDTO> veganTestDTOList, Double maxWeight){
		String response;
		
		if(veganTestDTOList.size() > 15){
			response =  "Error: the maximum number of items allowed (15) has been exceeded";
		}else if(veganTestDTOList.stream().map(VeganTestDTO::getWeight).max(Double::compare).get() > 100){
			response = "Error: the maximum weight of an item allowed (100kg) has been exceeded";
		}else if(veganTestDTOList.stream().map(VeganTestDTO::getPrice).max(Double::compare).get() > 100){
			response = "Error: the maximum price of an item allowed (100€) has been exceeded";
		}else if(maxWeight > 100){
			response = "Error: the maximum weight that package can hold (100kg) has been exceeded";
		}else{
			List<Integer> idList = new ArrayList<>();
			
			//Get a list that contains only elements with weight lower than maxWeight and sorted by descending price
			List<VeganTestDTO> sortedList = veganTestDTOList.stream()
					.filter(vtd -> vtd.getWeight() < maxWeight)
					.sorted(Comparator.comparing(VeganTestDTO::getWeight))
					.sorted(Comparator.comparing(VeganTestDTO::getPrice).reversed())
					.collect(Collectors.toList());
			
			Double remainingWeight = maxWeight;
			
			//get the weight of the lighter object in the list
			Double minWeight = veganTestDTOList.stream().map(VeganTestDTO::getWeight).min(Double::compare).get();
			
			for(VeganTestDTO selectedObject : sortedList){
				if(selectedObject.getWeight() < remainingWeight){
					idList.add(selectedObject.getId());
					remainingWeight = remainingWeight - selectedObject.getWeight();
					
					//check if the remaining weight is enough for add at least  the lighter object if not stop the loop
					if(remainingWeight < minWeight){
						break;
					}
				}
			}
			
			if(idList.isEmpty()){
				response = "Error: No items can be placed in the package under the given constraints";
			}else{
				response = idList.toString();
			}
		}
		
		return response;
	}
}
