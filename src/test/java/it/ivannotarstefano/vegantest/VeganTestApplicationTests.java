package it.ivannotarstefano.vegantest;

import it.ivannotarstefano.vegantest.controller.VeganTestController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class VeganTestApplicationTests {
	
	@Autowired
	private VeganTestController veganTestController;
	
	@Autowired
	private MockMvc mvc;

	@Test
	void contextLoads() {
		Assertions.assertThat(veganTestController).isNotNull();
	}
	
	@Test
	public void getErrorWhenMaxWeightIsTooMuch() throws Exception {
		
		String baseJson = "{\"id\":1,\"weight\":85.31,\"price\":75}";
		String json = "[" + baseJson + "]";
		
		String expected = "Error: the maximum weight that package can hold (100kg) has been exceeded";
		
		mvc.perform(MockMvcRequestBuilders.post("/api/getIds?packageMaxWeight=101")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(json))
				.andExpect(content().string(expected))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void getErrorWhenItemWeightIsTooMuch() throws Exception {
		
		String baseJson = "{\"id\":1,\"weight\":101,\"price\":75}";
		String json = "[" + baseJson + "]";
		
		String expected = "Error: the maximum weight of an item allowed (100kg) has been exceeded";
		
		mvc.perform(MockMvcRequestBuilders.post("/api/getIds?packageMaxWeight=75")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(json))
				.andDo(print())
				.andExpect(content().string(expected))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void getErrorWhenItemPriceIsTooMuch() throws Exception {
		
		String baseJson = "{\"id\":1,\"weight\":75,\"price\":101}";
		String json = "[" + baseJson + "]";
		
		String expected = "Error: the maximum price of an item allowed (100€) has been exceeded";
		
		mvc.perform(MockMvcRequestBuilders.post("/api/getIds?packageMaxWeight=75")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(json))
				.andExpect(content().string(expected))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void getErrorWhenItemAreTooMuch() throws Exception {
		
		String baseJson = "{\"id\":1,\"weight\":75,\"price\":75}";
		String json = "[";
		for(int i = 0; i < 16; i++){
			json += baseJson + ",";
		}
		json += baseJson +"]";
		
		String expected = "Error: the maximum number of items allowed (15) has been exceeded";
		
		mvc.perform(MockMvcRequestBuilders.post("/api/getIds?packageMaxWeight=75")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(json))
				.andExpect(content().string(expected))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void getErrorWhenItemWeightAreBiggerThanMaxWeight() throws Exception {
		
		String baseJson = "{\"id\":1,\"weight\":75,\"price\":75}";
		String json = "[";
		for(int i = 0; i < 1; i++){
			json += baseJson + ",";
		}
		json += baseJson +"]";
		
		String expected = "Error: No items can be placed in the package under the given constraints";
		
		mvc.perform(MockMvcRequestBuilders.post("/api/getIds?packageMaxWeight=7")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(json))
				.andExpect(content().string(expected))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void responseOk() throws Exception {

		String json = "    [\n" +
				"        {\"id\":1,\n" +
				"        \"weight\":85.31,\n" +
				"        \"price\":29\n" +
				"        },\n" +
				"        {\"id\":2,\n" +
				"        \"weight\":14.55,\n" +
				"        \"price\":74\n" +
				"        },\n" +
				"\t\t{\"id\":3,\n" +
				"        \"weight\":3.98,\n" +
				"        \"price\":16\n" +
				"        },\n" +
				"\t\t{\"id\":4,\n" +
				"        \"weight\":26.24,\n" +
				"        \"price\":55\n" +
				"        },\n" +
				"\t\t{\"id\":5,\n" +
				"        \"weight\":63.69,\n" +
				"        \"price\":52\n" +
				"        },\n" +
				"\t\t{\"id\":6,\n" +
				"        \"weight\":76.25,\n" +
				"        \"price\":75\n" +
				"        },\n" +
				"\t\t{\"id\":7,\n" +
				"        \"weight\":60.02,\n" +
				"        \"price\":74\n" +
				"        },\n" +
				"\t\t{\"id\":8,\n" +
				"        \"weight\":93.18,\n" +
				"        \"price\":35\n" +
				"        },\n" +
				"\t\t{\"id\":9,\n" +
				"        \"weight\":89.95,\n" +
				"        \"price\":78\n" +
				"        }\n" +
				"    ]";

		String expected = "[2, 7]";

		mvc.perform(MockMvcRequestBuilders.post("/api/getIds?packageMaxWeight=75")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(json))
				.andExpect(content().string(expected))
				.andExpect(status().is2xxSuccessful());
	}

	
}
